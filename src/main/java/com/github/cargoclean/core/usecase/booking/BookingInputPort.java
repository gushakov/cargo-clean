package com.github.cargoclean.core.usecase.booking;

import com.github.cargoclean.core.model.location.Location;

import java.util.Date;

public interface BookingInputPort {

    void prepareNewCargoBooking();

    void bookCargo(Location origin, Location destination, Date deliveryDeadline);
}
