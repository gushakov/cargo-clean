package com.github.cargoclean.core.model.consignment;

import com.github.cargoclean.core.model.Assert;
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

    @Builder
    private Consignment(ConsignmentId consignmentId, int quantityInContainers, Integer version) {
        this.consignmentId = Assert.notNull(consignmentId);
        this.quantityInContainers = Assert.positive(quantityInContainers);
        this.version = version;
    }

    public Consignment withQuantityInContainers(int newQuantity) {
        return newEntity().quantityInContainers(newQuantity).build();
    }

    private ConsignmentBuilder newEntity() {
        return Consignment.builder()
                .consignmentId(consignmentId)
                .quantityInContainers(quantityInContainers)
                .version(version)
                ;
    }
}