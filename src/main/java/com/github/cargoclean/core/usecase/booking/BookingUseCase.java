package com.github.cargoclean.core.usecase.booking;

import com.github.cargoclean.core.GenericCargoError;
import com.github.cargoclean.core.model.UtcDateTime;
import com.github.cargoclean.core.model.cargo.*;
import com.github.cargoclean.core.model.location.Location;
import com.github.cargoclean.core.model.location.UnLocode;
import com.github.cargoclean.core.port.operation.persistence.PersistenceGatewayOutputPort;
import com.github.cargoclean.core.port.operation.security.SecurityOutputPort;
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

        } catch (GenericCargoError e) {

            /*
                Point of interest:
                -----------------
                As a rule of thumb, we handle only "GenericCargoError"
                or its subclasses in use cases. All other runtime errors
                should be allowed to bubble up so that we can readily
                identify them during the development phase.
                Also, we handle specific errors here only if it requires
                interacting with output ports (secondary adapters). If
                we only need to differentiate presentation, depending on
                the exact type of errors, we let it be handled in the
                presenter.
             */

            presenter.presentError(e);
            return;
        }

        // if everything is OK, present the list of locations
        presenter.presentNewCargoBookingView(locations);

    }

    /*
        Point of interest:
        -----------------
        Notice "javax.transaction.Transactional" demarcation
        on the entire method (the use case). This is a principle
        deviation of Clean DDD from the classical DDD approach
        where transactional boundary is around each aggregate.
        This makes each use case operation a consistent one,
        but requires some through design with respect to
        the performance.
     */

    @Transactional
    @Override
    public void bookCargo(String originUnLocode, String destinationUnLocode, Date deliveryDeadline) {

        final TrackingId trackingId;
        try {
            securityOps.assertThatUserIsAgent();

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


        } catch (GenericCargoError e) {

            /*
                Point of interest:
                -----------------
                Notice how we are calling the gateway here to
                roll back the transaction. And not from the
                presenter to remain within CA paradigm: output
                ports are only called from a use case.
             */

            gatewayOps.rollback();
            presenter.presentError(e);
            return;
        }

        presenter.presentResultOfNewCargoBooking(trackingId);
    }
}
