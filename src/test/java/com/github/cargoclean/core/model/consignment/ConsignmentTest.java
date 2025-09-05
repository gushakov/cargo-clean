package com.github.cargoclean.core.model.consignment;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ConsignmentTest {

    @Test
    void withQuantityInContainers_shouldReturnNewInstance() {
        // Arrange
        ConsignmentId consignmentId = ConsignmentId.builder().id("test-consignment-id").build();
        Consignment consignment = Consignment.builder().consignmentId(consignmentId).quantityInContainers(10).build();
        int newQuantity = 15;

        // Act
        Consignment newConsignment = consignment.withQuantityInContainers(newQuantity);

        // Assert
        assertNotSame(consignment, newConsignment, "Should return a new instance");
        assertEquals(consignment.getConsignmentId(), newConsignment.getConsignmentId(), "Consignment ID should be the same");
        assertEquals(newQuantity, newConsignment.getQuantityInContainers(), "Quantity should be updated in new instance");
        assertEquals(10, consignment.getQuantityInContainers(), "Original consignment should not be modified");
    }

    @Test
    void builder_shouldCreateNewInstance() {
        // Arrange
        ConsignmentId consignmentId = ConsignmentId.builder().id("test-consignment-id").build();

        // Act
        Consignment consignment = Consignment.builder().consignmentId(consignmentId).quantityInContainers(10).build();

        // Assert
        assertNotNull(consignment);
        assertEquals(consignmentId, consignment.getConsignmentId());
        assertEquals(10, consignment.getQuantityInContainers());
    }
}