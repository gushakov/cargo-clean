package com.github.cargoclean.core.usecase.routing;

import com.github.cargoclean.core.model.cargo.Cargo;
import com.github.cargoclean.core.model.cargo.TrackingId;
import com.github.cargoclean.core.port.operation.PersistenceGatewayOutputPort;
import com.github.cargoclean.core.port.presenter.routing.RoutingPresenterOutputPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.NoSuchElementException;

@RequiredArgsConstructor
@Slf4j
public class RoutingUseCase implements RoutingInputPort {

    private final RoutingPresenterOutputPort presenter;

    private final PersistenceGatewayOutputPort gatewayOps;

    @Override
    public void showCargo(TrackingId trackingId) {
        final Cargo cargo;
        try {
            cargo = gatewayOps.obtainCargoByTrackingId(trackingId);

        } catch (NoSuchElementException e) {
            // we should be using domain exceptions here
            presenter.presentError(new Exception("Cannot find cargo with tracking ID: %s".formatted(trackingId)));
            return;
        } catch (Exception e){
            presenter.presentError(e);
            return;
        }

        presenter.presentCargoForRouting(cargo);

    }
}
