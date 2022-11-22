package com.github.cargoclean.core.usecase.welcome;

import com.github.cargoclean.core.GenericCargoError;
import com.github.cargoclean.core.port.operation.security.SecurityOutputPort;
import com.github.cargoclean.core.port.presenter.welcome.WelcomePresenterOutputPort;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class WelcomeUseCase implements WelcomeInputPort {

    private final WelcomePresenterOutputPort presenter;

    private final SecurityOutputPort securityOps;

    @Override
    public void welcome() {
        final String username;
        try {

            username = securityOps.username().orElse(null);

        } catch (GenericCargoError e) {
            presenter.presentError(e);
            return;
        }

        presenter.presentHomePage(username);
    }
}
