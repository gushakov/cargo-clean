package com.github.cargoclean.infrastructure.adapter.web.tracking;

import com.github.cargoclean.core.model.UtcDateTime;
import com.github.cargoclean.core.model.cargo.Cargo;
import com.github.cargoclean.core.model.cargo.Delivery;
import com.github.cargoclean.core.model.cargo.HandlingActivity;
import com.github.cargoclean.core.model.cargo.TransportStatus;
import com.github.cargoclean.core.model.handling.HandlingEventType;
import com.github.cargoclean.core.model.location.Location;
import com.github.cargoclean.core.model.voyage.VoyageNumber;
import com.github.cargoclean.core.port.presenter.tracking.TrackingPresenterOutputPort;
import com.github.cargoclean.infrastructure.adapter.web.AbstractWebPresenter;
import com.github.cargoclean.infrastructure.adapter.web.LocalDispatcherServlet;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

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
    public void presentCargoTrackingInformation(Cargo cargo, Location lastKnownLocation, Location locationForNexExpectedActivity) {

        /*
            We are preparing all the information to be displayed by the "track cargo" view.
            In original application this done in "se.citerus.dddsample.interfaces.tracking.CargoTrackingViewAdapter".
         */

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
                        .build()), "track-cargo");
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
