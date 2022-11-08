package com.github.cargoclean.core.model.handling;

/*
    Notice on COPYRIGHT:
    -------------------

    Some code in this file is based on, copied from, and or modified from
    the code in the original DDDSample application. Please, see the copyright
    notice in "README.md" and the copy of the original licence in
    "original-license.txt", as well.
 */


import com.github.cargoclean.core.model.UtcDateTime;
import com.github.cargoclean.core.model.cargo.TrackingId;
import com.github.cargoclean.core.model.location.UnLocode;
import com.github.cargoclean.core.model.voyage.VoyageNumber;
import lombok.*;
import lombok.experimental.FieldDefaults;

import static com.github.cargoclean.core.model.Assert.notNull;

/*
    Modeled after "se.citerus.dddsample.domain.model.handling.HandlingEvent".
 */
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
public class HandlingEvent {

    /*
        Consider handling events as equal if they are for the same cargo (tracking ID), and they happened
        in the same location, and they are of the same type.
     */

    EventId eventId;

    @EqualsAndHashCode.Include
    VoyageNumber voyageNumber;

    @EqualsAndHashCode.Include
    UnLocode location;

    @EqualsAndHashCode.Include
    TrackingId cargoId;

    UtcDateTime completionTime;

    UtcDateTime registrationTime;

    @EqualsAndHashCode.Include
    HandlingEventType type;

    Integer version;

    @Builder
    public HandlingEvent(EventId eventId, VoyageNumber voyageNumber, UnLocode location, TrackingId cargoId,
                         UtcDateTime completionTime, UtcDateTime registrationTime, HandlingEventType type,
                         Integer version) {
        this.eventId = notNull(eventId);
        this.voyageNumber = voyageNumber;
        this.location = notNull(location);
        this.cargoId = cargoId;
        this.completionTime = notNull(completionTime);
        this.registrationTime = notNull(registrationTime);
        this.type = notNull(type);
        this.version = version;
    }
}
