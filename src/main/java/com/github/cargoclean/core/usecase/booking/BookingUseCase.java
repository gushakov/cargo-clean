package com.github.cargoclean.core.usecase.booking;

import com.github.cargoclean.core.model.cargo.Cargo;
import com.github.cargoclean.core.model.cargo.Delivery;
import com.github.cargoclean.core.model.cargo.TrackingId;
import com.github.cargoclean.core.model.cargo.TransportStatus;
import com.github.cargoclean.core.model.location.Location;
import com.github.cargoclean.core.port.operation.PersistenceGatewayOutputPort;
import com.github.cargoclean.core.port.presenter.booking.BookingPresenterOutputPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.transaction.Transactional;
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

    @Transactional
    @Override
    public void bookCargo(Location origin, Location destination, Date deliveryDeadline) {

        // Create new Cargo object here, for now we are just
        // creating some related objects manually (Delivery).

        final TrackingId trackingId = gatewayOps.nextTrackingId();
        final Cargo cargo = Cargo.builder()
                .origin(origin)
                .trackingId(trackingId)
                .delivery(Delivery.builder()
                        .transportStatus(TransportStatus.NOT_RECEIVED)
                        .build())
                .build();

        gatewayOps.save(cargo);

        log.debug("[Booking] Booked new cargo: {}", cargo.getTrackingId());

        presenter.presentResultOfNewCargoBooking(trackingId);
    }
}
