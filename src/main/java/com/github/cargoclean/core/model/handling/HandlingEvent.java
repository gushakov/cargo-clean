package com.github.cargoclean.core.model.handling;

import com.github.cargoclean.core.model.cargo.TrackingId;
import com.github.cargoclean.core.model.location.UnLocode;
import com.github.cargoclean.core.model.voyage.VoyageNumber;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.ZonedDateTime;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Builder
public class HandlingEvent {

    @EqualsAndHashCode.Include
    EventId eventId;

    VoyageNumber voyageNumber;

    UnLocode location;

    TrackingId cargoId;

    ZonedDateTime completionTime;

    ZonedDateTime registrationTime;

    HandlingEventType type;

    Integer version;

}
