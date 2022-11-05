package com.github.cargoclean.core.usecase.booking;


import com.github.cargoclean.core.model.UtcDateTime;

public interface BookingInputPort {

    void prepareNewCargoBooking();

    void bookCargo(String originUnLocode, String destinationUnLocode, UtcDateTime deliveryDeadline);
}
