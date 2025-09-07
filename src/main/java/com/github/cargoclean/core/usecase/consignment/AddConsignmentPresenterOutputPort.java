package com.github.cargoclean.core.usecase.consignment;

import com.github.cargoclean.core.port.ErrorHandlingPresenterOutputPort;

public interface AddConsignmentPresenterOutputPort extends ErrorHandlingPresenterOutputPort {
    void presentConsignmentAdded(String cargoTrackingId, String consignmentId);

    void presentErrorWhenConsignmentCouldNotBeAdded(String consignmentId, String cargoTrackingId, String message);
}