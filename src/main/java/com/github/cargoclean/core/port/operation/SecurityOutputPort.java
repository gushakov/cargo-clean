package com.github.cargoclean.core.port.operation;

import com.github.cargoclean.core.model.cargo.Itinerary;
import com.github.cargoclean.core.model.location.Region;
import com.github.cargoclean.core.model.location.UnLocode;

import java.util.Map;

public interface SecurityOutputPort {
    void assertThatUserIsAgent();

    void assertThatUserIsManager();

    void assertThatUserHasPermissionToRouteCargoThroughRegions(Itinerary itinerary, Map<UnLocode, Region> regions);
}
