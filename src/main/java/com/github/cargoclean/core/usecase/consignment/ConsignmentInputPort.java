package com.github.cargoclean.core.usecase.consignment;

public interface ConsignmentInputPort {
    void agentInitializesConsignmentEntry(String cargoTrackingId);

    void agentAssignsConsignmentToCargo(String cargoTrackingId, String consignmentId, int quantityInContainers);
}
