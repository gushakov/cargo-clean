package com.github.cargoclean.infrastructure.adapter.web.presenter.routing;

import com.github.cargoclean.core.usecase.routing.RouteDto;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class CargoInfoDto {

    String trackingId;
    String origin;
    String destination;
    String arrivalDeadline;
    boolean routed;
    RouteDto routeDto;
}
