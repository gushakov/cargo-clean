package com.github.cargoclean.infrastructure.adapter.web;

/*
    References:
    ----------

    1.  Error handling in Spring MVC: https://spring.io/blog/2013/11/01/exception-handling-in-spring-mvc
 */

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Common parent for web controllers: declares default error handling for web interface (redirect to an
 * error view).
 *
 * @see AbstractRestController
 */
@Slf4j
public abstract class AbstractWebController {
    /*
        This will be called for any error which was not handled through
        presenters.
     */

    @ExceptionHandler
    public void redirectOnError(HttpServletRequest request, HttpServletResponse response, Exception exception) {

        request.getSession().setAttribute("errorMessage", exception.getMessage());
        try {
            response.sendRedirect("/error");
        } catch (IOException e) {
            log.error("[Error handling] Cannot redirect to error handling endpoint: %s"
                    .formatted(exception.getMessage()));
        }
    }

}
