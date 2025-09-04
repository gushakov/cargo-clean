package com.github.cargoclean.core.model.consignment;

import lombok.Builder;
import lombok.Value;

import static com.github.cargoclean.core.model.Assert.notBlank;

/**
 * Value object representing the unique identifier for a consignment.
 */
@Value
public class ConsignmentId {

    String id;

    @Builder
    public ConsignmentId(String id) {
        // Make sure we cannot construct an invalid consignment ID.
        this.id = notBlank(id);
    }

    @Override
    public String toString() {
        return id;
    }

    public static ConsignmentId of(String id) {
        return ConsignmentId.builder()
                .id(id)
                .build();
    }
}