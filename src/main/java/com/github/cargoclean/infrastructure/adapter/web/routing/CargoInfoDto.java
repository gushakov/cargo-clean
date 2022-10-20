package com.github.cargoclean.infrastructure.adapter.web.routing;

import com.github.cargoclean.core.model.cargo.RouteDto;
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
