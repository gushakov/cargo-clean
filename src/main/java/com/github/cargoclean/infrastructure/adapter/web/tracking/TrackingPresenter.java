package com.github.cargoclean.infrastructure.adapter.web.tracking;

import com.github.cargoclean.core.model.cargo.Cargo;
import com.github.cargoclean.core.model.cargo.TransportStatus;
import com.github.cargoclean.core.model.location.Location;
import com.github.cargoclean.core.model.voyage.VoyageNumber;
import com.github.cargoclean.core.port.presenter.tracking.TrackingPresenterOutputPort;
import com.github.cargoclean.infrastructure.adapter.web.AbstractWebPresenter;
import com.github.cargoclean.infrastructure.adapter.web.LocalDispatcherServlet;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

@Component
@Scope(scopeName = "request")
public class TrackingPresenter extends AbstractWebPresenter implements TrackingPresenterOutputPort {

    /*
        Copied from "se.citerus.dddsample.interfaces.tracking.CargoTrackingViewAdapter#FORMAT".
     */
    private final String FORMAT = "yyyy-MM-dd hh:mm";


    public TrackingPresenter(LocalDispatcherServlet dispatcher, HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
        super(dispatcher, httpRequest, httpResponse);
    }

    @Override
    public void presentInitialViewForCargoTracking() {
        presentModelAndView(Map.of("trackingForm", TrackingForm.builder().build()), "track-cargo");
    }

    @Override
    public void presentCargoTrackingInformation(Cargo cargo, Location lastKnownLocation) {

        /*
            We are preparing all the information to be displayed by the "track cargo" view.
            In original application this done in "se.citerus.dddsample.interfaces.tracking.CargoTrackingViewAdapter".
         */

        presentModelAndView(Map.of("trackingForm", TrackingForm.builder()
                        .trackingId(cargo.getTrackingId().getId())
                        .build(),
                "trackingInfo", TrackingInfo.builder()
                        .trackingId(cargo.getTrackingId().toString())
                        .statusText(makeCargoStatusText(cargo.getDelivery().getTransportStatus(), lastKnownLocation,
                                cargo.getDelivery().getCurrentVoyage()))
                        .destination(cargo.getRouteSpecification().getDestination().toString())
                        .eta(getEta(cargo))
                        .build()), "track-cargo");
    }

    /*
        Method copied and modified from
        "se.citerus.dddsample.interfaces.tracking.CargoTrackingViewAdapter#getStatusText".
     */
    private String makeCargoStatusText(TransportStatus transportStatus, Location lastKnownLocation, VoyageNumber currentVoyage) {

        return switch (transportStatus) {
            case IN_PORT -> "In port: %s".formatted(lastKnownLocation);
            case ONBOARD_CARRIER -> "On board carrier, voyage: %s".formatted(currentVoyage);
            case CLAIMED, NOT_RECEIVED, UNKNOWN -> "Unknown";
        };

    }

    /*
        Copied from original "se.citerus.dddsample.interfaces.tracking.CargoTrackingViewAdapter#getEta".
     */
    public String getEta(Cargo cargo) {
        Date eta = cargo.getDelivery().estimatedTimeOfArrival();

        if (eta == null) return "?";
        else return new SimpleDateFormat(FORMAT).format(eta);
    }

}
