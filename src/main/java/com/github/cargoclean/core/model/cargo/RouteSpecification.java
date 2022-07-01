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
import lombok.Builder;
import lombok.Value;

import java.time.ZonedDateTime;

/**
 * Modeled after original "se.citerus.dddsample.domain.model.cargo.RouteSpecification".
 */
@Value
@Builder
public class RouteSpecification {

    Location origin;
    Location destination;
    ZonedDateTime arrivalDeadline;

    public RouteSpecification withOrigin(Location origin){
        return newRouteSpecification().origin(origin).build();
    }

    public RouteSpecification withDestination(Location destination){
        return newRouteSpecification().destination(destination).build();
    }

    private RouteSpecificationBuilder newRouteSpecification(){
        return RouteSpecification.builder()
                .origin(origin)
                .destination(destination)
                .arrivalDeadline(arrivalDeadline);
    }

}
