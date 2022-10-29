package com.github.cargoclean.core.model.cargo;

/*
    Notice on COPYRIGHT:
    -------------------

    Some code in this file is based on, copied from, and or modified from
    the code in the original DDDSample application. Please, see the copyright
    notice in "README.md" and the copy of the original licence in
    "original-license.txt", as well.
 */


import com.github.cargoclean.core.model.handling.HandlingEvent;
import com.github.cargoclean.core.model.handling.HandlingHistory;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotNull;

import static com.github.cargoclean.core.model.cargo.TransportStatus.*;


/**
 * Modeled after "se.citerus.dddsample.domain.model.cargo.Delivery".
 */
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class Delivery {


    HandlingEvent lastEvent;

    @NotNull
    @Getter
    TransportStatus transportStatus;

    /**
     * This constructor is needed for MapStruct mapper to work. We need to be
     * able to map a database entity to "Cargo".
     *
     * @param transportStatus current transport status for the cargo
     */
    @Builder
    public Delivery(TransportStatus transportStatus) {
        this.lastEvent = null;
        this.transportStatus = transportStatus;

    }

    static Delivery derivedFrom(RouteSpecification routeSpecification, Itinerary itinerary, HandlingHistory handlingHistory) {

        return new Delivery(handlingHistory.mostRecentlyCompletedEvent().orElse(null), itinerary, routeSpecification);
    }

    private Delivery(HandlingEvent lastEvent, Itinerary itinerary, RouteSpecification routeSpecification) {
        this.lastEvent = lastEvent;
        transportStatus = calculateTransportStatus();
    }


    /*
        Copied and modified from original: "se.citerus.dddsample.domain.model.cargo.Delivery#calculateTransportStatus".
     */
    private TransportStatus calculateTransportStatus() {
        if (lastEvent == null) {
            return NOT_RECEIVED;
        }

        return switch (lastEvent.getType()) {
            case LOAD -> ONBOARD_CARRIER;
            case UNLOAD, RECEIVE, CUSTOMS -> IN_PORT;
            case CLAIM -> CLAIMED;
        };
    }
}
