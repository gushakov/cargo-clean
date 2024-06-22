package com.github.cargoclean.core.usecase.routing;

import com.github.cargoclean.core.port.routing.RouteDto;

public interface RoutingInputPort {

    void showCargo(String cargoTrackingId);

    void selectItinerary(String cargoTrackingId);

    void assignRoute(String trackingId, RouteDto selectedRoute);
}
