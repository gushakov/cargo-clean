package com.github.cargoclean.core.model.cargo;

/*
    Notice on COPYRIGHT:
    -------------------

    Some code in this file is based on, copied from, and or modified from
    the code in the original DDDSample application. Please, see the copyright
    notice in "README.md" and the copy of the original licence in
    "original-license.txt", as well.
 */

import com.github.cargoclean.core.model.UtcDateTime;
import com.github.cargoclean.core.model.location.UnLocode;
import com.github.cargoclean.core.model.voyage.VoyageNumber;
import lombok.Builder;
import lombok.Value;

import static com.github.cargoclean.core.model.Assert.notNull;

/**
 * Modeled after original "se.citerus.dddsample.domain.model.cargo.Leg".
 */
@Value
public class Leg {

    TrackingId cargoTrackingId;

    VoyageNumber voyageNumber;

    UnLocode loadLocation;
    UnLocode unloadLocation;

    UtcDateTime loadTime;
    UtcDateTime unloadTime;

    @Builder
    public Leg(TrackingId cargoTrackingId, VoyageNumber voyageNumber, UnLocode loadLocation,
               UnLocode unloadLocation, UtcDateTime loadTime, UtcDateTime unloadTime) {
        this.cargoTrackingId = notNull(cargoTrackingId);
        this.voyageNumber = notNull(voyageNumber);
        this.loadLocation = notNull(loadLocation);
        this.unloadLocation = notNull(unloadLocation);
        this.loadTime = notNull(loadTime);
        this.unloadTime = notNull(unloadTime);
    }
}
