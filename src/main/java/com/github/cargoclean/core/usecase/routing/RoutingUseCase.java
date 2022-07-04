package com.github.cargoclean.core.usecase.routing;

import com.github.cargoclean.core.model.cargo.Cargo;
import com.github.cargoclean.core.model.cargo.TrackingId;
import com.github.cargoclean.core.port.operation.PersistenceGatewayOutputPort;
import com.github.cargoclean.core.port.presenter.routing.RoutingPresenterOutputPort;
import com.github.cargoclean.core.validator.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
public class RoutingUseCase implements RoutingInputPort {

    private final RoutingPresenterOutputPort presenter;

    // validation service
    private final Validator validator;

    private final PersistenceGatewayOutputPort gatewayOps;

    @Override
    public void showCargo(TrackingId trackingId) {
        final Cargo cargo;
        try {
            cargo = validator.validate(gatewayOps.obtainCargoByTrackingId(trackingId));
        } catch (Exception e) {
            presenter.presentError(e);
            return;
        }

        presenter.presentCargoForRouting(cargo);

    }
}
