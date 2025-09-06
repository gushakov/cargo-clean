package com.github.cargoclean.infrastructure.adapter.db.consigment;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Builder
@Table("consignment")
public class ConsignmentDbEntity {

    @Column("consignment_id")
    private String consignmentId;

    @Column("quantity_in_containers")
    private int quantityInContainers;

    @Column("cargo_tracking_id")
    private String cargoTrackingId;

}