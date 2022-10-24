package com.github.cargoclean.core.usecase.handling;

import com.github.cargoclean.core.port.operation.PersistenceGatewayOutputPort;
import com.github.cargoclean.core.port.presenter.handling.HandlingPresenterOutputPort;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class HandlingUseCase implements HandlingInputPort {

    private final HandlingPresenterOutputPort presenter;

    private final PersistenceGatewayOutputPort gatewayOps;

}
