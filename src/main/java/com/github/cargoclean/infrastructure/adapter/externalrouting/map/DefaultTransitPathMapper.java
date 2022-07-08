package com.github.cargoclean.infrastructure.adapter.externalrouting.map;

import com.github.cargoclean.core.model.cargo.Itinerary;
import com.github.cargoclean.core.model.cargo.Leg;
import com.github.cargoclean.core.model.cargo.TrackingId;
import com.github.cargoclean.infrastructure.adapter.map.CommonMapStructConverters;
import com.github.cargoclean.infrastructure.adapter.map.IgnoreForMapping;
import com.pathfinder.api.TransitEdge;
import com.pathfinder.api.TransitPath;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * MapStruct mapper for mapping between "com.pathfinder" API objects
 * and cargo domain objects.
 */
@Mapper(componentModel = "spring", uses = {CommonMapStructConverters.class})
public abstract class DefaultTransitPathMapper implements TransitPathMapper {

    /*
        We use MapStruct's "Context" annotation to specify "TrackingId"
        parameter as the mapping context parameter which will be passed
        through the mapping call-chain for us.
     */

    @Mapping(target = "cargoTrackingId", expression = "java(trackingId)")
    @Mapping(target = "voyageNumber", source = "edge")
    @Mapping(target = "loadLocation", source = "fromNode")
    @Mapping(target = "unloadLocation", source = "toNode")
    @Mapping(target = "loadTime", source = "fromDate")
    @Mapping(target = "unloadTime", source = "toDate")
    abstract Leg map(TransitEdge transitEdge, @Context TrackingId trackingId);

    @Mapping(target = "legs", source = "transitEdges")
    abstract Itinerary map(TransitPath transitPath, @Context TrackingId trackingId);

    @IgnoreForMapping
    @Override
    public Itinerary convert(TransitPath transitPath, TrackingId trackingId) {
        return map(transitPath, trackingId);
    }
}
