package com.github.cargoclean.infrastructure.adapter.web.editlocations;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
public class EditLocationsController {

    private final ApplicationContext appContext;

    @RequestMapping("/addNewLocation")
    public void showAddNewLocationForm(){

    }

}
