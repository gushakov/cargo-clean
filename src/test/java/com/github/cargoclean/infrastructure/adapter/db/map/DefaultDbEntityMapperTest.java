package com.github.cargoclean.infrastructure.adapter.db.map;

import com.github.cargoclean.core.model.location.Location;
import com.github.cargoclean.core.model.location.UnLocode;
import com.github.cargoclean.infrastructure.adapter.db.LocationDbEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

@SpringJUnitConfig(classes = {DefaultDbEntityMapperImpl.class})
public class DefaultDbEntityMapperTest {

    @Autowired
    private DbEntityMapper mapper;

    @Test
    void should_map_location_db_entity_to_model() {
        assertThat(mapper).isNotNull();

        final LocationDbEntity dbEntity = LocationDbEntity.builder()
                .id(1)
                .unlocode("JNTKO")
                .name("Tokyo")
                .build();

        final Location location = mapper.convert(dbEntity);

        assertThat(location)
                .isNotNull()
                .extracting(Location::getUnLocode, Location::getName)
                .containsExactly(UnLocode.of("JNTKO"), "Tokyo");
    }
}
