package com.github.cargoclean.core.usecase.handling;

import com.github.cargoclean.core.model.InvalidDomainObjectError;
import com.github.cargoclean.core.model.UtcDateTime;
import com.github.cargoclean.core.model.cargo.Cargo;
import com.github.cargoclean.core.model.cargo.TrackingId;
import com.github.cargoclean.core.model.handling.EventId;
import com.github.cargoclean.core.model.handling.HandlingEvent;
import com.github.cargoclean.core.model.handling.HandlingEventType;
import com.github.cargoclean.core.model.handling.HandlingHistory;
import com.github.cargoclean.core.model.location.UnLocode;
import com.github.cargoclean.core.model.voyage.VoyageNumber;
import com.github.cargoclean.core.port.events.EventDispatcherOutputPort;
import com.github.cargoclean.core.port.persistence.PersistenceGatewayOutputPort;
import com.github.cargoclean.core.port.security.SecurityOutputPort;
import com.github.cargoclean.core.port.transaction.TransactionOperationsOutputPort;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.Instant;
import java.util.Optional;

@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class HandlingUseCase implements HandlingInputPort {

    HandlingPresenterOutputPort presenter;

    SecurityOutputPort securityOps;

    PersistenceGatewayOutputPort gatewayOps;

    EventDispatcherOutputPort eventsOps;

    TransactionOperationsOutputPort txOps;

    @Override
    public void recordHandlingEvent(String voyageNumberStr, String locationStr, String cargoIdStr,
                                    Instant completionTime, HandlingEventType type) {

        try {

            // make sure user is a manager
            securityOps.assertThatUserIsManager();

            EventId eventId = gatewayOps.nextEventId();

            // parse identifiers and create value objects: outside the transaction
            // since validation does not require a consistency boundary
            VoyageNumber voyageNumber;
            UnLocode location;
            TrackingId cargoId;
            UtcDateTime completionDateTime;
            try {
                voyageNumber = Optional.ofNullable(voyageNumberStr)
                        .map(VoyageNumber::of).orElse(null);
                location = UnLocode.of(locationStr);
                cargoId = TrackingId.of(cargoIdStr);
                completionDateTime = UtcDateTime.of(completionTime);
            } catch (InvalidDomainObjectError e) {
                presenter.presentInvalidParametersError(e);
                return;
            }

            // construct new handling event
            HandlingEvent handlingEvent = HandlingEvent.builder()
                    .eventId(eventId)
                    .voyageNumber(voyageNumber)
                    .location(location)
                    .cargoId(cargoId)
                    .completionTime(completionDateTime)
                    .registrationTime(UtcDateTime.now())
                    .type(type)
                    .build();

            /*
                Point of interest:
                -----------------
                Only persistence and event dispatch are wrapped in a transaction:
                a "poor man's outbox" pattern ensuring that the handling event
                is recorded and the domain event is dispatched atomically.
                Event handling is performed synchronously in the same thread
                via "TransactionalEventListener" which guarantees that events
                are processed only after this transaction commits.
             */

            txOps.doInTransaction(() -> {
                gatewayOps.recordHandlingEvent(handlingEvent);
                eventsOps.dispatch(handlingEvent);
                txOps.doAfterCommit(() -> presenter.presentResultOfRegisteringHandlingEvent(cargoId, handlingEvent));
            });

        } catch (Exception e) {
            presenter.presentError(e);
        }

    }

    @Override
    public void updateDeliveryAfterHandlingActivity(String cargoIdStr) {

        try {

            /*
                Point of interest:
                -----------------
                This use case will be executed by the system from the event handling adapter,
                so we do not need to assert security.
             */

            TrackingId trackingId = TrackingId.of(cargoIdStr);

            // retrieve cargo
            Cargo cargo = gatewayOps.obtainCargoByTrackingId(trackingId);

            // retrieve handling history of cargo
            HandlingHistory handlingHistory = gatewayOps.handlingHistory(trackingId);

            // update cargo to reflect handling history
            Cargo updatedCargo = cargo.updateDeliveryProgress(handlingHistory);

            // save cargo aggregate
            gatewayOps.saveCargo(updatedCargo);
        } catch (Exception e) {
            presenter.presentError(e);
        }
    }
}
