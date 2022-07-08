package com.github.cargoclean.infrastructure.adapter.web.presenter.routing;

import com.github.cargoclean.core.GenericCargoError;

public class RoutingError extends GenericCargoError {
    public RoutingError(String message) {
        super(message);
    }

    public RoutingError(String message, Throwable cause) {
        super(message, cause);
    }
}
