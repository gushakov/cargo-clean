package com.github.cargoclean.core.port.presenter.handling;

import com.github.cargoclean.core.model.handling.HandlingEvent;
import com.github.cargoclean.core.port.error.ErrorHandlingPresenterOutputPort;

public interface HandlingPresenterOutputPort extends ErrorHandlingPresenterOutputPort {
    void presentResultOfRegisteringHandlingEvent(HandlingEvent handlingEvent);
}
