package com.github.cargoclean.core.usecase.routing;

import com.github.cargoclean.core.model.cargo.TrackingId;

public interface RoutingInputPort {

    void showCargo(TrackingId trackingId);

    void selectItinerary(TrackingId trackingId);

}
