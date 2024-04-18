package com.github.cargoclean.infrastructure.adapter.web.tracking;

/*
    Notice on COPYRIGHT:
    -------------------

    Some code in this file is based on, copied from, and or modified from
    the code in the original DDDSample application. Please, see the copyright
    notice in "README.md" and the copy of the original licence in
    "original-license.txt", as well.
 */


import com.github.cargoclean.core.model.UtcDateTime;
import com.github.cargoclean.core.model.cargo.Cargo;
import com.github.cargoclean.core.model.cargo.Delivery;
import com.github.cargoclean.core.model.cargo.HandlingActivity;
import com.github.cargoclean.core.model.cargo.TransportStatus;
import com.github.cargoclean.core.model.handling.HandlingEvent;
import com.github.cargoclean.core.model.handling.HandlingEventType;
import com.github.cargoclean.core.model.handling.HandlingHistory;
import com.github.cargoclean.core.model.location.Location;
import com.github.cargoclean.core.model.location.UnLocode;
import com.github.cargoclean.core.model.voyage.VoyageNumber;
import com.github.cargoclean.core.usecase.tracking.TrackingPresenterOutputPort;
import com.github.cargoclean.infrastructure.adapter.web.AbstractWebPresenter;
import com.github.cargoclean.infrastructure.adapter.web.LocalDispatcherServlet;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/*
    References:
    ----------

    1.  Some code in this class is copied and/or modified from "se.citerus.dddsample.interfaces.tracking.CargoTrackingViewAdapter"
        and "se.citerus.dddsample.interfaces.tracking.CargoTrackingViewAdapter.HandlingEventViewAdapter"
        classes in the original application "DDDSample" application.
 */

@Component
@Scope(scopeName = "request")
public class TrackingPresenter extends AbstractWebPresenter implements TrackingPresenterOutputPort {

    public TrackingPresenter(LocalDispatcherServlet dispatcher, HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
        super(dispatcher, httpRequest, httpResponse);
    }

    @Override
    public void presentInitialViewForCargoTracking() {
        presentModelAndView(Map.of("trackingForm", TrackingForm.builder().build()), "track-cargo");
    }

    @Override
    public void presentCargoTrackingInformation(Cargo cargo, HandlingHistory handlingHistory,
                                                Map<UnLocode, Location> allLocationsMap) {

        /*
            We are preparing all the information to be displayed by the "track cargo" view.
            In original application this done in "se.citerus.dddsample.interfaces.tracking.CargoTrackingViewAdapter",
            and "se.citerus.dddsample.interfaces.tracking.CargoTrackingViewAdapter.HandlingEventViewAdapter".
         */

        // resolve last known location, if any
        Location lastKnownLocation = Optional.ofNullable(cargo.getDelivery().getLastKnownLocation())
                .map(allLocationsMap::get)
                .orElse(Location.UNKNOWN);

        // resolve next expected activity, if any
        Location locationForNexExpectedActivity = Optional.ofNullable(cargo.getDelivery().getNextExpectedActivity())
                .map(HandlingActivity::getLocation)
                .map(allLocationsMap::get)
                .orElse(Location.UNKNOWN);

        // map handling history to the list of events for presentation
        List<HandlingEventTrackingInfo> events = handlingHistory.historyOfEvents()
                .stream()
                .map(handlingEvent -> HandlingEventTrackingInfo.builder()
                        .expected(isEventExpected(handlingEvent, cargo))
                        .description(eventDescription(handlingEvent, allLocationsMap))
                        .build())
                .toList();

        Delivery delivery = cargo.getDelivery();
        presentModelAndView(Map.of("trackingForm", TrackingForm.builder()
                        .trackingId(cargo.getTrackingId().getId())
                        .build(),
                "trackingInfo", TrackingInfo.builder()
                        .trackingId(cargo.getTrackingId().toString())
                        .statusText(makeCargoStatusText(delivery.getTransportStatus(), lastKnownLocation,
                                delivery.getCurrentVoyage()))
                        .destination(cargo.getRouteSpecification().getDestination().toString())
                        .eta(getEta(cargo))
                        .misdirected(delivery.isMisdirected())
                        .nextExpectedActivity(getNextExpectedActivity(delivery, locationForNexExpectedActivity))
                        .events(events)
                        .build()), "track-cargo");
    }

