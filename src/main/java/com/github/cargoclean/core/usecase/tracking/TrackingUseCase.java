package com.github.cargoclean.core.usecase.tracking;

import com.github.cargoclean.core.model.cargo.Cargo;
import com.github.cargoclean.core.model.cargo.TrackingId;
import com.github.cargoclean.core.model.handling.HandlingHistory;
import com.github.cargoclean.core.model.location.Location;
import com.github.cargoclean.core.model.location.UnLocode;
import com.github.cargoclean.core.port.persistence.PersistenceGatewayOutputPort;
import com.github.cargoclean.core.port.security.SecurityOutputPort;
import com.github.cargoclean.core.port.transaction.TransactionOperationsOutputPort;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.Map;

@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class TrackingUseCase implements TrackingInputPort {

    TrackingPresenterOutputPort presenter;

    SecurityOutputPort securityOps;

    PersistenceGatewayOutputPort gatewayOps;

    TransactionOperationsOutputPort txOps;

    @Override
    public void initializeCargoTrackingView() {
        try {
            securityOps.assertThatUserIsAgent();

            presenter.presentInitialViewForCargoTracking();
        } catch (Exception e) {
            presenter.presentError(e);
        }
    }

    @Override
    public void trackCargo(String cargoTrackingId) {
        try {

            txOps.doInTransaction(true, () -> {
                securityOps.assertThatUserIsAgent();

                TrackingId trackingId = TrackingId.of(cargoTrackingId);

                // load cargo
                Cargo cargo = gatewayOps.obtainCargoByTrackingId(trackingId);

                // load handling history of the cargo
                HandlingHistory handlingHistory = gatewayOps.handlingHistory(trackingId);

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
                Map<UnLocode, Location> allLocationsMap = gatewayOps.allLocationsMap();

                txOps.doAfterCommit(() -> presenter.presentCargoTrackingInformation(cargo, handlingHistory, allLocationsMap));
            });

        } catch (Exception e) {
            presenter.presentError(e);
        }

    }
}
