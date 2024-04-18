package com.github.cargoclean.core.port.security;

/**
 * Error thrown if the authenticated user does not have a permission to route cargo
 * through some forbidden region.
 */
public class RegionForbiddenForRoutingError extends CargoSecurityError {

    /**
     * Assumes that user has already been authenticated.
     */
    public RegionForbiddenForRoutingError() {
        super("Forbidden: user does not have permission to route through this itinerary", true);
    }
}
