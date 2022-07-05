package com.github.cargoclean.core.model;

import com.github.cargoclean.core.model.cargo.Leg;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class LegTest {

    @Test
    void should_use_fields_other_than_id_when_comparing_with_equals() {
        Leg leg1 = MockModels.leg(1);
        assertThat(leg1).isNotEqualTo(leg1.withLoadLocation(MockModels.location("SEGOT")));
        assertThat(leg1).isNotEqualTo(leg1.withLoadTime(MockModels.localDate("14-01-2025")));
    }

    @Test
    void should_ignore_id_when_comparing_with_equals() {
        Leg leg1 = MockModels.leg(1);
        assertThat(leg1).isEqualTo(leg1.withId(2));
    }
}
