package com.github.cargoclean.core.usecase.booking;

import com.github.cargoclean.core.model.cargo.TrackingId;
import com.github.cargoclean.core.model.location.Location;
import com.github.cargoclean.core.port.operation.PersistenceGatewayOutputPort;
import com.github.cargoclean.core.port.presenter.booking.BookingPresenterOutputPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
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

            /*
                Present the view where we can enter new cargo information.
                We are passing an immutable list of locations as is (value
                objects) to be used by the presenter. In general, according
                to CA, we may need to create a Response Model (DTO) object
                instead.
             */
            presenter.presentNewCargoBookingView(locations);

        } catch (Exception e) {
            // if anything went wrong: present the error
            presenter.presentError(e);
        }

    }

    @Override
    public void bookCargo(Location origin, Location destination, Date deliveryDeadline) {
        log.debug("Actually book new cargo here...");

        // TODO: create new Cargo, save new Cargo in the database

        final TrackingId trackingId = TrackingId.builder()
                .id("ABCDE1")
                .build();

        presenter.presentResultOfNewCargoBooking(trackingId);
    }
}
