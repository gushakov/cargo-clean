package com.github.cargoclean.infrastructure.adapter.web.editlocations;

import com.github.cargoclean.core.usecase.editlocation.EditLocationsInputPort;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
public class EditLocationsController {

    private final ApplicationContext appContext;

    @RequestMapping("/addNewLocation")
    @ResponseBody
    public void showAddNewLocationForm() {

        useCase().prepareAddNewLocationView();
    }

    @RequestMapping(method = RequestMethod.POST, value = "/registerNewLocation")
    @ResponseBody
    public void registerNewLocation(@ModelAttribute AddLocationForm addLocationForm) {

        useCase().registerNewLocation(addLocationForm.getUnLocode(),
                addLocationForm.getLocation(),
                addLocationForm.getRegion());
    }

    @RequestMapping("/updateLocation")
    @ResponseBody
    public void showUpdateLocationForm() {

        useCase().prepareUpdateLocationView();
    }


    private EditLocationsInputPort useCase() {
        return appContext.getBean(EditLocationsInputPort.class);
    }

}
