package com.github.cargoclean.core.model;

import com.github.cargoclean.core.GenericCargoError;

public class InvalidDomainObjectError extends GenericCargoError {
    public InvalidDomainObjectError(String message) {
        super(message);
    }

    public InvalidDomainObjectError(String message, Throwable cause) {
        super(message, cause);
    }
}
