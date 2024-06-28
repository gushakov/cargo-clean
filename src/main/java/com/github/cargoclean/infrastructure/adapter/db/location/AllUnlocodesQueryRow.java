package com.github.cargoclean.infrastructure.adapter.db.location;

import lombok.Data;

@Data
public class AllUnlocodesQueryRow {

    public static final String SQL = """
            select distinct l.unlocode from "location" l;
            """.trim();

    String unlocode;

}
