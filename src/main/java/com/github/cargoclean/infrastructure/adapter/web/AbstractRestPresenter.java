package com.github.cargoclean.infrastructure.adapter.web;

/*
    References:
    ----------

    1. Clean DDD, REST presenter: https://github.com/gushakov/cleanddd
    2. REST presenter for Clean Architecture: https://github.com/gushakov/clean-rest

 */

import com.github.cargoclean.core.port.error.ErrorHandlingPresenterOutputPort;
import com.github.cargoclean.infrastructure.adapter.AbstractErrorHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.server.DelegatingServerHttpResponse;
import org.springframework.http.server.ServletServerHttpResponse;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.Optional;

@Slf4j
public abstract class AbstractRestPresenter extends AbstractErrorHandler implements ErrorHandlingPresenterOutputPort {

    private final HttpServletResponse httpServletResponse;
    private final MappingJackson2HttpMessageConverter jacksonConverter;

    protected AbstractRestPresenter(HttpServletResponse httpServletResponse, MappingJackson2HttpMessageConverter jacksonConverter) {
        this.httpServletResponse = httpServletResponse;
        this.jacksonConverter = jacksonConverter;
    }

    // REST presenter for Clean Architecture
    // copied from https://github.com/gushakov/clean-rest
    public <T> void presentOk(T content) {

        final DelegatingServerHttpResponse httpOutputMessage =
                new DelegatingServerHttpResponse(new ServletServerHttpResponse(httpServletResponse));

        httpOutputMessage.setStatusCode(HttpStatus.OK);

        try {
            jacksonConverter.write(content, MediaType.APPLICATION_JSON, httpOutputMessage);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void presentError(Exception t) {

        doPresentError(t, HttpStatus.INTERNAL_SERVER_ERROR);

    }

    public void presentClientError(Exception t) {
        doPresentError(t, HttpStatus.BAD_REQUEST);
    }

    protected void doPresentError(Exception t, HttpStatus status) {

        logErrorAndRollBack(t);

        final DelegatingServerHttpResponse httpOutputMessage =
                new DelegatingServerHttpResponse(new ServletServerHttpResponse(httpServletResponse));

        httpOutputMessage.setStatusCode(status);

        try {
            jacksonConverter.write(Map.of("error", Optional.ofNullable(t.getMessage()).orElse("null")),
                    MediaType.APPLICATION_JSON, httpOutputMessage);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
