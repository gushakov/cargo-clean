package com.github.cargoclean.core.usecase.consignment;

public interface AddConsignmentInputPort {
    void addConsignmentToCargo(String cargoTrackingId, String consignmentId, int quantityInContainers);
}