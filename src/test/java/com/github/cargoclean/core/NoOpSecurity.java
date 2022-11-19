package com.github.cargoclean.core;

import com.github.cargoclean.core.model.cargo.Itinerary;
import com.github.cargoclean.core.model.location.Region;
import com.github.cargoclean.core.model.location.UnLocode;
import com.github.cargoclean.core.port.operation.SecurityOutputPort;

import java.util.Map;

/**
 * No-op security which does not throw any exceptions for any checks.
 */
public class NoOpSecurity implements SecurityOutputPort {
    @Override
    public void assertThatUserIsAgent() {
        // no-op
    }

    @Override
    public void assertThatUserIsManager() {
        // no-op
    }

    @Override
    public void assertThatUserHasPermissionToRouteCargoThroughRegions(Itinerary itinerary, Map<UnLocode, Region> regions) {
        // no-op
    }
}
