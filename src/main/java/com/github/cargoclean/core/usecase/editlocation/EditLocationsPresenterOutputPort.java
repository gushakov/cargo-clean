package com.github.cargoclean.core.usecase.editlocation;

import com.github.cargoclean.core.model.location.Location;
import com.github.cargoclean.core.port.ErrorHandlingPresenterOutputPort;

import java.util.List;

public interface EditLocationsPresenterOutputPort extends ErrorHandlingPresenterOutputPort {

    void presentAddNewLocationForm();

    void presentResultOfSuccessfulRegistrationOfNewLocation(Location location);

    void presentUpdateLocationForm(List<Location> locations);
}
