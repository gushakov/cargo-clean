package com.github.cargoclean.core.usecase.booking;

import com.github.cargoclean.core.model.cargo.Cargo;
import com.github.cargoclean.core.model.cargo.Delivery;
import com.github.cargoclean.core.model.cargo.TrackingId;
import com.github.cargoclean.core.model.cargo.TransportStatus;
import com.github.cargoclean.core.model.location.Location;
import com.github.cargoclean.core.model.location.UnLocode;
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

            List<String> allUnlocodes = locations.stream().map(Location::getUnLocode).map(UnLocode::getCode).toList();

            presenter.presentNewCargoBookingView(allUnlocodes);

        } catch (Exception e) {
            // if anything went wrong: present the error
            presenter.presentError(e);
        }

    }

    @Transactional
    @Override
    public void bookCargo(String originUnLocode, String destinationUnLocode, Date deliveryDeadline) {

        // This is where we are going to create new Cargo model object (aggregate)
        // with all necessary related objects (entities).

        try {
            Location origin = gatewayOps.obtainLocationByUnLocode(UnLocode.of(originUnLocode));

            final TrackingId trackingId = gatewayOps.nextTrackingId();
            final Cargo cargo = Cargo.builder()
                    .origin(origin)
                    .trackingId(trackingId)
                    .delivery(Delivery.builder()
                            .transportStatus(TransportStatus.NOT_RECEIVED)
                            .build())
                    .build();

            // Here we can validate Cargo aggregate if needed.

            gatewayOps.saveCargo(cargo);

            log.debug("[Booking] Booked new cargo: {}", cargo.getTrackingId());

            presenter.presentResultOfNewCargoBooking(trackingId);
        } catch (Exception e) {
            presenter.presentError(e);
        }
    }
}
