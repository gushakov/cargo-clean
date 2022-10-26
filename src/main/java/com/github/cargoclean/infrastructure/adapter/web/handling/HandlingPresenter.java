package com.github.cargoclean.infrastructure.adapter.web.handling;

import com.github.cargoclean.core.model.handling.HandlingEvent;
import com.github.cargoclean.core.port.presenter.handling.HandlingPresenterOutputPort;
import com.github.cargoclean.infrastructure.adapter.web.AbstractRestPresenter;
import org.springframework.context.annotation.Scope;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.http.HttpServletResponse;

@Component
@Scope(scopeName = WebApplicationContext.SCOPE_REQUEST)
public class HandlingPresenter extends AbstractRestPresenter implements HandlingPresenterOutputPort {
    protected HandlingPresenter(HttpServletResponse httpServletResponse, MappingJackson2HttpMessageConverter jacksonConverter) {
        super(httpServletResponse, jacksonConverter);
    }

    @Override
    public void presentResultOfRegisteringHandlingEvent(HandlingEvent handlingEvent) {

    }
}
