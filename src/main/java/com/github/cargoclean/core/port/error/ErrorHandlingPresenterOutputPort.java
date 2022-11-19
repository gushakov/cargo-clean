package com.github.cargoclean.core.port.error;

import com.github.cargoclean.core.port.operation.security.CargoSecurityError;

public interface ErrorHandlingPresenterOutputPort {
    void presentError(Exception e);

    void presentSecurityError(CargoSecurityError e);
}
