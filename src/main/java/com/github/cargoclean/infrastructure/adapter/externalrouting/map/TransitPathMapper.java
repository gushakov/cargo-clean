package com.github.cargoclean.infrastructure.adapter.externalrouting.map;

import com.github.cargoclean.core.model.cargo.Itinerary;
import com.pathfinder.api.TransitPath;

import java.util.List;

public interface TransitPathMapper {

    default List<Itinerary> convert(List<TransitPath> transitPaths) {
        return transitPaths.stream().map(this::convert).toList();
    }

    Itinerary convert(TransitPath transitPath);
}
