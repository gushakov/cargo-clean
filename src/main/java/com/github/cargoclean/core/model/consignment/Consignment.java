package com.github.cargoclean.core.model.consignment;

import com.github.cargoclean.core.model.Assert;
import com.github.cargoclean.core.model.cargo.TrackingId;
import lombok.Builder;
import lombok.Value;

/**
 * Represents a consignment, the unit of goods being shipped.
 * This is an aggregate root.
 */
@Value
public class Consignment {

    ConsignmentId consignmentId;
    int quantityInContainers;
    Integer version;
    TrackingId trackingId;

    @Builder
    private Consignment(ConsignmentId consignmentId, int quantityInContainers, Integer version, TrackingId trackingId) {
        this.consignmentId = Assert.notNull(consignmentId);
        this.quantityInContainers = Assert.positive(quantityInContainers);
        this.version = version;
        this.trackingId = trackingId;
    }

    public Consignment withQuantityInContainers(int newQuantity) {
        return newEntity().quantityInContainers(newQuantity).build();
    }

    public Consignment assignToCargo(TrackingId trackingId) {
        return newEntity().trackingId(trackingId).build();
    }

    private ConsignmentBuilder newEntity() {
        return Consignment.builder()
                .consignmentId(consignmentId)
                .quantityInContainers(quantityInContainers)
                .version(version)
                .trackingId(trackingId)
                ;
    }
}