    /*
        Copied from original "se.citerus.dddsample.interfaces.tracking.CargoTrackingViewAdapter.HandlingEventViewAdapter#isExpected".
    */
    private boolean isEventExpected(HandlingEvent handlingEvent, Cargo cargo) {
        return cargo.getItinerary().isExpected(handlingEvent);
    }

    /*
        Copied and modified from original "se.citerus.dddsample.interfaces.tracking.CargoTrackingViewAdapter.HandlingEventViewAdapter#getDescription".
     */
    private String eventDescription(HandlingEvent event, Map<UnLocode, Location> allLocationsMap) {

        final String atLocation = allLocationsMap.get(event.getLocation()).getName();
        final String completionTime = event.getCompletionTime().format(DateTimeFormatter.ofPattern("dd.MM.yy HH:mm"));
        return switch (event.getType()) {
            case LOAD -> "Loaded onto voyage %s in %s, at %s"
                    .formatted(event.getVoyageNumber(), atLocation, completionTime);
            case UNLOAD -> "Unloaded off voyage %s in %s, at %s"
                    .formatted(event.getVoyageNumber(), atLocation, completionTime);
            case RECEIVE -> "Received in %s, at %s".formatted(atLocation, completionTime);
            case CLAIM -> "Claimed in %s, at %s".formatted(atLocation, completionTime);
            case CUSTOMS -> "Cleared customs in %s, at %s".formatted(atLocation, completionTime);
        };

    }

    /*
        Method copied and modified from
        "se.citerus.dddsample.interfaces.tracking.CargoTrackingViewAdapter#getStatusText".
     */
    private String makeCargoStatusText(TransportStatus transportStatus, Location lastKnownLocation, VoyageNumber currentVoyage) {

        return switch (transportStatus) {
            case IN_PORT -> "In port: %s".formatted(lastKnownLocation.getName());
            case ONBOARD_CARRIER -> "On board carrier, voyage: %s".formatted(currentVoyage);
            case CLAIMED, NOT_RECEIVED, UNKNOWN -> "Unknown";
        };

    }

    /*
        Copied from original "se.citerus.dddsample.interfaces.tracking.CargoTrackingViewAdapter#getEta".
     */
    public String getEta(Cargo cargo) {
        UtcDateTime eta = cargo.getDelivery().estimatedTimeOfArrival();

        if (eta == null || eta.isUnknown()) return "?";
        else return eta.toString();
    }

    /*
        Copied and modified slightly from the original
        "se.citerus.dddsample.interfaces.tracking.CargoTrackingViewAdapter#getNextExpectedActivity".
     */
    public String getNextExpectedActivity(Delivery delivery, Location locationForNexExpectedActivity) {
        HandlingActivity activity = delivery.getNextExpectedActivity();
        if (activity == null) {
            return "";
        }

        String text = "Next expected activity is to ";
        HandlingEventType type = activity.getType();
        if (type == HandlingEventType.LOAD) {
            return
                    text + type.name().toLowerCase() + " cargo onto voyage " + activity.getVoyageNumber() +
                            " in " + locationForNexExpectedActivity.getName();
        } else if (type == HandlingEventType.UNLOAD) {
            return
                    text + type.name().toLowerCase() + " cargo off of " + activity.getVoyageNumber() +
                            " in " + locationForNexExpectedActivity.getName();
        } else {
            return text + type.name().toLowerCase() + " cargo in " + locationForNexExpectedActivity.getName();
        }
    }

}
