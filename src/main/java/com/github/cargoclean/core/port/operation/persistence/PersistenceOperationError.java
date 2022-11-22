package com.github.cargoclean.core.port.operation.persistence;

import com.github.cargoclean.core.GenericCargoError;

public class PersistenceOperationError extends GenericCargoError {

    public PersistenceOperationError(String message) {
        super(message);
    }

    public PersistenceOperationError(String message, Throwable cause) {
        super(message, cause);
    }
}
