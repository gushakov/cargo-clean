package com.github.cargoclean.core.model.handling;

import com.github.cargoclean.core.model.cargo.TrackingId;
import com.github.cargoclean.core.model.location.UnLocode;
import com.github.cargoclean.core.model.voyage.VoyageNumber;
import org.junit.jupiter.api.Test;

import java.time.ZonedDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class HandlingHistoryTest {

    @Test
    void must_correctly_disambiguate_similar_events_keeping_one_with_latest_completion_time() {
        ZonedDateTime time = ZonedDateTime.now();
        ZonedDateTime timeLater = time.plusSeconds(1L);
        HandlingEvent event1 = HandlingEvent.builder()
                .eventId(EventId.of(1L))
                .voyageNumber(VoyageNumber.of("100S"))
                .cargoId(TrackingId.of("C1"))
                .location(UnLocode.of("AUMEL"))
                .type(HandlingEventType.LOAD)
                .completionTime(time)
                .registrationTime(time)
                .build();
        HandlingEvent event2 = HandlingEvent.builder()
                .eventId(EventId.of(2L))
                .voyageNumber(VoyageNumber.of("100S"))
                .cargoId(TrackingId.of("C1"))
                .location(UnLocode.of("AUMEL"))
                .type(HandlingEventType.LOAD)
                .completionTime(timeLater)
                .registrationTime(timeLater)
                .build();
        HandlingHistory handlingHistory = HandlingHistory.builder()
                .handlingEvents(List.of(event1, event2))
                .build();

        // we should have discarded all events other than the one with the
        // latest completion time
        assertThat(handlingHistory.numberOfEvents()).isEqualTo(1);

        // the second event is the one to keep
        assertThat(handlingHistory.mostRecentlyCompletedEvent())
                .hasValue(event2);
    }
}
