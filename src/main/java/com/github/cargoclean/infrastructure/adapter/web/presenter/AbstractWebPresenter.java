package com.github.cargoclean.infrastructure.adapter.web.presenter;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.NoTransactionException;
import org.springframework.transaction.interceptor.TransactionInterceptor;
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
public abstract class AbstractWebPresenter {

    private final LocalDispatcherServlet dispatcher;
    private final HttpServletRequest httpRequest;
    private final HttpServletResponse httpResponse;

    public void presentError(Exception e) {

        // we need to roll back any active transaction
        try {
            TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
        } catch (NoTransactionException nte) {
            // do nothing if not running in a transactional context
        }

        // redirect to special error handling controller
        redirectError(e.getMessage());
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
}
