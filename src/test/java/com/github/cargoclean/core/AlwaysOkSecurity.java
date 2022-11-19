package com.github.cargoclean.core;

import com.github.cargoclean.core.port.operation.security.SecurityOutputPort;

/**
 * Security profile for testing which will assume user has all roles.
 */
public class AlwaysOkSecurity implements SecurityOutputPort {

    @Override
    public boolean hasRole(String role) {
        return true;
    }
}
