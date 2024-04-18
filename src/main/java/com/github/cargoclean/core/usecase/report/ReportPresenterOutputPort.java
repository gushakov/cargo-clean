package com.github.cargoclean.core.usecase.report;

import com.github.cargoclean.core.model.report.ExpectedArrivals;
import com.github.cargoclean.core.port.ErrorHandlingPresenterOutputPort;

import java.util.List;

public interface ReportPresenterOutputPort extends ErrorHandlingPresenterOutputPort {

    void presentExpectedArrivals(List<ExpectedArrivals> expectedArrivalsList);

}
