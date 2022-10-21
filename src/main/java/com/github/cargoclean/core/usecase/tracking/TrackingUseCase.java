package com.github.cargoclean.core.usecase.tracking;

import com.github.cargoclean.core.GenericCargoError;
import com.github.cargoclean.core.model.cargo.Cargo;
import com.github.cargoclean.core.model.cargo.TrackingId;
import com.github.cargoclean.core.port.operation.PersistenceGatewayOutputPort;
import com.github.cargoclean.core.port.presenter.tracking.TrackingPresenterOutputPort;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TrackingUseCase implements TrackingInputPort {

    private final TrackingPresenterOutputPort presenter;

    private final PersistenceGatewayOutputPort gatewayOps;

    @Override
    public void initializeCargoTrackingView() {
        presenter.presentInitialViewForCargoTracking();
    }

    @Override
    public void trackCargo(String trackingIdText) {
        TrackingId trackingId;
        Cargo cargo;
        try {
            trackingId = TrackingId.of(trackingIdText);
            cargo = gatewayOps.obtainCargoByTrackingId(trackingId);
        }
        catch (IllegalArgumentException | GenericCargoError e){
            // cannot parse tracking ID
            presenter.presentError(e);
            return;
        }

        presenter.presentCargoTrackingInformation(cargo);
    }
}
