package com.github.cargoclean.infrastructure.adapter.db;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

/**
 * Class which will used to store {@code Location} in the database.
 */
@Data
@Table(name = "location")
@Builder
public class LocationDbEntity {

    @Id
    private Integer id;

    private String unlocode;

    private String name;

}
