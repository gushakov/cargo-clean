package com.github.cargoclean.core.port.presenter.booking;

import com.github.cargoclean.core.model.location.Location;
import com.github.cargoclean.core.port.error.ErrorHandlingPresenterOutputPort;

import java.util.List;

public interface BookingPresenterOutputPort extends ErrorHandlingPresenterOutputPort {

    void presentNewCargoBookingView(List<Location> locations);

}
