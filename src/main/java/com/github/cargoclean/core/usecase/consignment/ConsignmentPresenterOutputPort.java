package com.github.cargoclean.core.usecase.consignment;

import com.github.cargoclean.core.port.ErrorHandlingPresenterOutputPort;

public interface ConsignmentPresenterOutputPort extends ErrorHandlingPresenterOutputPort {
    void presentConsignmentAdded(String consignmentId, String cargoTrackingId);

    void presentErrorWhenConsignmentCouldNotBeAdded(String consignmentId, String cargoTrackingId, String message);

    void presentConsignmentEntryForm(String cargoTrackingId);
}