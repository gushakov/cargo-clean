package com.github.cargoclean.infrastructure.adapter.web;

/*
    References:
    ----------

    1. Clean DDD, REST presenter: https://github.com/gushakov/cleanddd
    2. REST presenter for Clean Architecture: https://github.com/gushakov/clean-rest

 */

import com.github.cargoclean.core.port.ErrorHandlingPresenterOutputPort;
import com.github.cargoclean.core.port.security.CargoSecurityError;
import com.github.cargoclean.infrastructure.adapter.AbstractErrorHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.server.DelegatingServerHttpResponse;
import org.springframework.http.server.ServletServerHttpResponse;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
public abstract class AbstractRestPresenter extends AbstractErrorHandler implements ErrorHandlingPresenterOutputPort {

    private final HttpServletResponse httpServletResponse;
    private final MappingJackson2HttpMessageConverter jacksonConverter;

    private final AtomicBoolean presentedOnceAlready;

    protected AbstractRestPresenter(HttpServletResponse httpServletResponse, MappingJackson2HttpMessageConverter jacksonConverter) {
        this.httpServletResponse = httpServletResponse;
        this.jacksonConverter = jacksonConverter;
        this.presentedOnceAlready = new AtomicBoolean(false);
    }

    // REST presenter for Clean Architecture
    // based on the idea from https://github.com/gushakov/clean-rest
    public <T> void presentOk(T content) {

        /*
            Point of interest:
            -----------------
            A REST presenter bean is "request" scoped, so it may, potentially,
            be called several times (from several consecutive use cases) which
            share the reference to it. We should either coordinate calls to use
            cases to avoid this situation or synchronize output message processing
            in this presenter.
         */

        if (!presentedOnceAlready.getAndSet(true)) {
            try (final DelegatingServerHttpResponse httpOutputMessage =
                         new DelegatingServerHttpResponse(new ServletServerHttpResponse(httpServletResponse))) {
                httpOutputMessage.setStatusCode(HttpStatus.OK);
                jacksonConverter.write(content, MediaType.APPLICATION_JSON, httpOutputMessage);
                httpOutputMessage.flush();
            } catch (Exception e) {
                log.error("[REST][Presenter] Error while presenting OK content. %s"
                        .formatted(e.getMessage()), e);
            }
        }

    }

    @Override
    public void presentError(Exception t) {

        if (t instanceof CargoSecurityError) {
            presentSecurityError((CargoSecurityError) t);
        } else {
            doPresentError(t, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    protected void presentSecurityError(CargoSecurityError e) {

        if (e.isUserAuthenticated()) {
            doPresentError(e, HttpStatus.FORBIDDEN);
        } else {
            doPresentError(e, HttpStatus.UNAUTHORIZED);
        }

    }

    public void presentClientError(Exception t) {
        doPresentError(t, HttpStatus.BAD_REQUEST);
    }

    protected void doPresentError(Exception t, HttpStatus status) {

        if (!presentedOnceAlready.getAndSet(true)) {
            logError(t);

            try (final DelegatingServerHttpResponse httpOutputMessage =
                         new DelegatingServerHttpResponse(new ServletServerHttpResponse(httpServletResponse))) {
                httpOutputMessage.setStatusCode(status);
                jacksonConverter.write(Map.of("error", Optional.ofNullable(t.getMessage()).orElse("null")),
                        MediaType.APPLICATION_JSON, httpOutputMessage);
                httpOutputMessage.flush();
            } catch (Exception e) {
                log.error("[REST][Presenter] Error while presenting an error: %s. %s"
                        .formatted(t.getMessage(), e.getMessage()), e);
            }
        }

    }

}
