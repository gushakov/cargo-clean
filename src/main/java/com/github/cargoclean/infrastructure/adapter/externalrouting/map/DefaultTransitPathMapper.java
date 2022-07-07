package com.github.cargoclean.infrastructure.adapter.externalrouting.map;

import com.github.cargoclean.core.model.cargo.Itinerary;
import com.github.cargoclean.core.model.cargo.Leg;
import com.github.cargoclean.infrastructure.adapter.map.CommonMapStructConverters;
import com.pathfinder.api.TransitEdge;
import com.pathfinder.api.TransitPath;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * MapStruct mapper for mapping between "com.pathfinder" API objects
 * and cargo domain objects.
 */
@Mapper(componentModel = "spring", uses = {CommonMapStructConverters.class})
public abstract class DefaultTransitPathMapper implements TransitPathMapper {

    @Mapping(target = "cargoTrackingId", ignore = true)
    @Mapping(target = "voyageNumber", source = "edge")
    @Mapping(target = "loadLocation", source = "fromNode")
    @Mapping(target = "unloadLocation", source = "toNode")
    @Mapping(target = "loadTime", source = "fromDate")
    @Mapping(target = "unloadTime", source = "toDate")
    abstract Leg map(TransitEdge transitEdge);

    @Mapping(target = "legs", source = "transitEdges")
    abstract Itinerary map(TransitPath transitPath);

}
