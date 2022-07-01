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

/**
 * Modeled after original "se.citerus.dddsample.domain.model.cargo.Cargo".
 */

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Builder
public class Cargo {

    @EqualsAndHashCode.Include(rank = 1)
    Integer id;
    @EqualsAndHashCode.Include(rank = 2)
    TrackingId trackingId;
    Location origin;
    Delivery delivery;

    RouteSpecification routeSpecification;

    public Cargo withNullId() {
        return newCargo().id(null).build();
    }

    public Cargo withOrigin(Location origin) {
        return newCargo().origin(origin).build();
    }

    public Cargo withRouteSpecification(RouteSpecification routeSpec) {
        return newCargo().routeSpecification(routeSpec).build();
    }

    public Cargo withDelivery(Delivery delivery){
        return newCargo().delivery(delivery).build();
    }

    private CargoBuilder newCargo() {
        return builder()
                .id(id)
                .trackingId(trackingId)
                .origin(origin)
                .delivery(delivery)
                .routeSpecification(routeSpecification);
    }

    @Override
    public String toString() {
        return "Cargo{" +
                "trackingId=" + trackingId +
                '}';
    }
}
