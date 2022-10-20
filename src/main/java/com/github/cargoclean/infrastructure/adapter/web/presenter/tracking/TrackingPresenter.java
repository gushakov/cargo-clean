package com.github.cargoclean.infrastructure.adapter.web.presenter.tracking;

import com.github.cargoclean.core.port.presenter.tracking.TrackingPresenterOutputPort;
import com.github.cargoclean.infrastructure.adapter.web.presenter.AbstractWebPresenter;
import com.github.cargoclean.infrastructure.adapter.web.presenter.LocalDispatcherServlet;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
@Scope(scopeName = "request")
public class TrackingPresenter extends AbstractWebPresenter implements TrackingPresenterOutputPort {
    public TrackingPresenter(LocalDispatcherServlet dispatcher, HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
        super(dispatcher, httpRequest, httpResponse);
    }
}
