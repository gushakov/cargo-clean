package com.github.cargoclean.infrastructure.adapter.db.location;

import lombok.Data;

@Data
public class LocationExistsQueryRow {

    public static final String SQL = "select count(*) as count from location l where l.unlocode = :unlocode";

    Integer count;

    public boolean exists() {
        return count > 0;
    }

}
