package com.github.cargoclean.infrastructure.adapter.web;

import com.github.cargoclean.core.port.operation.security.CargoSecurityError;
import com.github.cargoclean.infrastructure.adapter.AbstractErrorHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/*
    References:
    ----------

    1.  Rollback active transaction: https://stackoverflow.com/a/23502214
 */


/**
 * Presents Thymeleaf views by delegating to {@code render()} method of {@link LocalDispatcherServlet}.
 * Concrete Presenters should override providing {@code Response Model} and
 * the name of the view to render. Can also be used to "redirect" response to a particular path.
 *
 * @see LocalDispatcherServlet
 * @see #redirect(String, Map)
 */
@RequiredArgsConstructor
@Slf4j
public abstract class AbstractWebPresenter extends AbstractErrorHandler {

    private final LocalDispatcherServlet dispatcher;
    private final HttpServletRequest httpRequest;
    private final HttpServletResponse httpResponse;

    protected void storeInSession(String attributeName, Object attributeValue) {
        httpRequest.getSession().setAttribute(attributeName, attributeValue);
    }

    public void presentError(Exception e) {

        logError(e);

        // see if this is a security error
        if (e instanceof CargoSecurityError) {
            presentSecurityError((CargoSecurityError) e);
        } else {

            // redirect to special error handling controller
            redirectError(e.getMessage());
        }

    }

    protected void presentSecurityError(CargoSecurityError e) {
        logError(e);

        if (e.isUserAuthenticated()) {
            // if user is authenticated, this is a permission error:
            // go the error view, as usual
            redirectError(e.getMessage());
        } else {
            // redirect to the login page if user is not authenticated
            redirectLogin();
        }
    }

    protected void presentModelAndView(Map<String, Object> responseModel, String viewName) {
        final ModelAndView mav = new ModelAndView(viewName, responseModel);
        try {
            dispatcher.render(mav, httpRequest, httpResponse);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected void redirect(String path, Map<String, String> params) {
        final UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromPath(path);
        params.forEach(uriBuilder::queryParam);
        final String uri = uriBuilder.toUriString();

        try {
            httpRequest.getSession().setAttribute("lastError", Map.copyOf(params));
            httpResponse.sendRedirect(httpResponse.encodeRedirectURL(uri));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected void redirectError(String errorMessage) {
        try {
            httpRequest.getSession().setAttribute("errorMessage", errorMessage);
            httpResponse.sendRedirect("/error");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected void redirectLogin() {
        try {
            httpResponse.sendRedirect("/login");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
