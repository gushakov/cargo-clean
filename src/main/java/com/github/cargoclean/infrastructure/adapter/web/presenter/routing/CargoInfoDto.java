package com.github.cargoclean.infrastructure.adapter.web.presenter.routing;

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
