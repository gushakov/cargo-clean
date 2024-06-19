package com.github.cargoclean.core.usecase.welcome;

import com.github.cargoclean.core.model.cargo.CargoInfo;
import com.github.cargoclean.core.port.persistence.PersistenceGatewayOutputPort;
import com.github.cargoclean.core.port.security.SecurityOutputPort;
import com.github.cargoclean.core.port.transaction.TransactionOperationsOutputPort;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.List;

@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class WelcomeUseCase implements WelcomeInputPort {

    WelcomePresenterOutputPort presenter;

    SecurityOutputPort securityOps;

    PersistenceGatewayOutputPort gatewayOps;

    TransactionOperationsOutputPort txOps;

    @Override
    public void welcome() {
        try {

            txOps.doInTransaction(true, () -> {

                final String username = securityOps.username().orElse(null);

                final List<CargoInfo> cargoes = gatewayOps.allCargoes();

                txOps.doAfterCommit(() -> presenter.presentHomePage(username, cargoes));
            });

        } catch (Exception e) {
            presenter.presentError(e);
        }

    }
}
