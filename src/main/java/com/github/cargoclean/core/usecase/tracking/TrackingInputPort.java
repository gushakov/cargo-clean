package com.github.cargoclean.core.usecase.tracking;

public interface TrackingInputPort {
    void initializeCargoTrackingView();

    void trackCargo(String cargoTrackingId);
}
