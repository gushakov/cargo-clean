package com.github.cargoclean.core.port.cache;

import com.github.cargoclean.core.GenericCargoError;

public class CacheOperationError extends GenericCargoError {

    public CacheOperationError(String message) {
        super(message);
    }

    public CacheOperationError(String message, Throwable cause) {
        super(message, cause);
    }
}
