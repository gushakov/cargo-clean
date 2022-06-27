package com.github.cargoclean.core.usecase.booking;

import com.github.cargoclean.core.model.location.Location;
import com.github.cargoclean.core.port.operation.PersistenceGatewayOutputPort;
import com.github.cargoclean.core.port.presenter.booking.BookingPresenterOutputPort;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class BookingUseCase implements BookingInputPort {

    // here is our Presenter
    private final BookingPresenterOutputPort presenter;

    // here is our gateway
    private final PersistenceGatewayOutputPort gatewayOps;

    @Override
    public void prepareNewCargoBooking() {

        try {
            // retrieve all locations from the gateway

            final List<Location> locations = gatewayOps.allLocations();

            // present the view where we can enter new cargo information
            presenter.presentNewCargoBookingView(locations);

        } catch (Exception e) {
            // if anything went wrong: present the error
            presenter.presentError(e);
        }

    }
}
