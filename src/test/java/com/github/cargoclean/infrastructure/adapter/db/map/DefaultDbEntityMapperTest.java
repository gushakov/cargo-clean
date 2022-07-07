package com.github.cargoclean.infrastructure.adapter.db.map;

import com.github.cargoclean.core.model.cargo.*;
import com.github.cargoclean.core.model.location.Location;
import com.github.cargoclean.core.model.location.UnLocode;
import com.github.cargoclean.core.model.report.ExpectedArrivals;
import com.github.cargoclean.core.model.voyage.VoyageNumber;
import com.github.cargoclean.infrastructure.adapter.db.cargo.CargoDbEntity;
import com.github.cargoclean.infrastructure.adapter.db.cargo.DeliveryDbEntity;
import com.github.cargoclean.infrastructure.adapter.db.cargo.LegDbEntity;
import com.github.cargoclean.infrastructure.adapter.db.cargo.RouteSpecificationDbEntity;
import com.github.cargoclean.infrastructure.adapter.db.location.LocationDbEntity;
import com.github.cargoclean.infrastructure.adapter.db.report.ExpectedArrivalsQueryRow;
import com.github.cargoclean.infrastructure.adapter.map.CommonMapStructConverters;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.List;

import static com.github.cargoclean.core.model.MockModels.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;

/*
    References:
    ----------

    1.  AssertJ, describe messages: https://stackoverflow.com/questions/28994316/can-you-add-a-custom-message-to-assertj-assertthat

 */

@SpringJUnitConfig(classes = {DefaultDbEntityMapperImpl.class, CommonMapStructConverters.class})
public class DefaultDbEntityMapperTest {

    @Autowired
    private DefaultDbEntityMapper mapper;

    @Test
    void should_map_location_db_entity_to_model() {
        assertThat(mapper).isNotNull();

        final LocationDbEntity dbEntity = LocationDbEntity.builder()
                .unlocode("JNTKO")
                .name("Tokyo")
                .build();

        final Location location = mapper.convert(dbEntity);

        Location example = location("JNTKO");
        assertThat(location).isEqualTo(example);
        assertThat(location.getName()).isEqualTo(example.getName());
    }

    @Test
    void should_map_delivery_to_db_entity() {

        final Delivery delivery = Delivery.builder()
                .transportStatus(TransportStatus.IN_PORT)
                .build();

        final DeliveryDbEntity dbEntity = mapper.map(delivery);

        assertThat(dbEntity.getTransportStatus())
                .isEqualTo(TransportStatus.IN_PORT.name());

    }

    @Test
    void should_map_delivery_db_entity_to_model() {

        final DeliveryDbEntity deliveryDbEntity = DeliveryDbEntity.builder()
                .transportStatus(TransportStatus.IN_PORT.name())
                .build();

        final Delivery delivery = mapper.map(deliveryDbEntity);

        assertThat(delivery.getTransportStatus())
                .isEqualTo(TransportStatus.IN_PORT);

    }

    @Test
    void should_map_cargo_model_to_db_entity() {

        final Cargo cargo = cargo("75FC0BD4");

        final CargoDbEntity cargoDbEntity = mapper.convert(cargo);

        assertThat(cargoDbEntity.getTrackingId())
                .as("Cargo tracking ID")
                .isEqualTo("75FC0BD4");

        assertThat(cargoDbEntity.getDelivery().getTransportStatus())
                .as("Delivery, transport status")
                .isEqualTo(TransportStatus.IN_PORT.name());

        assertThat(cargoDbEntity.getRouteSpecification())
                .as("Routing specification")
                .extracting(RouteSpecificationDbEntity::getOrigin,
                        RouteSpecificationDbEntity::getDestination,
                        RouteSpecificationDbEntity::getArrivalDeadline)
                .containsExactly("USDAL", "AUMEL", localInstant("24-08-2022"));

    }

