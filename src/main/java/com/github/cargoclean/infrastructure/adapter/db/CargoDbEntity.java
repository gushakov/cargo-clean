package com.github.cargoclean.infrastructure.adapter.db;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
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
    private String trackingId;

    private LocationDbEntity origin;

    @Embedded.Nullable
    private DeliveryDbEntity delivery;

}
