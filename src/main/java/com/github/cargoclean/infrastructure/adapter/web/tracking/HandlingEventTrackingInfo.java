package com.github.cargoclean.infrastructure.adapter.web.tracking;

/*
    Notice on COPYRIGHT:
    -------------------

    Some code in this file is based on, copied from, and or modified from
    the code in the original DDDSample application. Please, see the copyright
    notice in "README.md" and the copy of the original licence in
    "original-license.txt", as well.
 */


import lombok.Builder;
import lombok.Value;

/*
    References:
    ----------

    1.  Some code in this class is copied and/or modified from "se.citerus.dddsample.interfaces.tracking.CargoTrackingViewAdapter"
        and "se.citerus.dddsample.interfaces.tracking.CargoTrackingViewAdapter.HandlingEventViewAdapter"
        classes in the original application "DDDSample" application.
 */

@Value
@Builder
public class HandlingEventTrackingInfo {

    boolean expected;

    String description;

}
