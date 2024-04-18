package com.github.cargoclean.core.usecase.handling;

import com.github.cargoclean.core.model.InvalidDomainObjectError;
import com.github.cargoclean.core.model.cargo.TrackingId;
import com.github.cargoclean.core.model.handling.HandlingEvent;
import com.github.cargoclean.core.port.ErrorHandlingPresenterOutputPort;

public interface HandlingPresenterOutputPort extends ErrorHandlingPresenterOutputPort {
    void presentResultOfRegisteringHandlingEvent(TrackingId cargoId, HandlingEvent handlingEvent);

    void presentInvalidParametersError(InvalidDomainObjectError e);
}
