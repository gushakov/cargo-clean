package com.github.cargoclean.core;

public class GenericCargoError extends RuntimeException {

    public GenericCargoError(String message) {
        super(message);
    }

    public GenericCargoError(String message, Throwable cause) {
        super(message, cause);
    }
}
