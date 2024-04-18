package com.github.cargoclean.core.port.security;


import com.github.cargoclean.core.GenericCargoError;
import lombok.Getter;

/**
 * Error signaling that some security assertion failed: user may not be authenticated,
 * she may not have an appropriate role or a permission.
 */
public abstract class CargoSecurityError extends GenericCargoError {
    @Getter
    private final boolean userAuthenticated;

    /**
     * Creates security error with provided {@code message}. Sets the
     * authentication status of the user.
     *
     * @param message             error message
     * @param isUserAuthenticated {@code true} is user has been successfully authenticated, {@code false} otherwise
     */
    public CargoSecurityError(String message, boolean isUserAuthenticated) {
        super(message);
        this.userAuthenticated = isUserAuthenticated;
    }
}
