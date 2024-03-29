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
import com.github.cargoclean.core.model.InvalidDomainObjectError;
import com.github.cargoclean.core.model.UtcDateTime;
import com.github.cargoclean.core.model.location.UnLocode;
import lombok.Builder;
import lombok.Value;

import static com.github.cargoclean.core.model.Assert.notNull;

/**
 * Code copied and modified from original "se.citerus.dddsample.domain.model.cargo.RouteSpecification".
 */
@Value
public class RouteSpecification implements Specification<Itinerary> {

    UnLocode origin;

    UnLocode destination;

    UtcDateTime arrivalDeadline;

    @Builder
    public RouteSpecification(UnLocode origin, UnLocode destination, UtcDateTime arrivalDeadline) {
        this.origin = notNull(origin);
        this.destination = notNull(destination);
        this.arrivalDeadline = notNull(arrivalDeadline);

        /*
            Another invariant assertion: route specification cannot
            have the same origin and destination.
         */
        if (origin.equals(destination)) {
            throw new InvalidDomainObjectError(("Invalid route specification, the origin (%s) " +
                    "must not be the same as the destination (%s)")
                    .formatted(origin, destination));
        }
    }

    /*
        Copied from "se.citerus.dddsample.domain.model.cargo.RouteSpecification#isSatisfiedBy".
     */
    @Override
    public boolean isSatisfiedBy(Itinerary itinerary) {
        return origin.equals(itinerary.first().getLoadLocation())
                && destination.equals(itinerary.last().getUnloadLocation())
                && arrivalDeadline.isAfter(itinerary.last().getUnloadTime());
    }
}
