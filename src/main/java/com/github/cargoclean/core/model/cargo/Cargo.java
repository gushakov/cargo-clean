package com.github.cargoclean.core.model.cargo;

/*
    Notice on COPYRIGHT:
    -------------------

    Some code in this file is based on, copied from, and or modified from
    the code in the original DDDSample application. Please, see the copyright
    notice in "README.md" and the copy of the original licence in
    "original-license.txt", as well.
 */


import com.github.cargoclean.core.model.location.Location;
import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotNull;

/**
 * Modeled after original "se.citerus.dddsample.domain.model.cargo.Cargo".
 */

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Builder
public class Cargo {

    /*
        Cargo's ID can be null for newly created Cargo objects. And should never be
        null for the instances loaded and mapped from the database.
     */
    @EqualsAndHashCode.Include
    Integer id;

    @NotNull
    @EqualsAndHashCode.Include
    TrackingId trackingId;

    @NotNull
    Location origin;

    @NotNull
    Delivery delivery;

    @NotNull
    RouteSpecification routeSpecification;

    Itinerary itinerary;

    public Cargo withNullId() {
        return newCargo().id(null).build();
    }

    public Cargo withOrigin(Location origin) {
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
        CargoBuilder builder = Cargo.builder()
                .id(id)
                .trackingId(trackingId)
                .origin(origin)
                .delivery(delivery)
                .routeSpecification(routeSpecification);
        if (itinerary != null){
            builder.itinerary(itinerary);
        }
        return builder;
    }

    @Override
    public String toString() {
        return "Cargo{" +
                "trackingId=" + trackingId +
                '}';
    }
}
