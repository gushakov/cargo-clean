package com.github.cargoclean.core.usecase.booking;

import com.github.cargoclean.core.CargoSecurityError;
import com.github.cargoclean.core.GenericCargoError;
import com.github.cargoclean.core.model.UtcDateTime;
import com.github.cargoclean.core.model.cargo.*;
import com.github.cargoclean.core.model.location.Location;
import com.github.cargoclean.core.model.location.UnLocode;
import com.github.cargoclean.core.port.operation.PersistenceGatewayOutputPort;
import com.github.cargoclean.core.port.operation.SecurityOutputPort;
import com.github.cargoclean.core.port.presenter.booking.BookingPresenterOutputPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.transaction.Transactional;
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

    private final SecurityOutputPort securityOps;

    // here is our gateway
    private final PersistenceGatewayOutputPort gatewayOps;

    @Override
    public void prepareNewCargoBooking() {

        final List<Location> locations;
        try {

            // check if the user has the role of agent
            securityOps.assertThatUserIsAgent();

            // retrieve all locations from the gateway

            locations = gatewayOps.allLocations();

        }
        catch (CargoSecurityError e){
            // handle security error
            presenter.presentSecurityError(e);
            return;
        }
        catch (GenericCargoError e) {
            // if anything else went wrong: present the error and return
            presenter.presentError(e);
            return;
        }

        // if everything is OK, present the list of locations
        presenter.presentNewCargoBookingView(locations);

    }

    @Transactional
    @Override
    public void bookCargo(String originUnLocode, String destinationUnLocode, Date deliveryDeadline) {

        final TrackingId trackingId;
        try {

            /*
                Point of interest:
                -----------------
                Out value objects and entities will throw "InvalidDomainObjectError"
                for any invalid input during construction. This is a part of our
                validation strategy where validation is handled by the model itself.
             */

            UnLocode origin = UnLocode.of(originUnLocode);
            UnLocode destination = UnLocode.of(destinationUnLocode);
            UtcDateTime deliveryDeadlineDateTime = UtcDateTime.of(deliveryDeadline);

            RouteSpecification routeSpecification = RouteSpecification.builder()
                    .origin(origin)
                    .destination(destination)
                    .arrivalDeadline(deliveryDeadlineDateTime)
                    .build();

            // we create new Cargo object

            trackingId = gatewayOps.nextTrackingId();
            final Cargo cargo = Cargo.builder()
                    .origin(origin)
                    .trackingId(trackingId)
                    .delivery(Delivery.builder()
                            .transportStatus(TransportStatus.NOT_RECEIVED)
                            .routingStatus(RoutingStatus.NOT_ROUTED)
                            .misdirected(false)
                            .build())
                    .routeSpecification(routeSpecification)
                    .build();

            // save Cargo to the database
            gatewayOps.saveCargo(cargo);

            log.debug("[Booking] Booked new cargo: {}", cargo.getTrackingId());


        } catch (Exception e) {
            gatewayOps.rollback();
            presenter.presentError(e);
            return;
        }

        presenter.presentResultOfNewCargoBooking(trackingId);
    }
}
