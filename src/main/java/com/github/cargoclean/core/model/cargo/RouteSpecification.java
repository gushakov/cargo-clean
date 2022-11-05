package com.github.cargoclean.core.model.cargo;

/*
    Notice on COPYRIGHT:
    -------------------

    Some code in this file is based on, copied from, and or modified from
    the code in the original DDDSample application. Please, see the copyright
    notice in "README.md" and the copy of the original licence in
    "original-license.txt", as well.
 */


import com.github.cargoclean.core.Specification;
import com.github.cargoclean.core.model.UtcDateTime;
import com.github.cargoclean.core.model.location.UnLocode;
import lombok.Builder;
import lombok.Value;

import javax.validation.constraints.NotNull;

/**
 * Code copied and modified from original "se.citerus.dddsample.domain.model.cargo.RouteSpecification".
 */
@Value
@Builder
public class RouteSpecification implements Specification<Itinerary> {

    @NotNull
    UnLocode origin;

    @NotNull
    UnLocode destination;

    @NotNull
    UtcDateTime arrivalDeadline;

    public RouteSpecification withOrigin(UnLocode origin) {
        return newRouteSpecification().origin(origin).build();
    }

    public RouteSpecification withDestination(UnLocode destination) {
        return newRouteSpecification().destination(destination).build();
    }

    private RouteSpecificationBuilder newRouteSpecification() {
        return RouteSpecification.builder()
                .origin(origin)
                .destination(destination)
                .arrivalDeadline(arrivalDeadline);
    }

    @Override
    public boolean isSatisfiedBy(Itinerary itinerary) {
        return origin.equals(itinerary.first().getLoadLocation())
                && destination.equals(itinerary.last().getUnloadLocation())
                && arrivalDeadline.isAfter(itinerary.last().getUnloadTime());
    }
}
