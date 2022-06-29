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

/**
 * Modeled after original "se.citerus.dddsample.domain.model.cargo.Cargo".
 */
@Value
@Builder
public class Cargo {

    TrackingId trackingId;
    Location origin;
}
