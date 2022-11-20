package com.github.cargoclean.core.usecase.handling;

import com.github.cargoclean.core.GenericCargoError;
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
import com.github.cargoclean.core.port.operation.PersistenceGatewayOutputPort;
import com.github.cargoclean.core.port.operation.security.SecurityOutputPort;
import com.github.cargoclean.core.port.presenter.handling.HandlingPresenterOutputPort;
import lombok.RequiredArgsConstructor;

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.Optional;

@RequiredArgsConstructor
public class HandlingUseCase implements HandlingInputPort {

    private final HandlingPresenterOutputPort presenter;

    private final SecurityOutputPort securityOps;

    private final PersistenceGatewayOutputPort gatewayOps;

    @Transactional
    @Override
    public void recordHandlingEvent(String voyageNumberStr, String locationStr, String cargoIdStr,
                                    Instant completionTime, HandlingEventType type) {

        TrackingId cargoId;
        HandlingEvent handlingEvent;
        EventId eventId;
        VoyageNumber voyageNumber;
        UnLocode location;
        UtcDateTime completionDateTime;
        try {

            // make sure user is a manager
            securityOps.assertThatUserIsManager();

            eventId = gatewayOps.nextEventId();

            // parse identifiers and create value objects
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
            handlingEvent = HandlingEvent.builder()
                    .eventId(eventId)
                    .voyageNumber(voyageNumber)
                    .location(location)
                    .cargoId(cargoId)
                    .completionTime(completionDateTime)
                    .registrationTime(UtcDateTime.now())
                    .type(type)
                    .build();

            // record handling event
            gatewayOps.recordHandlingEvent(handlingEvent);

        } catch (GenericCargoError e) {
            gatewayOps.rollback();
            presenter.presentError(e);
            return;
        }

        presenter.presentResultOfRegisteringHandlingEvent(cargoId, handlingEvent);
    }

    @Transactional
    @Override
    public void updateDeliveryAfterHandlingActivity(String cargoIdStr) {

        try {

            securityOps.assertThatUserIsManager();

            TrackingId trackingId = TrackingId.of(cargoIdStr);

            // retrieve cargo
            Cargo cargo = gatewayOps.obtainCargoByTrackingId(trackingId);

            // retrieve handling history of cargo
            HandlingHistory handlingHistory = gatewayOps.handlingHistory(trackingId);

            // update cargo to reflect handling history
            Cargo updatedCargo = cargo.updateDeliveryProgress(handlingHistory);

            // save cargo aggregate
            gatewayOps.saveCargo(updatedCargo);
        } catch (GenericCargoError e) {
            gatewayOps.rollback();
            presenter.presentError(e);
        }
    }
}
