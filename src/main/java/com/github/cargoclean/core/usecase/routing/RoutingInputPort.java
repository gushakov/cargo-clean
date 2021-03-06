package com.github.cargoclean.core.usecase.routing;

import com.github.cargoclean.core.model.cargo.RouteDto;
import com.github.cargoclean.core.model.cargo.TrackingId;

public interface RoutingInputPort {

    void showCargo(TrackingId trackingId);

    void selectItinerary(TrackingId trackingId);

    void assignRoute(String trackingId, RouteDto selectedRoute);
}
