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
    VoyageNumber voyageNumber;

    @EqualsAndHashCode.Include
    UnLocode location;

    @EqualsAndHashCode.Include
    TrackingId cargoId;

    ZonedDateTime completionTime;
    ZonedDateTime registrationTime;

    @EqualsAndHashCode.Include
    HandlingEventType type;


}
