package com.github.cargoclean.infrastructure.adapter.web.presenter.routing;

import com.github.cargoclean.core.model.cargo.Cargo;
import com.github.cargoclean.core.port.presenter.routing.RoutingPresenterOutputPort;
import com.github.cargoclean.infrastructure.adapter.web.presenter.AbstractWebPresenter;
import com.github.cargoclean.infrastructure.adapter.web.presenter.LocalDispatcherServlet;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
@Scope(scopeName = "request")
public class RoutingPresenter extends AbstractWebPresenter implements RoutingPresenterOutputPort {
    public RoutingPresenter(LocalDispatcherServlet dispatcher, HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
        super(dispatcher, httpRequest, httpResponse);
    }

    @Override
    public void presentCargoForRouting(Cargo cargo) {

    }
}
