package com.github.cargoclean.core.model.cargo;

/*
    Notice on COPYRIGHT:
    -------------------

    Some code in this file is based on, copied from, and or modified from
    the code in the original DDDSample application. Please, see the copyright
    notice in "README.md" and the copy of the original licence in
    "original-license.txt", as well.
 */


import com.github.cargoclean.core.model.location.Location;
import lombok.Builder;
import lombok.Value;

import javax.validation.constraints.NotNull;
import java.time.ZonedDateTime;

/**
 * Modeled after original "se.citerus.dddsample.domain.model.cargo.Leg".
 */
@Value
@Builder
public class Leg {

    @NotNull
    Location loadLocation;

    @NotNull
    Location unloadLocation;

    @NotNull
    ZonedDateTime loadTime;

    @NotNull
    ZonedDateTime unloadTime;

    public Leg withLoadLocation(Location loadLocation) {
        return newLeg().loadLocation(loadLocation).build();
    }

    private LegBuilder newLeg() {
        return Leg.builder()
                .loadLocation(loadLocation)
                .unloadLocation(unloadLocation)
                .loadTime(loadTime)
                .unloadTime(unloadTime);
    }

    public Leg withLoadTime(ZonedDateTime loadTime) {
        return newLeg().loadTime(loadTime).build();
    }

}
