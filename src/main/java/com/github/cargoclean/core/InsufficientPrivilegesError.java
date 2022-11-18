package com.github.cargoclean.core;

import lombok.Getter;

/**
 * Error thrown when user does not have a required role.
 */
public class InsufficientPrivilegesError extends CargoSecurityError {

    @Getter
    private final String missingRole;

    /**
     * Constructs security error with the specified missing role. Assumes that user is authenticated.
     *
     * @param missingRole role which is absent from the user's granted authorities
     */
    public InsufficientPrivilegesError(String missingRole) {
        super("Access denied: user does not have a required role: %s"
                .formatted(missingRole), true);
        this.missingRole = missingRole;
    }
}
