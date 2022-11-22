package com.github.cargoclean.core.port.presenter.editlocations;

import com.github.cargoclean.core.port.error.ErrorHandlingPresenterOutputPort;

public interface EditLocationsPresenterOutputPort extends ErrorHandlingPresenterOutputPort {

    void presentAddNewLocationForm();

}
