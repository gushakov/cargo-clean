package com.github.cargoclean.infrastructure.adapter.db.cargo;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CargoInfoRow {
    public static final String SQL = """
                    select tracking_id, l."name" as origin, l2."name" as destination, routing_status from public.cargo c join public."location" l on c.origin = l.unlocode
                    join public."location" l2 on c.spec_destination = l2.unlocode;
            """.trim();

    private String trackingId;
    private String origin;
    private String destination;
    private String routingStatus;

}
