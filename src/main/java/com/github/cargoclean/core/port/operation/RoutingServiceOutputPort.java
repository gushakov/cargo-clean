package com.github.cargoclean.core.port.operation;
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

import java.util.List;

/**
 * Modeled after original "se.citerus.dddsample.domain.service.RoutingService".
 */
public interface RoutingServiceOutputPort {

    List<Itinerary> fetchRoutesForSpecification(RouteSpecification routeSpecification);

}
