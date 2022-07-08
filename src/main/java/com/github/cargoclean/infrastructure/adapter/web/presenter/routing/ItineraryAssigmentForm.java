package com.github.cargoclean.infrastructure.adapter.web.presenter.routing;

import com.github.cargoclean.core.model.cargo.TrackingId;
import com.github.cargoclean.core.model.location.UnLocode;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Builder
public class ItineraryAssigmentForm {

    @Getter
    private TrackingId trackingId;

    @Getter
    private UnLocode cargoOrigin;

    @Getter
    private UnLocode cargoDestination;

    @Getter
    private List<CandidateRouteDto> candidateRoutes;

    @Getter
    @Setter
    private CandidateRouteDto selectedRoute;
}
