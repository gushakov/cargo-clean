package com.github.cargoclean.core.model.consignment;

import lombok.Builder;
import lombok.Value;
import org.springframework.util.Assert;

/**
 * Represents a consignment, the unit of goods being shipped.
 * This is an aggregate root.
 */
@Value
@Builder
public class Consignment {

    private final ConsignmentId consignmentId;
    private final int quantityInContainers;

    @Builder
    private Consignment(ConsignmentId consignmentId, int quantityInContainers) {
        Assert.notNull(consignmentId, "ConsignmentId cannot be null");
        Assert.isTrue(quantityInContainers > 0, "Quantity must be positive");
        this.consignmentId = consignmentId;
        this.quantityInContainers = quantityInContainers;
    }

    public Consignment withQuantityInContainers(int newQuantity) {
        return newEntity().quantityInContainers(newQuantity).build();
    }

    private ConsignmentBuilder newEntity() {
        return Consignment.builder().consignmentId(consignmentId).quantityInContainers(quantityInContainers);
    }
}