package com.github.cargoclean.core.model.cargo;

import com.github.cargoclean.core.model.MockModels;
import com.github.cargoclean.core.model.handling.EventId;
import com.github.cargoclean.core.model.handling.HandlingEvent;
import com.github.cargoclean.core.model.handling.HandlingEventType;
import com.github.cargoclean.core.model.handling.HandlingHistory;
import com.github.cargoclean.core.model.location.UnLocode;
import com.github.cargoclean.core.model.voyage.VoyageNumber;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.github.cargoclean.core.model.MockModels.*;
import static com.github.cargoclean.core.model.handling.HandlingEventType.LOAD;

public class DeliveryTest {

    @Test
    void must_calculate_transport_status_from_handling_history() {

        Delivery delivery = Delivery.derivedFrom(routeSpecification(), itinerary(),
                HandlingHistory.builder()
                        .handlingEvents(List.of(HandlingEvent.builder()
                                .type(LOAD)
                                .eventId(EventId.of(1L))
                                .location(UnLocode.of("USDAL"))
                                .cargoId(TrackingId.of("8E062F47"))
                                .registrationTime(localDate("05-07-2022"))
                                .completionTime(localDate("05-07-2022"))
                                .voyageNumber(VoyageNumber.of("100S"))
                                .build()))
                        .build());

        Assertions.assertThat(delivery.getTransportStatus()).isEqualTo(TransportStatus.ONBOARD_CARRIER);
    }
}
