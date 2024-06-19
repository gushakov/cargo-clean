package com.github.cargoclean.core.usecase.report;

import com.github.cargoclean.core.model.report.ExpectedArrivals;
import com.github.cargoclean.core.port.persistence.PersistenceGatewayOutputPort;
import com.github.cargoclean.core.port.transaction.TransactionOperationsOutputPort;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.List;

@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class ReportUseCase implements ReportInputPort {

    ReportPresenterOutputPort presenter;

    PersistenceGatewayOutputPort gatewayOps;

    TransactionOperationsOutputPort txOps;

    @Override
    public void reportExpectedArrivals() {

        try {

            txOps.doInTransaction(true, () -> {
                // just query the gateway
                final List<ExpectedArrivals> expectedArrivals = gatewayOps.queryForExpectedArrivals();

                txOps.doAfterCommit(() -> presenter.presentExpectedArrivals(expectedArrivals));
            });

        } catch (Exception e) {
            presenter.presentError(e);
        }

    }
}
