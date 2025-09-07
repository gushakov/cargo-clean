package com.github.cargoclean.core.usecase.consignment;

public interface AddConsignmentPresenterOutputPort {
    void presentConsignmentAdded(String cargoTrackingId, String consignmentId);
    void presentError(Exception e);
}