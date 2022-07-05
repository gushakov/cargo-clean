package com.github.cargoclean.core;

/**
 * Parent class for all domain exceptions.
 */
public class GenericCargoError extends RuntimeException {

    public GenericCargoError(String message) {
        super(message);
    }

    public GenericCargoError(String message, Throwable cause) {
        super(message, cause);
    }
}
