package com.github.cargoclean.core.model.cargo;

import com.github.cargoclean.core.GenericCargoError;

public class ConsignmentError extends GenericCargoError {

    public ConsignmentError(String message) {
        super(message);
    }

    public ConsignmentError(String message, Throwable cause) {
        super(message, cause);
    }
}