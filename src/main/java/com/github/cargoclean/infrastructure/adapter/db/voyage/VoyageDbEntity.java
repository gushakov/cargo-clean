package com.github.cargoclean.infrastructure.adapter.db.voyage;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table(name = "voyage")
@Builder
public class VoyageDbEntity {

    @Id
    @Column("id")
    private Integer id;

    @Column("voyage_number")
    private Integer voyageNumber;

}
