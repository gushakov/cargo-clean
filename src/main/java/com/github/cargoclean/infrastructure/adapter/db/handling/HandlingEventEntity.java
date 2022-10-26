package com.github.cargoclean.infrastructure.adapter.db.handling;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;

@Data
@Table(name = "handling_event")
@Builder
public class HandlingEventEntity {

    @Id
    @Column("event_id")
    private Long eventId;

    @Column("voyage_number")
    private String voyageNumber;

    @Column("location")
    private String location;

    @Column("cargo_id")
    private String cargoId;

    @Column("completion_time")
    private Instant completionTime;

    @Column("registration_time")
    private Instant registrationTime;

    @Column("type")
    private String type;

    @Version
    private Integer version;

}
