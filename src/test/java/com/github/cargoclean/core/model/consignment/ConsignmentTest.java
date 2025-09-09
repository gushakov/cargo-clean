package com.github.cargoclean.core.model.consignment;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ConsignmentTest {

    @Test
    void builder_shouldCreateNewInstance() {
        // Arrange
        ConsignmentId consignmentId = ConsignmentId.builder().id("test-consignment-id").build();

        // Act
        Consignment consignment = Consignment.builder().id(consignmentId).quantityInContainers(10).build();

        // Assert
        assertNotNull(consignment);
        assertEquals(consignmentId, consignment.getId());
        assertEquals(10, consignment.getQuantityInContainers());
    }
}