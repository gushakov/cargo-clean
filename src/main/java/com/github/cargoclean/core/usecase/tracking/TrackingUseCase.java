package com.github.cargoclean.core.usecase.tracking;

import com.github.cargoclean.core.CargoSecurityError;
import com.github.cargoclean.core.GenericCargoError;
import com.github.cargoclean.core.model.cargo.Cargo;
import com.github.cargoclean.core.model.cargo.TrackingId;
import com.github.cargoclean.core.model.handling.HandlingHistory;
import com.github.cargoclean.core.model.location.Location;
import com.github.cargoclean.core.port.operation.PersistenceGatewayOutputPort;
import com.github.cargoclean.core.port.operation.SecurityOutputPort;
import com.github.cargoclean.core.port.presenter.tracking.TrackingPresenterOutputPort;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class TrackingUseCase implements TrackingInputPort {

    private final TrackingPresenterOutputPort presenter;

    private final SecurityOutputPort securityOps;

    private final PersistenceGatewayOutputPort gatewayOps;


    @Override
    public void initializeCargoTrackingView() {
        try {
            securityOps.assertThatUserIsAgent();
        } catch (CargoSecurityError e) {
            presenter.presentSecurityError(e);
            return;
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
        List<Location> allLocations;
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
                Since we have modeled "Cargo" aggregate with "UnLocode"s (IDs), and not references
                to "Location", we need to load all "Locations" and them to the presenter.
                In the original, "DDDSample", this is different since "Cargo" aggregate directly references
                "Location", which is loaded by the ORM.
             */

            // load all locations
            allLocations = gatewayOps.allLocations();

        } catch (CargoSecurityError e) {
            presenter.presentSecurityError(e);
            return;
        } catch (GenericCargoError e) {
            presenter.presentError(e);
            return;
        }

        presenter.presentCargoTrackingInformation(cargo, handlingHistory, allLocations);
    }
}
