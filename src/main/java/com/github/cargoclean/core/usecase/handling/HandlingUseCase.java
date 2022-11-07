package com.github.cargoclean.core.usecase.handling;

import com.github.cargoclean.core.GenericCargoError;
import com.github.cargoclean.core.model.InvalidDomainObjectError;
import com.github.cargoclean.core.model.UtcDateTime;
import com.github.cargoclean.core.model.cargo.Cargo;
import com.github.cargoclean.core.model.cargo.TrackingId;
import com.github.cargoclean.core.model.handling.HandlingEvent;
import com.github.cargoclean.core.model.handling.HandlingEventType;
import com.github.cargoclean.core.model.handling.HandlingHistory;
import com.github.cargoclean.core.model.location.UnLocode;
import com.github.cargoclean.core.model.voyage.VoyageNumber;
import com.github.cargoclean.core.port.operation.PersistenceGatewayOutputPort;
import com.github.cargoclean.core.port.presenter.handling.HandlingPresenterOutputPort;
import lombok.RequiredArgsConstructor;

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.Optional;

@RequiredArgsConstructor
public class HandlingUseCase implements HandlingInputPort {

    private final HandlingPresenterOutputPort presenter;

    private final PersistenceGatewayOutputPort gatewayOps;

    @Transactional
    @Override
    public void recordHandlingEvent(String voyageNumberStr, String locationStr, String cargoIdStr,
                                    Instant completionTime, HandlingEventType type) {

        TrackingId cargoId;
        HandlingEvent handlingEvent;
        try {

            // parse identifiers
            VoyageNumber voyageNumber = Optional.ofNullable(voyageNumberStr)
                    .map(VoyageNumber::of).orElse(null);
            UnLocode location = UnLocode.of(locationStr);
            cargoId = TrackingId.of(cargoIdStr);

            // construct and validate new handling event
            try {
                handlingEvent = HandlingEvent.builder()
                        .eventId(gatewayOps.nextEventId())
                        .voyageNumber(voyageNumber)
                        .location(location)
                        .cargoId(cargoId)
                        .completionTime(UtcDateTime.of(completionTime))
                        .registrationTime(UtcDateTime.now())
                        .type(type)
                        .build();
            } catch (InvalidDomainObjectError e) {
                presenter.presentInvalidParametersError(e);
                return;
            }

            // record handling event
            gatewayOps.recordHandlingEvent(handlingEvent);

        } catch (GenericCargoError e) {
            presenter.presentError(e);
            return;
        }

        presenter.presentResultOfRegisteringHandlingEvent(cargoId, handlingEvent);
    }

    @Transactional
    @Override
    public void updateDeliveryAfterHandlingActivity(String cargoIdStr) {

        try {

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
            presenter.presentError(e);
        }

    }
}
