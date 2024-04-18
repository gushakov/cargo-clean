package com.github.cargoclean.core;

import com.github.cargoclean.core.port.security.SecurityOutputPort;

import java.util.Optional;

/**
 * Security profile for testing which will assume user has all roles.
 */
public class AlwaysOkSecurity implements SecurityOutputPort {

    @Override
    public boolean hasRole(String role) {
        return true;
    }

    @Override
    public Optional<String> username() {
        return Optional.of("test-user");
    }
}
