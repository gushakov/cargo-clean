package com.github.cargoclean.core.port.presenter.handling;

import com.github.cargoclean.core.model.cargo.TrackingId;
import com.github.cargoclean.core.model.handling.HandlingEvent;
import com.github.cargoclean.core.port.error.ErrorHandlingPresenterOutputPort;
import com.github.cargoclean.core.validator.InvalidDomainObjectError;

public interface HandlingPresenterOutputPort extends ErrorHandlingPresenterOutputPort {
    void presentResultOfRegisteringHandlingEvent(TrackingId cargoId, HandlingEvent handlingEvent);

    void presentInvalidParametersError(InvalidDomainObjectError e);
}
