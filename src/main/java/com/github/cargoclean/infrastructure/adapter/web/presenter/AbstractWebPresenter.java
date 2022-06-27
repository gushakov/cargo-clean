package com.github.cargoclean.infrastructure.adapter.web.presenter;

import lombok.RequiredArgsConstructor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * Presents Thymeleaf views (pages) by delegating to {@link LocalDispatcherServlet}.
 * Concrete Presenters should override providing {@code Response Model} and
 * the name of the view to render.
 *
 * @see LocalDispatcherServlet
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
}
