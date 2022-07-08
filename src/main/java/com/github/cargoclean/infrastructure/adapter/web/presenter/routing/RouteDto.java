package com.github.cargoclean.infrastructure.adapter.web.presenter.routing;

import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class RouteDto {

    List<LegDto> legs;

}
