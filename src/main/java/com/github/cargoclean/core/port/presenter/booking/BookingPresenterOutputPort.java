package com.github.cargoclean.core.port.presenter.booking;

import com.github.cargoclean.core.model.cargo.TrackingId;
import com.github.cargoclean.core.port.error.ErrorHandlingPresenterOutputPort;

import java.util.List;

public interface BookingPresenterOutputPort extends ErrorHandlingPresenterOutputPort {

    void presentNewCargoBookingView(List<String> allUnLocodes);

    void presentResultOfNewCargoBooking(TrackingId trackingId);
}
