package com.github.cargoclean.core.model.cargo;

/*
    Notice on COPYRIGHT:
    -------------------

    Some code in this file is based on, copied from, and or modified from
    the code in the original DDDSample application. Please, see the copyright
    notice in "README.md" and the copy of the original licence in
    "original-license.txt", as well.
 */


import com.github.cargoclean.core.model.handling.HandlingEventType;
import com.github.cargoclean.core.model.location.UnLocode;
import com.github.cargoclean.core.model.voyage.VoyageNumber;
import lombok.Builder;
import lombok.Value;

import static com.github.cargoclean.core.model.Assert.notNull;

/**
 * Modeled after "se.citerus.dddsample.domain.model.cargo.HandlingActivity".
 */

@Value
public class HandlingActivity {

    HandlingEventType type;

    UnLocode location;

    VoyageNumber voyageNumber;

    @Builder
    public HandlingActivity(HandlingEventType type, UnLocode location, VoyageNumber voyageNumber) {
        this.type = notNull(type);
        this.location = notNull(location);
        this.voyageNumber = voyageNumber;
    }
}
