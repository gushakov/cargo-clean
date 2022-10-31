package com.github.cargoclean.core.usecase.handling;

import com.github.cargoclean.core.GenericCargoError;
import com.github.cargoclean.core.model.Constants;
import com.github.cargoclean.core.model.cargo.Cargo;
import com.github.cargoclean.core.model.cargo.TrackingId;
import com.github.cargoclean.core.model.handling.HandlingEvent;
import com.github.cargoclean.core.model.handling.HandlingEventType;
import com.github.cargoclean.core.model.handling.HandlingHistory;
import com.github.cargoclean.core.model.location.UnLocode;
import com.github.cargoclean.core.model.voyage.VoyageNumber;
import com.github.cargoclean.core.port.operation.PersistenceGatewayOutputPort;
import com.github.cargoclean.core.port.presenter.handling.HandlingPresenterOutputPort;
import com.github.cargoclean.core.validator.InvalidDomainObjectError;
import com.github.cargoclean.core.validator.Validator;
import lombok.RequiredArgsConstructor;

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.Optional;

@RequiredArgsConstructor
public class HandlingUseCase implements HandlingInputPort {

    private final HandlingPresenterOutputPort presenter;

    private final Validator validator;

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
                handlingEvent = validator.validate(HandlingEvent.builder()
                        .eventId(gatewayOps.nextEventId())
                        .voyageNumber(voyageNumber)
                        .location(location)
                        .cargoId(cargoId)
                        .completionTime(completionTime.atZone(Constants.DEFAULT_ZONE_ID))
                        .registrationTime(Instant.now().atZone(Constants.DEFAULT_ZONE_ID))
                        .type(type)
                        .build());
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
