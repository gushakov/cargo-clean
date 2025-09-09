package com.github.cargoclean.core.usecase.consignment;

public interface ConsignmentInputPort {
    void agentInitializesConsignmentEntry(String cargoTrackingId);

    void addConsignmentToCargo(String cargoTrackingId, String consignmentId, int quantityInContainers);
}
