package com.github.cargoclean.infrastructure.adapter.security;

import com.github.cargoclean.core.CargoSecurityError;
import com.github.cargoclean.core.model.cargo.Itinerary;
import com.github.cargoclean.core.model.location.Region;
import com.github.cargoclean.core.model.location.UnLocode;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

import static com.github.cargoclean.core.model.location.Region.Oceania;

/**
 * Default security adapter based on Spring Security.
 */
@Service
public final class CargoSecurityAdapter {

    // Roles for the Cargo tracking application users

    public static final String ROLE_CARGO_AGENT = "CARGO_AGENT";
    public static final String ROLE_CARGO_MANAGER = "CARGO_MANGER";

    public void assertAgentRole() {

        if (!hasRole(getAuthentication(), ROLE_CARGO_AGENT)) {
            throw new CargoSecurityError("User does not have role %s"
                    .formatted(ROLE_CARGO_AGENT));
        }

    }

    public void assertManagerRole() {

        if (!hasRole(getAuthentication(), ROLE_CARGO_MANAGER)) {
            throw new CargoSecurityError("User does not have role %s"
                    .formatted(ROLE_CARGO_MANAGER));
        }

    }

    public void assertPermissionToRouteCargo(Itinerary itinerary, Map<UnLocode, Region> regions) {

        // see if the itinerary contains a leg with a location from Oceania

        if (itinerary.getLegs().stream()
                .anyMatch(leg -> regions.get(leg.getLoadLocation()) == Oceania
                        || regions.get(leg.getUnloadLocation()) == Oceania)) {

            // only manager can route Cargo through Oceania
            assertManagerRole();
        }

    }

    private boolean hasRole(Authentication authentication, String role) {
        return authentication.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .map(authority -> authority.replaceFirst("ROLE_", ""))
                .anyMatch(authority -> authority.equals(role.toUpperCase()));
    }

    private Authentication getAuthentication() {
        return Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
                .orElseThrow(() -> new CargoSecurityError("User is not authenticated"));
    }

}
