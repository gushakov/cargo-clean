package com.github.cargoclean.infrastructure.adapter.externalrouting.map;

import com.github.cargoclean.core.model.cargo.Itinerary;
import com.github.cargoclean.core.model.cargo.TrackingId;
import com.pathfinder.api.TransitPath;

import java.util.List;

public interface TransitPathMapper {

    default List<Itinerary> convert(List<TransitPath> transitPaths, TrackingId trackingId) {
        return transitPaths.stream()
                .map(transitPath -> convert(transitPath, trackingId))
                .toList();
    }

    Itinerary convert(TransitPath transitPath, TrackingId trackingId);
}
