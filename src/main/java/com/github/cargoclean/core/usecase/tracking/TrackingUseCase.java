package com.github.cargoclean.core.usecase.tracking;

import com.github.cargoclean.core.port.presenter.tracking.TrackingPresenterOutputPort;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TrackingUseCase implements TrackingInputPort {

    private final TrackingPresenterOutputPort presenter;

}
