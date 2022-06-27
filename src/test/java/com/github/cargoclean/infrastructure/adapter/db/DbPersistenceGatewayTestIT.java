package com.github.cargoclean.infrastructure.adapter.db;

import com.github.cargoclean.CargoCleanApplication;
import com.github.cargoclean.core.model.location.Location;
import com.github.cargoclean.core.model.location.UnLocode;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(classes = {CargoCleanApplication.class}, webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class DbPersistenceGatewayTestIT {

    /*
        These are integrations tests intended to be run from the IDE to
        verify the workings of the persistence gateway with the running
        Postgres database (using local Docker instance).
     */

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    private DbPersistenceGateway dbGateway;

    @Test
    void should_process_flyway_initialization_scripts() {
        final Integer locationsCount = jdbcTemplate.queryForObject("select count(*) from public.location",
                Map.of(), Integer.class);
        assertThat(locationsCount).isEqualTo(13);
    }

    @Test
    void should_load_all_locations() {
        final List<Location> locations = dbGateway.allLocations();
        assertThat(locations)
                .hasSize(13)
                .extracting(Location::getUnLocode, Location::getName)
                .contains(tuple(UnLocode.of("USNYC"), "New York"));

        // locations should not be modifiable

        assertThrows(UnsupportedOperationException.class, locations::clear);
    }
}
