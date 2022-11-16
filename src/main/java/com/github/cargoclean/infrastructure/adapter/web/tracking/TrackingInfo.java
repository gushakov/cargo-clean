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

import java.util.List;

/*
    References:
    ----------

    1.  Some methods are copied and modified from "CargoTrackingViewAdapter.java" in the original "DDDSample": https://github.com/citerus/dddsample-core/blob/master/src/main/java/se/citerus/dddsample/interfaces/tracking/CargoTrackingViewAdapter.java
 */


@Value
@Builder
public class TrackingInfo {

    String trackingId;

    String statusText;

    String destination;

    String eta;

    boolean misdirected;

    String nextExpectedActivity;

    List<HandlingEventTrackingInfo> events;
}