    @Test
    void should_map_cargo_db_entity_to_model() {
        CargoDbEntity cargoDbEntity = CargoDbEntity.builder()
                .trackingId("ABCDEF12")
                .origin("USDAL")
                .delivery(DeliveryDbEntity.builder()
                        .transportStatus(TransportStatus.IN_PORT.name())
                        .build())
                .routeSpecification(RouteSpecificationDbEntity.builder()
                        .origin("USDAL")
                        .destination("AUMEL")
                        .arrivalDeadline(localInstant("10-10-2022"))
                        .build())
                .build();

        Cargo cargo = mapper.convert(cargoDbEntity);

        assertThat(cargo.getTrackingId()).isEqualTo(TrackingId.of("ABCDEF12"));
        assertThat(cargo.getOrigin()).isEqualTo(UnLocode.of("USDAL"));

    }

    @Test
    void should_map_expected_arrivals_query_row_to_model() {
        ExpectedArrivalsQueryRow queryRow = ExpectedArrivalsQueryRow.builder()
                .unlocode("AUMEL")
                .city("Melbourne")
                .arrivals(2)
                .build();

        ExpectedArrivals expectedArrivals = mapper.convert(queryRow);

        assertThat(expectedArrivals)
                .extracting(ExpectedArrivals::getCity, ExpectedArrivals::getNumberOfArrivals)
                .containsExactly(location("AUMEL"), 2);
    }

    @Test
    void should_map_itinerary_to_list_of_legs() {
        Cargo cargo = cargo("8E062F47").withItinerary(itinerary(1, 2));

        CargoDbEntity cargoDbEntity = mapper.convert(cargo);

        assertThat(cargoDbEntity.getLegs())
                .extracting(LegDbEntity::getCargoTrackingId,
                        LegDbEntity::getVoyageNumber,
                        LegDbEntity::getLoadLocation,
                        LegDbEntity::getUnloadLocation,
                        LegDbEntity::getLoadTime,
                        LegDbEntity::getUnloadTime)
                .containsExactly(tuple("8E062F47", "100S", "USDAL", "AUMEL",
                                localInstant("05-07-2022"), localInstant("23-07-2022")),
                        tuple("8E062F47", "200S", "AUMEL", "JNTKO",
                                localInstant("25-07-2022"), localInstant("05-08-2022")));
    }

    @Test
    void should_map_list_of_legs_to_itinerary() {
        CargoDbEntity partialCargoDbEntity = CargoDbEntity.builder()
                .trackingId("8E062F47")
                .origin("USDAL")
                .delivery(DeliveryDbEntity.builder()
                        .transportStatus(TransportStatus.ONBOARD_CARRIER.name())
                        .build())
                .routeSpecification(RouteSpecificationDbEntity.builder()
                        .origin("USDAL")
                        .destination("AUMEL")
                        .arrivalDeadline(localInstant("10-08-2022"))
                        .build())
                .legs(List.of(LegDbEntity.builder()
                        .cargoTrackingId("8E062F47")
                                .voyageNumber("100S")
                        .loadLocation("USDAL")
                        .unloadLocation("AUMEL")
                        .loadTime(localInstant("05-07-2022"))
                        .unloadTime(localInstant("05-08-2022"))
                        .build()))
                .build();

        Cargo partialCargo = mapper.convert(partialCargoDbEntity);
        assertThat(partialCargo.getItinerary()).isNotNull();
        assertThat(partialCargo.getItinerary().getLegs()).isNotNull();
        assertThat(partialCargo.getItinerary().getLegs())
                .extracting(Leg::getCargoTrackingId,
                        Leg::getVoyageNumber,
                        Leg::getLoadLocation,
                        Leg::getUnloadLocation,
                        Leg::getLoadTime,
                        Leg::getUnloadTime)
                .containsExactly(tuple(TrackingId.of("8E062F47"),
                        VoyageNumber.of("100S"),
                        UnLocode.of("USDAL"), UnLocode.of("AUMEL"),
                        localDate("05-07-2022"), localDate("05-08-2022")));
    }
}
