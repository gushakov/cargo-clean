package com.github.cargoclean.core.port.operation;

import com.github.cargoclean.core.InsufficientPrivilegesError;
import com.github.cargoclean.core.model.cargo.Itinerary;
import com.github.cargoclean.core.model.cargo.Leg;
import com.github.cargoclean.core.model.location.Region;
import com.github.cargoclean.core.model.location.UnLocode;
import com.github.cargoclean.core.usecase.routing.RegionForbiddenForRoutingError;

import java.util.Map;

import static com.github.cargoclean.core.model.location.Region.Europe;

public interface SecurityOutputPort {

    /*
        Point of interest:
        -----------------
        Most of the security should be implemented in default
        methods in "SecurityOutputPort" interface.
     */

    // Roles for the Cargo tracking application users
    String ROLE_CARGO_AGENT = "CARGO_AGENT";
    String ROLE_CARGO_MANAGER = "CARGO_MANGER";

    // Special region used for routing permission check
    Region SPECIAL_REGION = Europe;

    boolean hasRole(String role);

    default boolean doesNotHaveRole(String role) {
        return !hasRole(role);
    }

    default boolean userIsNotManager() {
        return doesNotHaveRole(ROLE_CARGO_MANAGER);
    }

    default void assertThatUserIsAgent() {
        if (doesNotHaveRole(ROLE_CARGO_AGENT)) {
            throw new InsufficientPrivilegesError(ROLE_CARGO_AGENT);
        }
    }

    default void assertThatUserIsManager() {
        if (doesNotHaveRole(ROLE_CARGO_MANAGER)) {
            throw new InsufficientPrivilegesError(ROLE_CARGO_MANAGER);
        }
    }

    default void assertThatUserHasPermissionToRouteCargoThroughRegions(Itinerary itinerary, Map<UnLocode, Region> regions) {
        // see if the itinerary contains an intermediate leg with a location from
        // the special region

        if (itinerary.intermediate().stream()
                .anyMatch(leg -> goesThroughSpecialRegion(leg, regions))) {

            // only manager can route Cargo through special region
            if (userIsNotManager()) {
                throw new RegionForbiddenForRoutingError();
            }
        }

    }

    private boolean goesThroughSpecialRegion(Leg leg, Map<UnLocode, Region> regions) {
        return regions.get(leg.getLoadLocation()) == SPECIAL_REGION
                || regions.get(leg.getUnloadLocation()) == SPECIAL_REGION;
    }
}
