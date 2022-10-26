package com.github.cargoclean.core.usecase.handling;

import com.github.cargoclean.core.GenericCargoError;
import com.github.cargoclean.core.model.cargo.TrackingId;
import com.github.cargoclean.core.model.handling.HandlingEventType;
import com.github.cargoclean.core.model.location.UnLocode;
import com.github.cargoclean.core.model.voyage.VoyageNumber;
import com.github.cargoclean.core.port.operation.PersistenceGatewayOutputPort;
import com.github.cargoclean.core.port.presenter.handling.HandlingPresenterOutputPort;
import lombok.RequiredArgsConstructor;

import java.time.Instant;

@RequiredArgsConstructor
public class HandlingUseCase implements HandlingInputPort {

    private final HandlingPresenterOutputPort presenter;

    private final PersistenceGatewayOutputPort gatewayOps;

    @Override
    public void recordHandlingEvent(String voyageNumberStr, String locationStr, String cargoIdStr, Instant completionTime,
                                    Instant registrationTime, HandlingEventType type) {

        try {

            // parse identifiers
            VoyageNumber voyageNumber = VoyageNumber.of(voyageNumberStr);
            UnLocode location = UnLocode.of(locationStr);
            TrackingId cargoId = TrackingId.of(cargoIdStr);

        }
        catch (GenericCargoError e){
            presenter.presentError(e);
            return;
        }

    }
}
