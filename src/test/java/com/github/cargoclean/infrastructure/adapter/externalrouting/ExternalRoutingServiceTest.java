package com.github.cargoclean.infrastructure.adapter.externalrouting;
/*
    Notice on COPYRIGHT:
    -------------------

    Some code in this file is based on, copied from, and or modified from
    the code in the original DDDSample application. Please, see the copyright
    notice in "README.md" and the copy of the original licence in
    "original-license.txt", as well.
 */


import com.github.cargoclean.core.model.Constants;
import com.github.cargoclean.core.model.MockModels;
import com.github.cargoclean.core.model.cargo.Itinerary;
import com.github.cargoclean.core.model.cargo.RouteSpecification;
import com.github.cargoclean.core.model.location.Location;
import com.github.cargoclean.core.model.location.UnLocode;
import com.pathfinder.api.GraphTraversalService;
import com.pathfinder.internal.GraphDAOStub;
import com.pathfinder.internal.GraphTraversalServiceImpl;
import org.junit.jupiter.api.Test;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Code copied and modified from original "se.citerus.dddsample.infrastructure.routing.ExternalRoutingServiceTest".
 */
public class ExternalRoutingServiceTest {

    @Test
    void should_find_shortest_path_using_external_graph_traversal_service() {

        GraphTraversalService graphTraversalService = new GraphTraversalServiceImpl(new GraphDAOStub() {
            @Override
            public List<String> listAllNodes() {

                // list of nodes needs to be modifiable

                return new ArrayList<>(MockModels.allLocations().values().stream()
                        .map(Location::getUnlocode)
                        .map(UnLocode::getCode)
                        .toList());
            }
        });

        ExternalRoutingService externalRoutingService = new ExternalRoutingService(graphTraversalService);

        List<Itinerary> itineraries = externalRoutingService.fetchRoutesForSpecification(RouteSpecification.builder()
                .origin(UnLocode.of("JNTKO"))
                .destination(UnLocode.of("USDAL"))
                .arrivalDeadline(MockModels.now().plusDays(25))
                .build());

        itineraries.forEach(System.out::println);

    }
}
