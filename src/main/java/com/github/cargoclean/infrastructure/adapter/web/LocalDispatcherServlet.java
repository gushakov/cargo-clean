package com.github.cargoclean.infrastructure.adapter.web;

import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/*
    We are overriding the default DispatcherServlet so that we can have
    access to "render" (protected) method.
    Based on this answer: https://stackoverflow.com/a/68536242
 */

public class LocalDispatcherServlet extends DispatcherServlet {

    /*
        We are calling this method from the Presenter ourselves.
     */
    @Override
    protected void render(ModelAndView mv, HttpServletRequest request, HttpServletResponse response) throws Exception {
        super.render(mv, request, response);
    }

}
