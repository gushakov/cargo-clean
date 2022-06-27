package com.github.cargoclean.infrastructure.adapter.web;

import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/*
    Notes:
    -----

    1.  We are overriding the default DispatcherServlet so that we can have
    access to "render" (protected) method.

    References:
    ----------

    1.  Access DispatcherServlet: https://bit.ly/3NnIAcg
 */

public class LocalDispatcherServlet extends DispatcherServlet {

    /*
        We will be calling this method from Presenters.
     */
    @Override
    protected void render(ModelAndView mv, HttpServletRequest request, HttpServletResponse response) throws Exception {
        super.render(mv, request, response);
    }

}
