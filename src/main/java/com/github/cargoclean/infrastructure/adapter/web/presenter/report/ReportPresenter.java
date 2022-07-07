package com.github.cargoclean.infrastructure.adapter.web.presenter.report;

import com.github.cargoclean.core.model.report.ExpectedArrivals;
import com.github.cargoclean.core.port.presenter.report.ReportPresenterOutputPort;
import com.github.cargoclean.infrastructure.adapter.web.presenter.AbstractWebPresenter;
import com.github.cargoclean.infrastructure.adapter.web.presenter.LocalDispatcherServlet;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

@Component
@Scope(scopeName = WebApplicationContext.SCOPE_REQUEST)
public class ReportPresenter extends AbstractWebPresenter implements ReportPresenterOutputPort {
    public ReportPresenter(LocalDispatcherServlet dispatcher, HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
        super(dispatcher, httpRequest, httpResponse);
    }

    @Override
    public void presentExpectedArrivals(List<ExpectedArrivals> expectedArrivalsList) {

        // we can reuse the value object for the results of the query as the
        // Response Model for the view

        presentModelAndView(Map.of("expectedArrivals", expectedArrivalsList), "expected-arrivals");

    }
}
