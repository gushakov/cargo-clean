package com.github.cargoclean.infrastructure.adapter.map;

import com.github.cargoclean.core.model.UtcDateTime;
import com.github.cargoclean.core.model.cargo.TrackingId;
import com.github.cargoclean.core.model.consignment.ConsignmentId;
import com.github.cargoclean.core.model.handling.EventId;
import com.github.cargoclean.core.model.location.Region;
import com.github.cargoclean.core.model.location.UnLocode;
import com.github.cargoclean.core.model.voyage.VoyageNumber;
import org.mapstruct.Mapper;

import java.time.Instant;

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

    public Instant convertUtcDateTimeToInstant(UtcDateTime utcDateTime) {
        if (utcDateTime == null) {
            return null;
        }
        return utcDateTime.toInstant();
    }

    public UtcDateTime convertInstantToUtcDateTime(Instant instant) {
        if (instant == null) {
            return null;
        }
        return new UtcDateTime(instant);
    }

    public String convertRegionToString(Region region) {
        if (region == null) {
            return null;
        }
        return region.toString();
    }

    public Region convertStringToRegion(String regionName) {
        if (regionName == null) {
            return null;
        }
        return Region.of(regionName);
    }

   public String mapConsignmentIdToString(ConsignmentId consignmentId) {
        if (consignmentId == null) {
            return null;
        }
        return consignmentId.getId();
    }

    public ConsignmentId mapStringToConsignmentId(String consignmentId) {
        if (consignmentId == null) {
            return null;
        }
        return ConsignmentId.of(consignmentId);
    }
}
