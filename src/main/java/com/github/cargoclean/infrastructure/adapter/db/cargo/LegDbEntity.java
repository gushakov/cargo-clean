package com.github.cargoclean.infrastructure.adapter.db.cargo;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;

@Data
@Table(name = "leg")
@Builder
public class LegDbEntity {

    @Id
    @Column("id")
    private Integer id;

    @Column("voyage_id")
    private Integer voyageId;

    @Column("load_location_id")
    private Integer loadLocationId;

    @Column("load_time")
    private Instant loadTime;

    @Column("unload_location_id")
    private Integer unloadLocationId;

    @Column("unload_time")
    private Instant unloadTime;

    @Column("cargo_id")
    private Integer cargoId;

    @Column("leg_index")
    private Integer legIndex;

}
