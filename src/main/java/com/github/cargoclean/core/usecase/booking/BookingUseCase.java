package com.github.cargoclean.core.usecase.booking;

import com.github.cargoclean.core.model.UtcDateTime;
import com.github.cargoclean.core.model.cargo.*;
import com.github.cargoclean.core.model.location.Location;
import com.github.cargoclean.core.model.location.UnLocode;
import com.github.cargoclean.core.port.persistence.PersistenceGatewayOutputPort;
import com.github.cargoclean.core.port.security.SecurityOutputPort;
import com.github.cargoclean.core.port.transaction.TransactionOperationsOutputPort;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.List;

/*
    References:
    ----------

    1.  Convert Date to ZonedDateTime: https://stackoverflow.com/questions/25376242/java8-java-util-date-conversion-to-java-time-zoneddatetime
 */

@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
@Slf4j
public class BookingUseCase implements BookingInputPort {

    // here is our Presenter
    BookingPresenterOutputPort presenter;

    SecurityOutputPort securityOps;

    // here is our gateway
    PersistenceGatewayOutputPort gatewayOps;

    TransactionOperationsOutputPort txOps;

    @Override
    public void prepareNewCargoBooking() {

        try {

            txOps.doInTransaction(true, () -> {
                // check if the user has the role of agent
                securityOps.assertThatUserIsAgent();

                // retrieve all locations from the gateway

                final List<Location> locations = gatewayOps.allLocations();

                // if everything is OK, present the list of locations
                txOps.doAfterCommit(() -> presenter.presentNewCargoBookingView(locations));

            });

        } catch (Exception e) {

            /*
                Point of interest:
                -----------------
                UPDATE: 14.04.2024
                We catch ALL exceptions here. We differentiate the
                presentation for known exceptions from "GenericCargoError"
                hierarchy from the unknown (runtime) exceptions by calling
                specific presentation methods, if needed.
                Also, we handle specific errors here only if it requires
                interacting with output ports (secondary adapters). If
                we only need to differentiate presentation, depending on
                the exact type of errors, we let it be handled in the
                presenter.
             */

            presenter.presentError(e);
        }

    }

    /*
        Point of interest:
        -----------------
        UPDATE: 24.05.2024
        We are no longer using "javax.transaction.Transactional" annotations around
        the use cases (methods). Instead, we are creating a transaction around each
        individual method of the gateway. We also provide a method "doInTransaction()"
        in the gateway which allows us to control the transactional (consistency)
        boundary of the code in each use case with more granularity. In particular,
        with this approach we can call Presenter outside the transactional boundary.
     */
    @Override
    public void bookCargo(String originUnLocode, String destinationUnLocode, Date deliveryDeadline) {

        try {


            final TrackingId trackingId;

            securityOps.assertThatUserIsAgent();

            /*
                Point of interest:
                -----------------
                Our value objects and entities will throw "InvalidDomainObjectError"
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

            /*
                Point of interest:
                -----------------
                Use case is responsible for its own presentation. We are
                not returning anything to the controller.
            */
            presenter.presentResultOfNewCargoBooking(trackingId);

        } catch (Exception e) {
            presenter.presentError(e);
        }

    }
}
