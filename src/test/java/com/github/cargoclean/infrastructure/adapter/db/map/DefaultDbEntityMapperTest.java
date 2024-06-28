package com.github.cargoclean.infrastructure.adapter.db.map;

import com.github.cargoclean.core.model.UtcDateTime;
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
import com.github.cargoclean.infrastructure.config.CargoCleanProperties;
import com.github.cargoclean.infrastructure.config.TestCacheConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
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

@SpringJUnitConfig(classes = {TestCacheConfig.class, DefaultDbEntityMapperImpl.class, CommonMapStructConverters.class})
@EnableConfigurationProperties(CargoCleanProperties.class)
public class DefaultDbEntityMapperTest {

    @Autowired
    private DefaultDbEntityMapper mapper;

    @Test
    void should_map_location_db_entity_to_model() {
        assertThat(mapper).isNotNull();

        final LocationDbEntity dbEntity = LocationDbEntity.builder()
                .unlocode("JNTKO")
                .name("Tokyo")
                .region("Asia")
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
                .routingStatus(RoutingStatus.ROUTED)
                .build();

        final DeliveryDbEntity dbEntity = mapper.map(delivery);

        assertThat(dbEntity.getTransportStatus())
                .isEqualTo(TransportStatus.IN_PORT.name());

    }

    @Test
    void should_map_delivery_db_entity_to_model() {

        final DeliveryDbEntity deliveryDbEntity = DeliveryDbEntity.builder()
                .transportStatus(TransportStatus.IN_PORT.name())
                .routingStatus(RoutingStatus.ROUTED.name())
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
                .containsExactly("USDAL", "AUMEL", UtcDateTime.of("24-08-2022").toInstant());

    }

    @Test
    void should_map_cargo_model_to_db_entity_with_delivery() {

        final Cargo cargo = cargo("CC3A58FB");

        final CargoDbEntity cargoDbEntity = mapper.convert(cargo);

        assertThat(cargoDbEntity.getTrackingId())
                .as("Cargo tracking ID")
                .isEqualTo("CC3A58FB");

        assertThat(cargoDbEntity.getDelivery())
                .as("Delivery with transport status, routing status, ETA, misdirected set")
                .extracting(DeliveryDbEntity::getTransportStatus,
                        DeliveryDbEntity::getRoutingStatus,
                        DeliveryDbEntity::getEta,
                        DeliveryDbEntity::isMisdirected)
                .containsExactly(TransportStatus.IN_PORT.name(),
                        RoutingStatus.ROUTED.name(),
                        UtcDateTime.of("16-12-2022").toInstant(),
                        false);

        assertThat(cargoDbEntity.getRouteSpecification())
                .as("Routing specification")
                .extracting(RouteSpecificationDbEntity::getOrigin,
                        RouteSpecificationDbEntity::getDestination,
                        RouteSpecificationDbEntity::getArrivalDeadline)
                .containsExactly("JNTKO", "USNYC", UtcDateTime.of("16-12-2022").toInstant());

    }

    @Test
    void should_map_cargo_db_entity_to_model() {
        CargoDbEntity cargoDbEntity = CargoDbEntity.builder()
                .trackingId("ABCDEF12")
                .origin("USDAL")
                .delivery(DeliveryDbEntity.builder()
                        .transportStatus(TransportStatus.IN_PORT.name())
                        .routingStatus(RoutingStatus.ROUTED.name())
                        .build())
                .routeSpecification(RouteSpecificationDbEntity.builder()
                        .origin("USDAL")
                        .destination("AUMEL")
                        .arrivalDeadline(UtcDateTime.of("10-10-2022").toInstant())
                        .build())
                .build();

        Cargo cargo = mapper.convert(cargoDbEntity);

        assertThat(cargo.getTrackingId()).isEqualTo(TrackingId.of("ABCDEF12"));
        assertThat(cargo.getOrigin()).isEqualTo(UnLocode.of("USDAL"));

    }

    @Test
    void should_map_expected_arrivals_query_row_to_model() {
        ExpectedArrivalsQueryRow queryRow = ExpectedArrivalsQueryRow.builder()
                .city("Melbourne")
                .arrivals(2)
                .build();

        ExpectedArrivals expectedArrivals = mapper.convert(queryRow);

        assertThat(expectedArrivals)
                .extracting(ExpectedArrivals::getCity, ExpectedArrivals::getNumberOfArrivals)
                .containsExactly("Melbourne", 2);
    }

    @Test
    void should_map_itinerary_to_list_of_legs() {
        Cargo cargo = cargo("8E062F47").assignItinerary(itinerary(1, 2));

        CargoDbEntity cargoDbEntity = mapper.convert(cargo);

        assertThat(cargoDbEntity.getLegs())
                .extracting(LegDbEntity::getCargoTrackingId,
                        LegDbEntity::getVoyageNumber,
                        LegDbEntity::getLoadLocation,
                        LegDbEntity::getUnloadLocation,
                        LegDbEntity::getLoadTime,
                        LegDbEntity::getUnloadTime)
                .containsExactly(tuple("8E062F47", "0100S", "USDAL", "AUMEL",
                                UtcDateTime.of("05-07-2022").toInstant(),
                                UtcDateTime.of("23-07-2022").toInstant()),
                        tuple("8E062F47", "0200S", "AUMEL", "JNTKO",
                                UtcDateTime.of("25-07-2022").toInstant(),
                                UtcDateTime.of("05-08-2022").toInstant()));
    }

    @Test
    void should_map_list_of_legs_to_itinerary() {
        CargoDbEntity partialCargoDbEntity = CargoDbEntity.builder()
                .trackingId("8E062F47")
                .origin("USDAL")
                .delivery(DeliveryDbEntity.builder()
                        .transportStatus(TransportStatus.ONBOARD_CARRIER.name())
                        .routingStatus(RoutingStatus.NOT_ROUTED.name())
                        .build())
                .routeSpecification(RouteSpecificationDbEntity.builder()
                        .origin("USDAL")
                        .destination("AUMEL")
                        .arrivalDeadline(UtcDateTime.of("10-08-2022").toInstant())
                        .build())
                .legs(List.of(LegDbEntity.builder()
                        .cargoTrackingId("8E062F47")
                        .voyageNumber("0100S")
                        .loadLocation("USDAL")
                        .unloadLocation("AUMEL")
                        .loadTime(UtcDateTime.of("05-07-2022").toInstant())
                        .unloadTime(UtcDateTime.of("05-08-2022").toInstant())
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
                        VoyageNumber.of("0100S"),
                        UnLocode.of("USDAL"), UnLocode.of("AUMEL"),
                        UtcDateTime.of("05-07-2022"),
                        UtcDateTime.of("05-08-2022")));
    }
}
