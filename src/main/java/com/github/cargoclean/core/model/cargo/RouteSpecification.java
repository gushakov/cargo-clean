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
import lombok.Builder;
import lombok.Value;

import javax.validation.constraints.NotNull;
import java.time.ZonedDateTime;

/**
 * Modeled after original "se.citerus.dddsample.domain.model.cargo.RouteSpecification".
 */
@Value
@Builder
public class RouteSpecification {

    @NotNull
    UnLocode origin;

    @NotNull
    UnLocode destination;

    @NotNull
    ZonedDateTime arrivalDeadline;

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

}
