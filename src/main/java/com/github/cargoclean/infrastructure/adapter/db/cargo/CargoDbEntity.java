package com.github.cargoclean.infrastructure.adapter.db.cargo;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Embedded;
import org.springframework.data.relational.core.mapping.Table;

/*
    References:
    ----------

    1.  Spring Data JDBC, embedded entites: https://docs.spring.io/spring-data/jdbc/docs/current/reference/html/#jdbc.entity-persistence.embedded-entities
 */

@Data
@Table(name = "cargo")
@Builder
public class CargoDbEntity {

    @Id
    private Integer id;

    private String trackingId;

    @Column("origin_id")
    private Integer origin;

    @Embedded.Nullable
    private DeliveryDbEntity delivery;

    @Embedded.Nullable
    private RouteSpecificationDbEntity routeSpecification;

}
