package com.github.cargoclean.core.port.presenter.routing;

import com.github.cargoclean.core.model.cargo.Cargo;
import com.github.cargoclean.core.port.error.ErrorHandlingPresenterOutputPort;

public interface RoutingPresenterOutputPort extends ErrorHandlingPresenterOutputPort {
    void presentCargoForRouting(Cargo cargo);
}
