package com.github.cargoclean.core;

/**
 * Error thrown if authentication is required but the user has not yet been authenticated.
 */
public class AuthenticationRequiredException extends CargoSecurityError {
    /**
     * Assumes that user has not been authenticated yet.
     */
    public AuthenticationRequiredException() {
        super("Access denied: user has not been authenticated", false);
    }
}
