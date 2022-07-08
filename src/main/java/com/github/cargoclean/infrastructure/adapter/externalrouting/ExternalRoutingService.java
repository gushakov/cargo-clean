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
import com.github.cargoclean.core.model.cargo.TrackingId;
import com.github.cargoclean.core.port.operation.RoutingServiceOutputPort;
import com.github.cargoclean.infrastructure.adapter.externalrouting.map.TransitPathMapper;
import com.pathfinder.api.GraphTraversalService;
import com.pathfinder.api.TransitPath;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Properties;

/**
 * Code were copied and modified from original "se.citerus.dddsample.infrastructure.routing.ExternalRoutingService".
 */
@RequiredArgsConstructor
@Service
public class ExternalRoutingService implements RoutingServiceOutputPort {

    private final GraphTraversalService graphTraversalService;

    private final TransitPathMapper pathMapper;

    @Override
    public List<Itinerary> fetchRoutesForSpecification(TrackingId trackingId, RouteSpecification routeSpecification) {

        // seems like the deadline in the specifications is not really used
        // by the original implementation of GraphTraversalService

        List<TransitPath> transitPaths = graphTraversalService.findShortestPath(routeSpecification.getOrigin().getCode(),
                routeSpecification.getDestination().getCode(),
                new Properties());

        // fetch transit paths, convert them to itineraries and
        // filter only the ones that satisfy route specification

        return pathMapper.convert(transitPaths, trackingId).stream()
                .filter(routeSpecification::isSatisfiedBy)
                .toList();
    }
}
