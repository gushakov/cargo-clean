package com.github.cargoclean.infrastructure.adapter.db.cargo;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.relational.core.mapping.Column;

@Data
@Builder
public class HandlingActivityDbEntity {

    /*
        Column names are the same as in "src/main/resources/se/citerus/dddsample/infrastructure/persistence/hibernate/Cargo.hbm.xml"
        in the original application.
     */

    @Column("next_expected_handling_event_type")
    private String type;

    @Column("next_expected_location")
    private String location;

    @Column("next_expected_voyage")
    private String voyageNumber;

}
