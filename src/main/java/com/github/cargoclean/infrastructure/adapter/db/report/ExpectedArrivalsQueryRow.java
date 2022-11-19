package com.github.cargoclean.infrastructure.adapter.db.report;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ExpectedArrivalsQueryRow {

    public static final String SQL = """
            select l."name" as city, count(*) as arrivals from cargo c join "location" l on c.spec_destination = l.unlocode group by l.unlocode, l."name" order by arrivals desc;
            """.trim();

    @Builder
    public ExpectedArrivalsQueryRow(String city, Integer arrivals) {
        this.city = city;
        this.arrivals = arrivals;
    }

    private String city;
    private Integer arrivals;
}
