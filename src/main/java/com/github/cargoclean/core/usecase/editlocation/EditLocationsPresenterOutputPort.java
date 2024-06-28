package com.github.cargoclean.core.usecase.editlocation;

import com.github.cargoclean.core.model.location.Location;
import com.github.cargoclean.core.port.ErrorHandlingPresenterOutputPort;

import java.util.List;

public interface EditLocationsPresenterOutputPort extends ErrorHandlingPresenterOutputPort {

    void presentAddNewLocationForm();

    void presentResultOfSuccessfulRegistrationOfNewLocation(Location location);

    void presentUpdateLocationFormForLocationSelection(List<Location> locations);

    void presentUpdateLocationFormWithSelectedLocation(Location location);

    void presentResultOfSuccessfulRegistrationOfUpdatedLocation(Location location);
}
