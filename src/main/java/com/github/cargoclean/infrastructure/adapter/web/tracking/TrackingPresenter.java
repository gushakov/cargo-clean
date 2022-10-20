package com.github.cargoclean.infrastructure.adapter.web.tracking;

import com.github.cargoclean.core.port.presenter.tracking.TrackingPresenterOutputPort;
import com.github.cargoclean.infrastructure.adapter.web.AbstractWebPresenter;
import com.github.cargoclean.infrastructure.adapter.web.LocalDispatcherServlet;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Component
@Scope(scopeName = "request")
public class TrackingPresenter extends AbstractWebPresenter implements TrackingPresenterOutputPort {
    public TrackingPresenter(LocalDispatcherServlet dispatcher, HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
        super(dispatcher, httpRequest, httpResponse);
    }

    @Override
    public void presentInitialViewForCargoTracking() {
        presentModelAndView(Map.of(), "track-cargo");
    }
}
