package com.github.cargoclean.core.usecase.booking;

import java.util.Date;

public interface BookingInputPort {

    void prepareNewCargoBooking();

    void bookCargo(String originUnLocode, String destinationUnLocode, Date deliveryDeadline);
}
