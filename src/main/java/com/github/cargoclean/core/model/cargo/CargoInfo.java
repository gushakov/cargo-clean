package com.github.cargoclean.core.model.cargo;

import lombok.Value;

@Value
public class CargoInfo {

    String trackingId;
    String origin;
    String destination;
    boolean routed;
}
