package com.github.cargoclean.core.model.cargo;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static com.github.cargoclean.core.model.MockModels.leg;
import static org.assertj.core.api.Assertions.assertThat;

public class ItineraryTest {

    @Test
    void should_initialize_itinerary_with_empty_list_of_legs_by_default() {
        assertThat(Itinerary.builder().build().getLegs())
                .isNotNull()
                .isEmpty();
    }

    @Test
    void should_initialize_itinerary_with_unmodifiable_list_of_legs() {

        List<Leg> legs = new ArrayList<>(List.of(leg(1), leg(2)));

        Itinerary itinerary = Itinerary.builder()
                .legs(legs)
                .build();

        legs.clear();

        assertThat(itinerary.getLegs())
                .hasSize(2);

    }
}
