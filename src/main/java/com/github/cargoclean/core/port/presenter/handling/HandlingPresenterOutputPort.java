package com.github.cargoclean.core.port.presenter.handling;

import com.github.cargoclean.core.model.InvalidDomainObjectError;
import com.github.cargoclean.core.model.cargo.TrackingId;
import com.github.cargoclean.core.model.handling.HandlingEvent;
import com.github.cargoclean.core.port.error.ErrorHandlingPresenterOutputPort;

public interface HandlingPresenterOutputPort extends ErrorHandlingPresenterOutputPort {
    void presentResultOfRegisteringHandlingEvent(TrackingId cargoId, HandlingEvent handlingEvent);

    void presentInvalidParametersError(InvalidDomainObjectError e);
}
