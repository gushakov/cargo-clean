package com.github.cargoclean.infrastructure.adapter.web.welcome;

import com.github.cargoclean.core.port.presenter.welcome.WelcomePresenterOutputPort;
import com.github.cargoclean.infrastructure.adapter.web.AbstractWebPresenter;
import com.github.cargoclean.infrastructure.adapter.web.LocalDispatcherServlet;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Component
@Scope(scopeName = WebApplicationContext.SCOPE_REQUEST)
@Slf4j
public class WelcomePresenter extends AbstractWebPresenter implements WelcomePresenterOutputPort {
    public WelcomePresenter(LocalDispatcherServlet dispatcher, HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
        super(dispatcher, httpRequest, httpResponse);
    }

    @Override
    public void presentHomePage(String username) {

        final Map<String, Object> model;
        if (username != null) {
            model = Map.of("username", username);
        } else {
            model = Map.of();
        }

        presentModelAndView(model, "home");
    }
}
