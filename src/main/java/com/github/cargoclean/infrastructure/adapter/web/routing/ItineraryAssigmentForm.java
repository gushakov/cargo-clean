package com.github.cargoclean.infrastructure.adapter.web.routing;

import com.github.cargoclean.core.model.cargo.RouteDto;
import com.github.cargoclean.core.model.cargo.TrackingId;
import com.github.cargoclean.core.model.location.UnLocode;
import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class ItineraryAssigmentForm {

    TrackingId trackingId;

    UnLocode cargoOrigin;

    UnLocode cargoDestination;

    List<RouteDto> candidateRoutes;

}
