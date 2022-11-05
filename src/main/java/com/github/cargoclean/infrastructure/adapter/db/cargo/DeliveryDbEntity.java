package com.github.cargoclean.infrastructure.adapter.db.cargo;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.relational.core.mapping.Column;

import java.time.Instant;

@Data
@Builder
public class DeliveryDbEntity {

    @Column("transport_status")
    String transportStatus;

    @Column("last_known_location")
    String lastKnownLocation;

    @Column("current_voyage")
    String currentVoyage;

    @Column("eta")
    Instant eta;

    @Column("routing_status")
    String routingStatus;

    @Column("is_misdirected")
    boolean misdirected;

}
