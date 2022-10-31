package com.github.cargoclean.core.model.handling;

/*
    Notice on COPYRIGHT:
    -------------------

    Some code in this file is based on, copied from, and or modified from
    the code in the original DDDSample application. Please, see the copyright
    notice in "README.md" and the copy of the original licence in
    "original-license.txt", as well.
 */


import com.github.cargoclean.core.model.cargo.TrackingId;
import com.github.cargoclean.core.model.location.UnLocode;
import com.github.cargoclean.core.model.voyage.VoyageNumber;
import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotNull;
import java.time.ZonedDateTime;

/*
    Modeled after "se.citerus.dddsample.domain.model.handling.HandlingEvent".
 */
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Builder
public class HandlingEvent {

    /*
        Consider handling events as equal if they are for the same cargo (tracking ID), and they happened
        in the same location, and they are of the same type.
     */

    @NotNull
    EventId eventId;

    @EqualsAndHashCode.Include
    VoyageNumber voyageNumber;

    @NotNull
    @EqualsAndHashCode.Include
    UnLocode location;

    @NonNull
    @EqualsAndHashCode.Include
    TrackingId cargoId;

    @NotNull
    ZonedDateTime completionTime;

    @NotNull
    ZonedDateTime registrationTime;

    @NotNull
    @EqualsAndHashCode.Include
    HandlingEventType type;

    Integer version;

}
