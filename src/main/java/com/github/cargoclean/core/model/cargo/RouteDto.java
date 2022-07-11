package com.github.cargoclean.core.model.cargo;

import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class RouteDto {

    List<LegDto> legs;

}
