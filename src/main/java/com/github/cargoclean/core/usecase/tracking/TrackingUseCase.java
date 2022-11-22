package com.github.cargoclean.core.usecase.tracking;

import com.github.cargoclean.core.GenericCargoError;
import com.github.cargoclean.core.model.cargo.Cargo;
import com.github.cargoclean.core.model.cargo.TrackingId;
import com.github.cargoclean.core.model.handling.HandlingHistory;
import com.github.cargoclean.core.model.location.Location;
import com.github.cargoclean.core.model.location.UnLocode;
import com.github.cargoclean.core.port.operation.persistence.PersistenceGatewayOutputPort;
import com.github.cargoclean.core.port.operation.security.SecurityOutputPort;
import com.github.cargoclean.core.port.presenter.tracking.TrackingPresenterOutputPort;
import lombok.RequiredArgsConstructor;

import java.util.Map;

@RequiredArgsConstructor
public class TrackingUseCase implements TrackingInputPort {

    private final TrackingPresenterOutputPort presenter;

    private final SecurityOutputPort securityOps;

    private final PersistenceGatewayOutputPort gatewayOps;


    @Override
    public void initializeCargoTrackingView() {
        try {
            securityOps.assertThatUserIsAgent();
        } catch (GenericCargoError e) {
            presenter.presentError(e);
            return;
        }
        presenter.presentInitialViewForCargoTracking();
    }

    @Override
    public void trackCargo(String cargoTrackingId) {
        TrackingId trackingId;
        Cargo cargo;
        HandlingHistory handlingHistory;
        Map<UnLocode, Location> allLocationsMap;
        try {
            securityOps.assertThatUserIsAgent();

            trackingId = TrackingId.of(cargoTrackingId);

            // load cargo
            cargo = gatewayOps.obtainCargoByTrackingId(trackingId);

            // load handling history of the cargo
            handlingHistory = gatewayOps.handlingHistory(trackingId);

            /*
                Point of interest:
                -----------------
                Since we have modeled "Cargo" aggregate with
                "UnLocode"s (IDs), and not references to
                "Location", we need to load all "Locations"
                and pass them to the presenter.
                In the original, "DDDSample", this is different since
                "Cargo" aggregate directly references "Location",
                which is loaded by the ORM.
             */

            // load all locations and make a map of UnLocode to Locations
            allLocationsMap = gatewayOps.allLocationsMap();

        } catch (GenericCargoError e) {
            presenter.presentError(e);
            return;
        }

        presenter.presentCargoTrackingInformation(cargo, handlingHistory, allLocationsMap);
    }
}
