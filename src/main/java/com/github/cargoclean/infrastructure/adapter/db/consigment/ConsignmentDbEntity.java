package com.github.cargoclean.infrastructure.adapter.db.consigment;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Builder
@Table("consignment")
public class ConsignmentDbEntity {

    @Id
    @Column("id")
    private String id;

    @Column("cargo_tracking_id")
    private String cargoTrackingId;

    @Column("quantity_in_containers")
    private int quantityInContainers;

    /**
     * Needed by Spring Data JDBC, see {@code Cargo}.
     */
    @Version
    private Integer version;

}