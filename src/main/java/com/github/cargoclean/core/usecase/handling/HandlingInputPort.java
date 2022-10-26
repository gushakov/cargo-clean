package com.github.cargoclean.core.usecase.handling;

import com.github.cargoclean.core.model.handling.HandlingEventType;

import java.time.Instant;

public interface HandlingInputPort {
    void recordHandlingEvent(String voyageNumberStr, String locationStr, String cargoIdStr, Instant completionTime,
                             Instant registrationTime, HandlingEventType type);
}
