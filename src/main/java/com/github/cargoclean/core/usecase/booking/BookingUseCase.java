package com.github.cargoclean.core.usecase.booking;

import com.github.cargoclean.core.model.Constants;
import com.github.cargoclean.core.model.cargo.*;
import com.github.cargoclean.core.model.location.Location;
import com.github.cargoclean.core.model.location.UnLocode;
import com.github.cargoclean.core.port.operation.PersistenceGatewayOutputPort;
import com.github.cargoclean.core.port.presenter.booking.BookingPresenterOutputPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;

/*
    References:
    ----------

    1.  Convert Date to ZonedDateTime: https://stackoverflow.com/questions/25376242/java8-java-util-date-conversion-to-java-time-zoneddatetime
 */

@RequiredArgsConstructor
@Slf4j
public class BookingUseCase implements BookingInputPort {

    // here is our Presenter
    private final BookingPresenterOutputPort presenter;

    // here is our gateway
    private final PersistenceGatewayOutputPort gatewayOps;

    @Override
    public void prepareNewCargoBooking() {

        final List<Location> locations;
        try {
            // retrieve all locations from the gateway

            locations = gatewayOps.allLocations();

        } catch (Exception e) {
            // if anything went wrong: present the error and return
            presenter.presentError(e);
            return;
        }

        // if everything is OK, present the list of locations
        presenter.presentNewCargoBookingView(locations);

    }

    @Transactional
    @Override
    public void bookCargo(String originUnLocode, String destinationUnLocode, Date deliveryDeadline) {

        // This is where we are going to create new Cargo model object (aggregate)
        // with all necessary related objects (entities).
        final TrackingId trackingId;
        try {
            Location origin = gatewayOps.obtainLocationByUnLocode(UnLocode.of(originUnLocode));
            Location destination = gatewayOps.obtainLocationByUnLocode(UnLocode.of(destinationUnLocode));

            trackingId = gatewayOps.nextTrackingId();
            final Cargo cargo = Cargo.builder()
                    .origin(origin)
                    .trackingId(trackingId)
                    .delivery(Delivery.builder()
                            .transportStatus(TransportStatus.NOT_RECEIVED)
                            .build())
                    .routeSpecification(RouteSpecification.builder()
                            .origin(origin)
                            .destination(destination)
                            .arrivalDeadline(ZonedDateTime.ofInstant(deliveryDeadline.toInstant(),
                                    Constants.DEFAULT_ZONE_ID))
                            .build())
                    .build();

            // Here we can validate Cargo aggregate if needed.

            gatewayOps.saveCargo(cargo);

            log.debug("[Booking] Booked new cargo: {}", cargo.getTrackingId());


        } catch (Exception e) {
            presenter.presentError(e);
            return;
        }

        presenter.presentResultOfNewCargoBooking(trackingId);
    }
}
