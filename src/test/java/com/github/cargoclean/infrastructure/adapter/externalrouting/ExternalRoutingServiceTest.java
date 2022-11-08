package com.github.cargoclean.infrastructure.adapter.externalrouting;
/*
    Notice on COPYRIGHT:
    -------------------

    Some code in this file is based on, copied from, and or modified from
    the code in the original DDDSample application. Please, see the copyright
    notice in "README.md" and the copy of the original licence in
    "original-license.txt", as well.
 */


import com.github.cargoclean.core.model.MockModels;
import com.github.cargoclean.core.model.UtcDateTime;
import com.github.cargoclean.core.model.cargo.Itinerary;
import com.github.cargoclean.core.model.cargo.Leg;
import com.github.cargoclean.core.model.cargo.RouteSpecification;
import com.github.cargoclean.core.model.cargo.TrackingId;
import com.github.cargoclean.core.model.location.Location;
import com.github.cargoclean.core.model.location.UnLocode;
import com.github.cargoclean.infrastructure.adapter.externalrouting.map.DefaultTransitPathMapperImpl;
import com.github.cargoclean.infrastructure.adapter.externalrouting.map.TransitPathMapper;
import com.github.cargoclean.infrastructure.adapter.map.CommonMapStructConverters;
import com.pathfinder.api.GraphTraversalService;
import com.pathfinder.internal.GraphDAOStub;
import com.pathfinder.internal.GraphTraversalServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Code copied and modified from original "se.citerus.dddsample.infrastructure.routing.ExternalRoutingServiceTest".
 */
@SpringJUnitConfig(classes = {DefaultTransitPathMapperImpl.class, CommonMapStructConverters.class})
public class ExternalRoutingServiceTest {

    @Autowired
    private TransitPathMapper pathMapper;

    @Test
    void should_find_itineries_using_external_graph_traversal_service() {

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

        ExternalRoutingService externalRoutingService = new ExternalRoutingService(graphTraversalService, pathMapper);

        UtcDateTime arrivalDeadline = UtcDateTime.now().plusDays(90);
        RouteSpecification routeSpecification = RouteSpecification.builder()
                .origin(UnLocode.of("JNTKO"))
                .destination(UnLocode.of("USDAL"))
                .arrivalDeadline(arrivalDeadline)
                .build();
        List<Itinerary> itineraries = externalRoutingService.fetchRoutesForSpecification(TrackingId.of("ABCDEF12"), routeSpecification);

        assertThat(itineraries.stream().allMatch(routeSpecification::isSatisfiedBy)).isTrue();

        // verify that the right tracking ID was assigned to each leg of each itinerary
        assertThat(itineraries.stream()
                .flatMap(itinerary -> itinerary.getLegs().stream())
                .map(Leg::getCargoTrackingId)
                .map(TrackingId::getId)
                .collect(Collectors.toSet()))
                .containsExactly("ABCDEF12");

    }
}
