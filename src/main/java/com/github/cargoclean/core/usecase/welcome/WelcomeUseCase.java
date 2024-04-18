package com.github.cargoclean.core.usecase.welcome;

import com.github.cargoclean.core.model.cargo.CargoInfo;
import com.github.cargoclean.core.port.persistence.PersistenceGatewayOutputPort;
import com.github.cargoclean.core.port.security.SecurityOutputPort;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class WelcomeUseCase implements WelcomeInputPort {

    private final WelcomePresenterOutputPort presenter;

    private final SecurityOutputPort securityOps;

    private final PersistenceGatewayOutputPort gatewayOps;

    @Override
    public void welcome() {
        final String username;
        final List<CargoInfo> cargoes;
        try {

            username = securityOps.username().orElse(null);

            cargoes = gatewayOps.allCargoes();

        } catch (Exception e) {
            presenter.presentError(e);
            return;
        }

        presenter.presentHomePage(username, cargoes);
    }
}
