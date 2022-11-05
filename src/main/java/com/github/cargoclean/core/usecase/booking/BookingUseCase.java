package com.github.cargoclean.core.usecase.booking;

import com.github.cargoclean.core.model.UtcDateTime;
import com.github.cargoclean.core.model.cargo.*;
import com.github.cargoclean.core.model.location.Location;
import com.github.cargoclean.core.model.location.UnLocode;
import com.github.cargoclean.core.port.operation.PersistenceGatewayOutputPort;
import com.github.cargoclean.core.port.presenter.booking.BookingPresenterOutputPort;
import com.github.cargoclean.core.validator.InvalidDomainObjectError;
import com.github.cargoclean.core.validator.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.transaction.Transactional;
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

    // validation service
    private final Validator validator;

    // here is our gateway
    private final PersistenceGatewayOutputPort gatewayOps;

    @Override
    public void prepareNewCargoBooking() {

        final List<Location> locations;
        try {
            // retrieve all locations from the gateway

            locations = validator.validate(gatewayOps.allLocations());

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
    public void bookCargo(String originUnLocode, String destinationUnLocode, UtcDateTime deliveryDeadline) {

        final TrackingId trackingId;
        try {

            // we validate the inputs to the use case
            if (deliveryDeadline == null) {
                throw new InvalidDomainObjectError("arrival deadline must not be null");
            }

            UnLocode origin = UnLocode.of(originUnLocode);
            UnLocode destination = UnLocode.of(destinationUnLocode);

            // we create new Cargo object

            trackingId = validator.validate(gatewayOps.nextTrackingId());
            final Cargo cargo = Cargo.builder()
                    .origin(origin)
                    .trackingId(trackingId)
                    .delivery(Delivery.builder()
                            .transportStatus(TransportStatus.NOT_RECEIVED)
                            .build())
                    .routeSpecification(RouteSpecification.builder()
                            .origin(origin)
                            .destination(destination)
                            .arrivalDeadline(deliveryDeadline)
                            .build())
                    .build();

            // validate newly constructed Cargo domain object
            validator.validate(cargo);

            // save Cargo to the database
            gatewayOps.saveCargo(cargo);

            log.debug("[Booking] Booked new cargo: {}", cargo.getTrackingId());


        } catch (Exception e) {
            presenter.presentError(e);
            return;
        }

        presenter.presentResultOfNewCargoBooking(trackingId);
    }
}
