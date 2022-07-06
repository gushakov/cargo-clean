package com.github.cargoclean.core.model;

import com.github.cargoclean.core.model.cargo.Leg;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class LegTest {

    @Test
    void should_use_load_location_and_load_time_fields_when_comparing_with_equals() {
        Leg leg1 = MockModels.leg(1);
        assertThat(leg1).isNotEqualTo(leg1.withLoadLocation(MockModels.location("SEGOT")));
        assertThat(leg1).isNotEqualTo(leg1.withLoadTime(MockModels.localDate("14-01-2025")));
    }
}
