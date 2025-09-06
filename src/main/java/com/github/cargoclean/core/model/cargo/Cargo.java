package com.github.cargoclean.core.model.cargo;

/*
    Notice on COPYRIGHT:
    -------------------

    Some code in this file is based on, copied from, and or modified from
    the code in the original DDDSample application. Please, see the copyright
    notice in "README.md" and the copy of the original licence in
    "original-license.txt", as well.
 */


import com.github.cargoclean.core.model.Assert;import com.github.cargoclean.core.model.consignment.Consignment;
import com.github.cargoclean.core.model.handling.HandlingHistory;
import com.github.cargoclean.core.model.location.UnLocode;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;

import static com.github.cargoclean.core.model.Assert.notNull;



/*
    References:
    ----------

    1.  Spring Data entity state detection strategy: https://docs.spring.io/spring-data/jdbc/docs/current/reference/html/#is-new-state-detection
 */

/**
 * Modeled after original "se.citerus.dddsample.domain.model.cargo.Cargo".
 */

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Cargo {

    @EqualsAndHashCode.Include
    TrackingId trackingId;

    UnLocode origin;

    Delivery delivery;

    RouteSpecification routeSpecification;

    // itinerary is null before cargo is routed
    Itinerary itinerary;

    // consignments associated with this cargo
    List<Consignment> consignments;

    /**
     * Should not be modified. Maps to a {@literal @Version} field in {@code CargoDbEntity}
     * which will be used by Spring Data JDBC to determine the state (new, existing) of
     * the persistent entity.
     */
    Integer version;

    /*
        This is the only constructor for "Cargo" domain object, we
        make sure no invalid values (nulls) are passed for the
        mandatory attributes.
     */
    @Builder
    public Cargo(TrackingId trackingId, UnLocode origin, Delivery delivery,
                 RouteSpecification routeSpecification, Itinerary itinerary,
                 List<Consignment> consignments, Integer version) {

        /*
           These are mandatory always non-null attributes. We do
           not need to see if these value objects are valid (themselves),
           this will be done upon construction of each object.
         */
        this.trackingId = notNull(trackingId);
        this.origin = notNull(origin);
        this.delivery = notNull(delivery);
        this.routeSpecification = notNull(routeSpecification);

        // these can be null for some instances of Cargo, i.e. initially before routing
        this.itinerary = itinerary;
        this.version = version;

        // consignments list - never null, but can be empty
        this.consignments = Assert.defensiveCopy(consignments);
    }

    public boolean exists() {
        return version != null;
    }

    /**
     * Routes this cargo by assigning new {@code itinerary} to it.
     *
     * @param itinerary itinerary for this cargo
     * @return new {@code Cargo} instance with new itinerary
     * @throws RoutingError if this cargo is already routed or if {@code itinerary} does not satisfy
     *                      {@code routeSpecification} for this cargo
     */
    public Cargo assignItinerary(Itinerary itinerary) {

        // make sure the cargo needs routing
        if (isRouted()) {
            throw new RoutingError("Cargo <%s> is already routed.".formatted(trackingId));
        }

        // make sure selected itinerary satisfies the route specification for the cargo
        if (!routeSpecification.isSatisfiedBy(itinerary)) {
            throw new RoutingError("Provided itinerary does not satisfy route specification for cargo <%s>."
                    .formatted(trackingId));
        }

        // return new instance of "Cargo" with updated itinerary
        return newCargo().itinerary(itinerary).build();
    }

    /**
     * Adds a consignment to this cargo.
     *
     * @param consignment the consignment to add
     * @return new {@code Cargo} instance with the added consignment
     * @throws ConsignmentError if consignment cannot be added (e.g., cargo already received)
     */
    public Cargo addConsignment(Consignment consignment) {
        notNull(consignment);

        // Business rule: consignments can only be added before cargo is received
        if (delivery.getTransportStatus() != TransportStatus.NOT_RECEIVED) {
            throw new ConsignmentError("Cannot add consignment to cargo <%s>: cargo has already been received (status: %s)."
                    .formatted(trackingId, delivery.getTransportStatus()));
        }

        List<Consignment> updatedConsignments = new ArrayList<>(this.consignments);
        updatedConsignments.add(consignment);

        return newCargo().consignments(updatedConsignments).build();
    }

    /**
     * Removes a consignment from this cargo.
     *
     * @param consignment the consignment to remove
     * @return new {@code Cargo} instance with the consignment removed
     * @throws ConsignmentError if consignment cannot be removed (e.g., cargo already received or consignment not found)
     */
    public Cargo removeConsignment(Consignment consignment) {
        notNull(consignment);

        // Business rule: consignments can only be removed before cargo is received
        if (delivery.getTransportStatus() != TransportStatus.NOT_RECEIVED) {
            throw new ConsignmentError("Cannot remove consignment from cargo <%s>: cargo has already been received (status: %s)."
                    .formatted(trackingId, delivery.getTransportStatus()));
        }

        if (!consignments.contains(consignment)) {
            throw new ConsignmentError("Consignment <%s> not found in cargo <%s>."
                    .formatted(consignment.getConsignmentId(), trackingId));
        }

        List<Consignment> updatedConsignments = new ArrayList<>(this.consignments);
        updatedConsignments.remove(consignment);

        return newCargo().consignments(updatedConsignments).build();
    }

    /**
     * Returns the total quantity of all consignments in containers.
     *
     * @return total quantity in containers
     */
    public int getTotalQuantityInContainers() {
        return consignments.stream()
                .mapToInt(Consignment::getQuantityInContainers)
                .sum();
    }

    /**
     * Checks if this cargo has any consignments.
     *
     * @return true if cargo has consignments, false otherwise
     */
    public boolean hasConsignments() {
        return !consignments.isEmpty();
    }

    /*
        Point of interest:
        -----------------
        This helper is used to create a copy of this instance so
        that we can return a new (modified) copy after each method
        which changes the state of "Cargo". This renders "Cargo"
        entity effectively immutable.
     */
    private CargoBuilder newCargo() {

        return Cargo.builder()
                .trackingId(trackingId)
                .origin(origin)
                .delivery(delivery)
                .routeSpecification(routeSpecification)
                .itinerary(itinerary)
                .consignments(consignments)
                .version(version);
    }

    @Override
    public String toString() {
        return "Cargo{" +
                "trackingId=" + trackingId +
                ", consignments=" + consignments.size() +
                '}';
    }

    public boolean isRouted() {
        return itinerary != null && !itinerary.getLegs().isEmpty();
    }

    /*
        Modeled after "se.citerus.dddsample.domain.model.cargo.Cargo#deriveDeliveryProgress".
     */

    /**
     * Returns a new instance of the cargo with delivery updated to reflect given {@code handlingHistory}.
     *
     * @param handlingHistory handling history (list of handling events) of the cargo
     * @return new {@code Cargo} with updated delivery
     */
    public Cargo updateDeliveryProgress(HandlingHistory handlingHistory) {
        return newCargo()
                .delivery(Delivery.derivedFrom(routeSpecification, itinerary, handlingHistory))
                .build();
    }

}