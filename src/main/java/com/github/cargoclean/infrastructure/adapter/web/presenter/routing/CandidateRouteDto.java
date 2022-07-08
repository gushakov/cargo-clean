package com.github.cargoclean.infrastructure.adapter.web.presenter.routing;

import com.github.cargoclean.core.model.cargo.Leg;
import lombok.Builder;
import lombok.Data;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class CandidateRouteDto {

    List<LegDto> legs;

}
