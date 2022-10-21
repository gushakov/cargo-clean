package com.github.cargoclean.infrastructure.adapter.web.tracking;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class TrackingInfo {

    String trackingId;
}
