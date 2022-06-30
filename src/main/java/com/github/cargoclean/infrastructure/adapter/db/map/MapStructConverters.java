package com.github.cargoclean.infrastructure.adapter.db.map;

import com.github.cargoclean.core.model.cargo.TrackingId;
import com.github.cargoclean.core.model.location.Location;
import com.github.cargoclean.core.model.location.UnLocode;
import com.github.cargoclean.infrastructure.adapter.db.LocationDbEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public class MapStructConverters {

    String mapUnLocodeToCode(UnLocode unLocode){
        return unLocode.toString();
    }

    UnLocode mapCodeToUnLocode(String unlocode) {
        return UnLocode.builder()
                .code(unlocode)
                .build();
    }

    String mapTrackingIdToId(TrackingId trackingId){
        return trackingId.toString();
    }

    TrackingId mapIdToTrackingId(String id){
        return TrackingId.builder()
                .id(id)
                .build();
    }

    Integer mapLocationToId(Location location){
        return location.getId();
    }

}
