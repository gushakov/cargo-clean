package com.github.cargoclean.core.usecase.consignment;

import com.github.cargoclean.core.model.cargo.TrackingId;
import com.github.cargoclean.core.model.consignment.ConsignmentId;
import com.github.cargoclean.core.port.ErrorHandlingPresenterOutputPort;

public interface ConsignmentPresenterOutputPort extends ErrorHandlingPresenterOutputPort {
    void presentConsignmentAdded(ConsignmentId consignmentId, TrackingId cargoTrackingId);

    void presentErrorWhenConsignmentCouldNotBeAdded(ConsignmentId consignmentId, TrackingId cargoTrackingId, String message);

    void presentConsignmentEntryForm(String cargoTrackingId);
}