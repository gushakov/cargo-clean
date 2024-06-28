package com.github.cargoclean.infrastructure.adapter.web.editlocations;

import com.github.cargoclean.core.usecase.editlocation.EditLocationsInputPort;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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
    public void showUpdateLocationForm(@RequestParam(name = "selectedUnlocode", required = false) String selectedUnlocode) {
        if (selectedUnlocode == null) {
            useCase().prepareUpdateLocationView();
        } else {
            useCase().selectLocationForUpdate(selectedUnlocode);
        }
    }

    @RequestMapping(method = RequestMethod.POST, value = "/registerUpdatedLocation")
    @ResponseBody
    public void registerUpdatedLocation(@ModelAttribute UpdateLocationForm updateLocationForm) {
        useCase().registerUpdatedLocation(updateLocationForm.getSelectedUnlocode(),
                updateLocationForm.getCity());
    }

    private EditLocationsInputPort useCase() {
        return appContext.getBean(EditLocationsInputPort.class);
    }

}
