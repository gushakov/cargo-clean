package com.github.cargoclean.core.port.presenter.tracking;

import com.github.cargoclean.core.port.error.ErrorHandlingPresenterOutputPort;

public interface TrackingPresenterOutputPort extends ErrorHandlingPresenterOutputPort {
    void presentInitialViewForCargoTracking();
}
