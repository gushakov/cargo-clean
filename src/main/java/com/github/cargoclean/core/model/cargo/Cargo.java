package com.github.cargoclean.core.model.cargo;

/*
    Notice on COPYRIGHT:
    -------------------

    Some code in this file is based on, copied from, and or modified from
    the code in the original DDDSample application. Please, see the copyright
    notice in "README.md" and the copy of the original licence in
    "original-license.txt", as well.
 */


import com.github.cargoclean.core.model.location.UnLocode;
import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotNull;

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
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Builder
public class Cargo {

    @NotNull
    @EqualsAndHashCode.Include
    TrackingId trackingId;

    @NotNull
    UnLocode origin;

    @NotNull
    Delivery delivery;

    @NotNull
    RouteSpecification routeSpecification;

    // itinerary is null before cargo is routed
    Itinerary itinerary;

    /**
     * Should not be modified. Maps to a {@literal @Version} field in {@code CargoDbEntity}
     * which will be used by Spring Data JDBC to determine the state (new, existing) of
     * the persistent entity.
     */
    Integer version;

    public boolean exists() {
        return version != null;
    }

    public Cargo withOrigin(UnLocode origin) {
        return newCargo().origin(origin).build();
    }

    public Cargo withRouteSpecification(RouteSpecification routeSpec) {
        return newCargo().routeSpecification(routeSpec).build();
    }

    public Cargo withDelivery(Delivery delivery) {
        return newCargo().delivery(delivery).build();
    }

    public Cargo withItinerary(Itinerary itinerary) {
        return newCargo().itinerary(itinerary).build();
    }

    private CargoBuilder newCargo() {

        return Cargo.builder()
                .trackingId(trackingId)
                .origin(origin)
                .delivery(delivery)
                .routeSpecification(routeSpecification)
                .itinerary(itinerary)
                .version(version);
    }

    @Override
    public String toString() {
        return "Cargo{" +
                "trackingId=" + trackingId +
                '}';
    }

    public boolean isRouted() {
        return itinerary != null && !itinerary.getLegs().isEmpty();
    }
}
