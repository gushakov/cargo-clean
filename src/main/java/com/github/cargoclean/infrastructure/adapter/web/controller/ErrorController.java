package com.github.cargoclean.infrastructure.adapter.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ErrorController {

    @RequestMapping("/error")
    public String onError(@RequestParam(required = false) String errorMessage, Model model){

        if (errorMessage != null){
            model.addAttribute("errorMessage", errorMessage);
        }

        return "error";
    }

}
