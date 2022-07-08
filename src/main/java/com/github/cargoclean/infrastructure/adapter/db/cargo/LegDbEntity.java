package com.github.cargoclean.infrastructure.adapter.db.cargo;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;

@Data
@Builder
@Table("leg")
public class LegDbEntity {

    @Column("cargo_tracking_id")
    private String cargoTrackingId;

    @Column("voyage_number")
    private String voyageNumber;

    @Column("load_location")
    private String loadLocation;

    @Column("unload_location")
    private String unloadLocation;

    @Column("load_time")
    private Instant loadTime;

    @Column("unload_time")
    private Instant unloadTime;

}
