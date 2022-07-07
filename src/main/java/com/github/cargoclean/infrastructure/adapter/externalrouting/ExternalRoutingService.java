package com.github.cargoclean.infrastructure.adapter.externalrouting;

/*
    Notice on COPYRIGHT:
    -------------------

    Some code in this file is based on, copied from, and or modified from
    the code in the original DDDSample application. Please, see the copyright
    notice in "README.md" and the copy of the original licence in
    "original-license.txt", as well.
 */

import com.github.cargoclean.core.model.cargo.Itinerary;
import com.github.cargoclean.core.model.cargo.RouteSpecification;
import com.github.cargoclean.core.port.operation.RoutingServiceOutputPort;
import com.pathfinder.api.GraphTraversalService;
import com.pathfinder.api.TransitPath;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Properties;

/**
 * Code were copied and modified from original "se.citerus.dddsample.infrastructure.routing.ExternalRoutingService".
 */
@RequiredArgsConstructor
public class ExternalRoutingService implements RoutingServiceOutputPort {

    private final GraphTraversalService graphTraversalService;

    @Override
    public List<Itinerary> fetchRoutesForSpecification(RouteSpecification routeSpecification) {

        // seems like the deadline in the specifications is not really used
        // by the current implementation of GraphTraversalService

        List<TransitPath> transitPaths = graphTraversalService.findShortestPath(routeSpecification.getOrigin().getCode(),
                routeSpecification.getDestination().getCode(),
                new Properties());

        transitPaths.forEach(System.out::println);


        return null;
    }
}
