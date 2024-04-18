package com.github.cargoclean.core.usecase.report;

import com.github.cargoclean.core.model.report.ExpectedArrivals;
import com.github.cargoclean.core.port.persistence.PersistenceGatewayOutputPort;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class ReportUseCase implements ReportInputPort {

    private final ReportPresenterOutputPort presenter;

    private final PersistenceGatewayOutputPort gatewayOps;

    @Override
    public void reportExpectedArrivals() {

        final List<ExpectedArrivals> expectedArrivals;

        try {

            // just query the gateway
            expectedArrivals = gatewayOps.queryForExpectedArrivals();

        } catch (Exception e) {
            presenter.presentError(e);
            return;
        }

        presenter.presentExpectedArrivals(expectedArrivals);

    }
}
