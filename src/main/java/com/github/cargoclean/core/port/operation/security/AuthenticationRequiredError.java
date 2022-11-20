package com.github.cargoclean.core.port.operation.security;

/**
 * Error thrown if authentication is required but the user has not yet been authenticated.
 */
public class AuthenticationRequiredError extends CargoSecurityError {
    /**
     * Assumes that user has not been authenticated yet.
     */
    public AuthenticationRequiredError() {
        super("Access denied: user has not been authenticated", false);
    }
}
