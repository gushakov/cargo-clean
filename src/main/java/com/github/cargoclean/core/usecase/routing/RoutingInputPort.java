package com.github.cargoclean.core.usecase.routing;

import com.github.cargoclean.core.model.cargo.TrackingId;
import com.github.cargoclean.infrastructure.adapter.web.presenter.routing.CandidateRouteDto;

public interface RoutingInputPort {

    void showCargo(TrackingId trackingId);

    void selectItinerary(TrackingId trackingId);

    void assignRoute(String trackingId, CandidateRouteDto selectedRoute);
}
