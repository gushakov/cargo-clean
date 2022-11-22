package com.github.cargoclean.core.port.operation.routing;
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

import java.util.List;

/**
 * Modeled after original "se.citerus.dddsample.domain.service.RoutingService".
 */
public interface RoutingServiceOutputPort {

    /*
        This is slightly different from the original. We are passing tracking ID
        for the cargo. This will allow us to set this tracking ID to each leg
        of the (candidate) itinerary when converting the transit edges to Legs
        instances.
     */

    List<Itinerary> fetchRoutesForSpecification(TrackingId trackingId, RouteSpecification routeSpecification);

}
