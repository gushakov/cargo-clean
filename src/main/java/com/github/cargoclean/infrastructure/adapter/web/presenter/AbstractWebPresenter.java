package com.github.cargoclean.infrastructure.adapter.web.presenter;

import lombok.RequiredArgsConstructor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

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
        final ModelAndView errorMav = new ModelAndView("error", Map.of("errorMessage", e.getMessage()));
        try {
            dispatcher.render(errorMav, httpRequest, httpResponse);
        } catch (Exception ex) {
            throw new RuntimeException(e);
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
            httpResponse.sendRedirect(httpResponse.encodeRedirectURL(uri));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
