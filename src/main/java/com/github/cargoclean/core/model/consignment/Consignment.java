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

    ConsignmentId id;
    TrackingId cargoTrackingId;
    int quantityInContainers;
    Integer version;

    @Builder
    private Consignment(ConsignmentId id, int quantityInContainers, Integer version, TrackingId cargoTrackingId) {
        this.id = Assert.notNull(id);
        this.quantityInContainers = Assert.positive(quantityInContainers);
        this.version = version;
        this.cargoTrackingId = cargoTrackingId;
    }

    public Consignment assignToCargo(TrackingId trackingId) {
        return newEntity().cargoTrackingId(trackingId).build();
    }

    private ConsignmentBuilder newEntity() {
        return Consignment.builder()
                .id(id)
                .cargoTrackingId(cargoTrackingId)
                .quantityInContainers(quantityInContainers)
                .version(version)
                ;
    }
}