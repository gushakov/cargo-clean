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

    @Column("load_location_id")
    private Integer loadLocationId;

    @Column("load_time")
    private Instant loadTime;

    @Column("unload_location_id")
    private Integer unloadLocationId;

    @Column("unload_time")
    private Instant unloadTime;

    /**
     * Foreign key matching ID for {@code Cargo} which itinerary this leg is part of.
     * Also see {@code MappedCollection} annotation in {@code CargoDbEntity} where
     * ID column name is customized.
     */
    @Column("cargo_id")
    private Integer cargoId;

    /**
     * Index of this leg in the intinerary of the related {@code Cargo}. Also see
     * {@code MappedCollection} annotation in {@code CargoDbEntity} where key column
     * name is customized.
     */
    @Column("leg_index")
    private Integer legIndex;

}
