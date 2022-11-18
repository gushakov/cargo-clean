package com.github.cargoclean.infrastructure.adapter.security;

import com.github.cargoclean.core.AuthenticationRequiredException;
import com.github.cargoclean.core.InsufficientPrivilegesError;
import com.github.cargoclean.core.model.cargo.Itinerary;
import com.github.cargoclean.core.model.location.Region;
import com.github.cargoclean.core.model.location.UnLocode;
import com.github.cargoclean.core.port.operation.SecurityOutputPort;
import com.github.cargoclean.core.usecase.routing.RegionForbiddenForRoutingError;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
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
public final class CargoSecurityAdapter implements SecurityOutputPort {

    // Roles for the Cargo tracking application users

    public static final String ROLE_CARGO_AGENT = "CARGO_AGENT";
    public static final String ROLE_CARGO_MANAGER = "CARGO_MANGER";

    public static final Region SPECIAL_REGION = Oceania;

    @Override
    public void assertThatUserIsAgent() {

        if (!hasRole(getAuthentication(), ROLE_CARGO_AGENT)) {
            throw new InsufficientPrivilegesError(ROLE_CARGO_AGENT);
        }

    }

    @Override
    public void assertThatUserIsManager() {

        if (!hasRole(getAuthentication(), ROLE_CARGO_MANAGER)) {
            throw new InsufficientPrivilegesError(ROLE_CARGO_MANAGER);
        }

    }

    @Override
    public void assertThatUserHasPermissionToRouteCargoThroughRegions(Itinerary itinerary, Map<UnLocode, Region> regions) {

        // see if the itinerary contains a leg with a location from Oceania

        if (itinerary.getLegs().stream()
                .anyMatch(leg -> regions.get(leg.getLoadLocation()) == SPECIAL_REGION
                        || regions.get(leg.getUnloadLocation()) == SPECIAL_REGION)) {

            // only manager can route Cargo through Oceania
            try {
                assertThatUserIsManager();
            } catch (Exception e) {
                throw new RegionForbiddenForRoutingError();
            }
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
                .filter(authentication -> authentication.isAuthenticated()
                        && !(authentication instanceof AnonymousAuthenticationToken))
                .orElseThrow(AuthenticationRequiredException::new);
    }

}
