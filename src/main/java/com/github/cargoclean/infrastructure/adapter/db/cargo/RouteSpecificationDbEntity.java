package com.github.cargoclean.infrastructure.adapter.db.cargo;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.relational.core.mapping.Column;

import java.time.Instant;

@Data
@Builder
public class RouteSpecificationDbEntity {

    @Column("spec_origin")
    String origin;

    @Column("spec_destination")
    String destination;

    @Column("spec_arrival_deadline")
    Instant arrivalDeadline;

}
