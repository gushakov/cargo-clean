package com.github.cargoclean.infrastructure.adapter.db.map;

import com.github.cargoclean.core.model.Constants;
import com.github.cargoclean.core.model.cargo.TrackingId;
import com.github.cargoclean.core.model.location.Location;
import com.github.cargoclean.core.model.location.UnLocode;
import org.mapstruct.Mapper;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

/**
 * MapStruct converters for common value objects and primitives. These are package-private
 * since they will only be accessed from the generated mapper implementation.
 */
@Mapper(componentModel = "spring")
public class MapStructConverters {

    String mapUnLocodeToCode(UnLocode unLocode) {
        return unLocode.toString();
    }

    UnLocode mapCodeToUnLocode(String unlocode) {
        return UnLocode.builder()
                .code(unlocode)
                .build();
    }

    String mapTrackingIdToId(TrackingId trackingId) {
        return trackingId.toString();
    }

    TrackingId mapIdToTrackingId(String id) {
        return TrackingId.builder()
                .id(id)
                .build();
    }

    Integer mapLocationToId(Location location) {
        return location.getId();
    }

    Instant convertZonedDateTimeToInstant(ZonedDateTime dateTime){
        return dateTime.toInstant();
    }

    ZonedDateTime convertInstantToZonedDateTime(Instant instant){
        return ZonedDateTime.ofInstant(instant, Constants.DEFAULT_ZONE_ID);
    }

}
