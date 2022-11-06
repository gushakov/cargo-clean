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
import com.github.cargoclean.core.model.handling.HandlingEvent;
import com.github.cargoclean.core.model.handling.HandlingEventType;
import com.github.cargoclean.core.model.handling.HandlingHistory;
import com.github.cargoclean.core.model.location.UnLocode;
import com.github.cargoclean.core.model.voyage.VoyageNumber;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotNull;
import java.util.Iterator;
import java.util.Optional;

import static com.github.cargoclean.core.model.cargo.RoutingStatus.*;
import static com.github.cargoclean.core.model.cargo.TransportStatus.*;


/**
 * Modeled after "se.citerus.dddsample.domain.model.cargo.Delivery".
 */
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class Delivery {

    /*
        Copied from "se.citerus.dddsample.domain.model.cargo.Delivery#ETA_UNKOWN".
     */
    private static final UtcDateTime ETA_UNKOWN = null;

    /*
        Copied from "se.citerus.dddsample.domain.model.cargo.Delivery#NO_ACTIVITY".
     */
    private static final HandlingActivity NO_ACTIVITY = null;

    HandlingEvent lastEvent;

    @NotNull
    @Getter
    TransportStatus transportStatus;

    @Getter
    UnLocode lastKnownLocation;

    @Getter
    VoyageNumber currentVoyage;

    @NotNull
    @Getter
    UtcDateTime eta;

    @NotNull
    @Getter
    RoutingStatus routingStatus;

    @Getter
    boolean misdirected;

    @Getter
    HandlingActivity nextExpectedActivity;

    /**
     * This constructor is needed for MapStruct mapper to map a database entity
     * {@code DeliveryDbEntity} to corresponding {@code Delivery} value object
     * associated with each cargo. All the fields for "Delivery" which are persisted
     * in the database need to be initialized here.
     *
     * @param transportStatus               current transport status for the cargo
     * @param lastKnownLocation             UnLocode for last known location of the cargo, can be {@code null}
     * @param currentVoyage                 current voyage the cargo is on, can be {@code null}
     * @param eta                           estimated date of arrival for the cargo
     * @param routingStatus                 routing status of the cargo
     * @param misdirected                   indicates whether the cargo is misdirected
     * @param nextExpectedHandlingEventType next expected handling event
     * @param nextExpectedLocation          next expected location
     * @param nextExpectedVoyage            next expected voyage number, can be {@code null}
     */
    @Builder
    public Delivery(TransportStatus transportStatus, UnLocode lastKnownLocation, VoyageNumber currentVoyage,
                    UtcDateTime eta, RoutingStatus routingStatus, boolean misdirected, HandlingEventType nextExpectedHandlingEventType,
                    UnLocode nextExpectedLocation, VoyageNumber nextExpectedVoyage) {
        this.transportStatus = transportStatus;
        this.lastKnownLocation = lastKnownLocation;
        this.currentVoyage = currentVoyage;
        this.lastEvent = null;
        this.eta = eta;
        this.routingStatus = routingStatus;
        this.misdirected = misdirected;

        // make an instance of HandlingActivity
        HandlingActivity.HandlingActivityBuilder handlingActivityBuilder = HandlingActivity.builder()
                .type(nextExpectedHandlingEventType)
                .location(nextExpectedLocation);
        if (nextExpectedVoyage != null) {
            handlingActivityBuilder.voyageNumber(nextExpectedVoyage);
        }
        this.nextExpectedActivity = handlingActivityBuilder.build();
    }

    static Delivery derivedFrom(RouteSpecification routeSpecification, Itinerary itinerary, HandlingHistory handlingHistory) {

        return new Delivery(handlingHistory.mostRecentlyCompletedEvent().orElse(null), itinerary, routeSpecification);
    }

    private Delivery(HandlingEvent lastEvent, Itinerary itinerary, RouteSpecification routeSpecification) {
        this.lastEvent = lastEvent;
        this.misdirected = calculateMisdirectionStatus(itinerary);
        this.routingStatus = calculateRoutingStatus(itinerary, routeSpecification);
        this.transportStatus = calculateTransportStatus();
        this.lastKnownLocation = calculateLastKnownLocation().orElse(null);
        this.currentVoyage = calculateCurrentVoyage().orElse(null);
        this.eta = calculateEta(itinerary);
        this.nextExpectedActivity = calculateNextExpectedActivity(routeSpecification, itinerary);
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

    /*
        Copied and modified from "se.citerus.dddsample.domain.model.cargo.Delivery#calculateLastKnownLocation".
     */
    private Optional<UnLocode> calculateLastKnownLocation() {
        if (lastEvent != null) {
            return Optional.of(lastEvent.getLocation());
        } else {
            return Optional.empty();
        }
    }

    // Copied froim "se.citerus.dddsample.domain.model.cargo.Delivery#calculateEta".
    private UtcDateTime calculateEta(Itinerary itinerary) {
        if (onTrack()) {
            return itinerary.finalArrivalDate();
        } else {
            return ETA_UNKOWN;
        }
    }

    /*
        Copied from "se.citerus.dddsample.domain.model.cargo.Delivery#calculateCurrentVoyage".
     */
    private Optional<VoyageNumber> calculateCurrentVoyage() {
        if (getTransportStatus() == ONBOARD_CARRIER && lastEvent != null) {
            return Optional.ofNullable(lastEvent.getVoyageNumber());
        } else {
            return Optional.empty();
        }
    }

    /*
        Modified from "se.citerus.dddsample.domain.model.cargo.Delivery#estimatedTimeOfArrival".
     */
    public UtcDateTime estimatedTimeOfArrival() {
        return eta;
    }

    /*
        Copied from "se.citerus.dddsample.domain.model.cargo.Delivery#onTrack".
     */
    private boolean onTrack() {
        return routingStatus.equals(ROUTED) && !misdirected;
    }

    /*
        Copied from "se.citerus.dddsample.domain.model.cargo.Delivery#calculateRoutingStatus".
     */
    private RoutingStatus calculateRoutingStatus(Itinerary itinerary, RouteSpecification routeSpecification) {
        if (itinerary == null) {
            return NOT_ROUTED;
        } else {
            if (routeSpecification.isSatisfiedBy(itinerary)) {
                return ROUTED;
            } else {
                return MISROUTED;
            }
        }
    }

    /*
        Copied from "se.citerus.dddsample.domain.model.cargo.Delivery#calculateMisdirectionStatus".
     */
    private boolean calculateMisdirectionStatus(Itinerary itinerary) {
        if (lastEvent == null) {
            return false;
        } else {
            return !itinerary.isExpected(lastEvent);
        }
    }

    /*
        Copied and modified from "se.citerus.dddsample.domain.model.cargo.Delivery#calculateNextExpectedActivity".
     */
    private HandlingActivity calculateNextExpectedActivity(RouteSpecification routeSpecification, Itinerary itinerary) {
        if (!onTrack()) return NO_ACTIVITY;

        if (lastEvent == null) return HandlingActivity.builder()
                .type(HandlingEventType.RECEIVE)
                .location(routeSpecification.getOrigin())
                .build();

        switch (lastEvent.getType()) {

            case LOAD:
                for (Leg leg : itinerary.getLegs()) {
                    if (leg.getLoadLocation().equals(lastEvent.getLocation())) {
                        return new HandlingActivity(HandlingEventType.UNLOAD, leg.getUnloadLocation(), leg.getVoyageNumber());
                    }
                }

                return NO_ACTIVITY;

            case UNLOAD:
                for (Iterator<Leg> it = itinerary.getLegs().iterator(); it.hasNext(); ) {
                    final Leg leg = it.next();
                    if (leg.getUnloadLocation().equals(lastEvent.getLocation())) {
                        if (it.hasNext()) {
                            final Leg nextLeg = it.next();
                            return HandlingActivity.builder()
                                    .type(HandlingEventType.LOAD)
                                    .location(nextLeg.getLoadLocation())
                                    .voyageNumber(nextLeg.getVoyageNumber())
                                    .build();
                        } else {
                            return HandlingActivity.builder()
                                    .type(HandlingEventType.CLAIM)
                                    .location(leg.getUnloadLocation())
                                    .build();
                        }
                    }
                }

                return NO_ACTIVITY;

            case RECEIVE:
                final Leg firstLeg = itinerary.getLegs().iterator().next();
                return HandlingActivity.builder()
                        .type(HandlingEventType.LOAD)
                        .location(firstLeg.getLoadLocation())
                        .voyageNumber(firstLeg.getVoyageNumber())
                        .build();

            case CLAIM:
            default:
                return NO_ACTIVITY;
        }
    }
}
