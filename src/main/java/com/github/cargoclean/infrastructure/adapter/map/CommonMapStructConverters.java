package com.github.cargoclean.infrastructure.adapter.map;

import com.github.cargoclean.core.model.Constants;
import com.github.cargoclean.core.model.cargo.TrackingId;
import com.github.cargoclean.core.model.location.Location;
import com.github.cargoclean.core.model.location.UnLocode;
import org.mapstruct.Mapper;

import java.time.Instant;
import java.time.ZonedDateTime;

/**
 * Common MapStruct converters for model entities, value objects, standard types and primitives.
 * These converters are intended to be used from various mappers used in the different layers
 * of the application.
 */
@Mapper(componentModel = "spring")
public class CommonMapStructConverters {

    public String mapUnLocodeToCode(UnLocode unLocode) {
        return unLocode.toString();
    }

    public UnLocode mapCodeToUnLocode(String unlocode) {
        return UnLocode.builder()
                .code(unlocode)
                .build();
    }

    public String mapTrackingIdToId(TrackingId trackingId) {
        return trackingId.toString();
    }

    public TrackingId mapIdToTrackingId(String id) {
        return TrackingId.builder()
                .id(id)
                .build();
    }

    public Integer mapLocationToId(Location location) {
        return location.getId();
    }

    public Instant convertZonedDateTimeToInstant(ZonedDateTime dateTime) {
        return dateTime.toInstant();
    }

    public ZonedDateTime convertInstantToZonedDateTime(Instant instant) {
        return ZonedDateTime.ofInstant(instant, Constants.DEFAULT_ZONE_ID);
    }

}
