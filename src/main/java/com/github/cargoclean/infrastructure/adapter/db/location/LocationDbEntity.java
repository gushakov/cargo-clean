package com.github.cargoclean.infrastructure.adapter.db.location;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * Class which will used to store {@code Location} in the database.
 */
@Data
@Table(name = "location")
@Builder
public class LocationDbEntity {

    @Id
    @Column("unlocode")
    private String unlocode;

    @Column("name")
    private String name;

}
