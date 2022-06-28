package com.github.cargoclean.core.usecase.routing;

import com.github.cargoclean.core.port.operation.PersistenceGatewayOutputPort;
import com.github.cargoclean.core.port.presenter.routing.RoutingPresenterOutputPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
public class RoutingUseCase implements RoutingInputPort {

    private final RoutingPresenterOutputPort presenter;

    private final PersistenceGatewayOutputPort gatewayOps;

}
