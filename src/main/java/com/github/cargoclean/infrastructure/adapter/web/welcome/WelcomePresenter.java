package com.github.cargoclean.infrastructure.adapter.web.welcome;

import com.github.cargoclean.core.model.cargo.CargoInfo;
import com.github.cargoclean.core.usecase.welcome.WelcomePresenterOutputPort;
import com.github.cargoclean.infrastructure.adapter.web.AbstractWebPresenter;
import com.github.cargoclean.infrastructure.adapter.web.LocalDispatcherServlet;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Scope(scopeName = WebApplicationContext.SCOPE_REQUEST)
@Slf4j
public class WelcomePresenter extends AbstractWebPresenter implements WelcomePresenterOutputPort {
    public WelcomePresenter(LocalDispatcherServlet dispatcher, HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
        super(dispatcher, httpRequest, httpResponse);
    }

    @Override
    public void presentHomePage(String username, List<CargoInfo> cargoes) {

        final Map<String, Object> model = new HashMap<>();

        if (username != null) {
            model.put("username", username);
        }

        model.put("cargoes", cargoes);

        presentModelAndView(model, "home");
    }
}
