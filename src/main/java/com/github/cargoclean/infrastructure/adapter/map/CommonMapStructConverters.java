package com.github.cargoclean.infrastructure.adapter.map;

import com.github.cargoclean.core.model.Constants;
import com.github.cargoclean.core.model.cargo.TrackingId;
import com.github.cargoclean.core.model.handling.EventId;
import com.github.cargoclean.core.model.location.UnLocode;
import com.github.cargoclean.core.model.voyage.VoyageNumber;
import org.mapstruct.Mapper;

import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.Date;

/**
 * Common MapStruct converters for model entities, value objects, standard types and primitives.
 * These converters are intended to be used from various mappers used in the different layers
 * of the application.
 */
@Mapper(componentModel = "spring")
public class CommonMapStructConverters {

    public String mapUnLocodeToCode(UnLocode unLocode) {
        if (unLocode == null) {
            return null;
        }
        return unLocode.toString();
    }

    public UnLocode mapCodeToUnLocode(String unlocode) {
        if (unlocode == null) {
            return null;
        }
        return UnLocode.builder()
                .code(unlocode)
                .build();
    }

    public String mapTrackingIdToId(TrackingId trackingId) {
        if (trackingId == null) {
            return null;
        }
        return trackingId.toString();
    }

    public TrackingId mapIdToTrackingId(String id) {
        if (id == null) {
            return null;
        }
        return TrackingId.builder()
                .id(id)
                .build();
    }

    public String mapVoyageNumberToNumber(VoyageNumber voyageNumber) {
        if (voyageNumber == null) {
            return null;
        }
        return voyageNumber.getNumber();
    }

    public VoyageNumber mapNumberToVoyageNumber(String number) {
        if (number == null) {
            return null;
        }
        return VoyageNumber.of(number);
    }

    public Instant convertZonedDateTimeToInstant(ZonedDateTime dateTime) {
        if (dateTime == null) {
            return null;
        }
        return dateTime.toInstant();
    }

    public ZonedDateTime convertInstantToZonedDateTime(Instant instant) {
        if (instant == null) {
            return null;
        }
        return ZonedDateTime.ofInstant(instant, Constants.DEFAULT_ZONE_ID);
    }

    public ZonedDateTime convertDateToZonedDateTime(Date date) {
        if (date == null) {
            return null;
        }
        return convertInstantToZonedDateTime(date.toInstant());
    }

    public EventId convertLongToEventId(Long id) {
        if (id == null) {
            return null;
        }
        return EventId.of(id);
    }

    public Long convertEventIdToLong(EventId eventId) {
        if (eventId == null) {
            return null;
        }
        return eventId.getId();
    }
}
