package com.github.cargoclean.infrastructure.adapter.web.presenter.routing;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CargoInfoDto {

    private String trackingId;
    private String origin;
    private String destination;
    private String arrivalDeadline;
    private boolean routed;
}
