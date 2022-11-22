package com.github.cargoclean.infrastructure.adapter.security;

import com.github.cargoclean.core.port.operation.security.AuthenticationRequiredError;
import com.github.cargoclean.core.port.operation.security.SecurityOutputPort;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Default security adapter based on Spring Security.
 */
@Service
public final class CargoSecurityAdapter implements SecurityOutputPort {

    @Override
    public boolean hasRole(String role) {
        return getAuthentication().getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .map(authority -> authority.replaceFirst("ROLE_", ""))
                .anyMatch(authority -> authority.equals(role.toUpperCase()));
    }

    @Override
    public Optional<String> username() {
        try {
            return Optional.of(((UserDetails)getAuthentication().getPrincipal()).getUsername());
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    private Authentication getAuthentication() {
        return Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
                .filter(authentication -> authentication.isAuthenticated()
                        && !(authentication instanceof AnonymousAuthenticationToken))
                .orElseThrow(AuthenticationRequiredError::new);
    }

}
