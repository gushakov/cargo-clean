package com.github.cargoclean.infrastructure.adapter.db.map;

import com.github.cargoclean.core.model.MockModels;
import com.github.cargoclean.core.model.cargo.Cargo;
import com.github.cargoclean.core.model.cargo.Delivery;
import com.github.cargoclean.core.model.cargo.TransportStatus;
import com.github.cargoclean.core.model.location.Location;
import com.github.cargoclean.core.model.voyage.Voyage;
import com.github.cargoclean.infrastructure.adapter.db.cargo.CargoDbEntity;
import com.github.cargoclean.infrastructure.adapter.db.cargo.DeliveryDbEntity;
import com.github.cargoclean.infrastructure.adapter.db.cargo.LegDbEntity;
import com.github.cargoclean.infrastructure.adapter.db.cargo.RouteSpecificationDbEntity;
import com.github.cargoclean.infrastructure.adapter.db.location.LocationDbEntity;
import com.github.cargoclean.infrastructure.adapter.db.voyage.VoyageDbEntity;
import com.github.cargoclean.infrastructure.adapter.map.CommonMapStructConverters;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.List;

import static com.github.cargoclean.core.model.MockModels.localInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

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
                .id(1)
                .unlocode("JNTKO")
                .name("Tokyo")
                .build();

        final Location location = mapper.convert(dbEntity);

        Location example = MockModels.location("JNTKO");
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

        final Cargo cargo = MockModels.cargo("75FC0BD4");

        final CargoDbEntity cargoDbEntity = mapper.convert(cargo);

        assertThat(cargoDbEntity.getTrackingId())
                .as("Cargo tracking ID")
                .isEqualTo("75FC0BD4");

        assertThat(cargoDbEntity.getOrigin())
                .as("Cargo origin location")
                .isEqualTo(cargo.getOrigin().getId());

        assertThat(cargoDbEntity.getDelivery().getTransportStatus())
                .as("Delivery, transport status")
                .isEqualTo(TransportStatus.IN_PORT.name());

        assertThat(cargoDbEntity.getRouteSpecification())
                .as("Routing specification")
                .extracting(RouteSpecificationDbEntity::getOrigin,
                        RouteSpecificationDbEntity::getDestination,
                        RouteSpecificationDbEntity::getArrivalDeadline)
                .containsExactly(3, 13, localInstant("24-08-2022"));

    }

    @Test
    void should_map_voyage_model_to_db_entity() {

        Voyage voyage = MockModels.voyage("AB001");

        VoyageDbEntity voyageDbEntity = mapper.convert(voyage);

        assertThat(voyageDbEntity)
                .extracting(VoyageDbEntity::getId, VoyageDbEntity::getVoyageNumber)
                .containsExactly(voyage.getId(), voyage.getVoyageNumber().getNumber());

    }

    @Test
    void should_map_voyage_db_entity_to_model() {

        VoyageDbEntity voyageDbEntity = VoyageDbEntity.builder()
                .voyageNumber("AB001")
                .id(1)
                .build();

        Voyage voyage = mapper.convert(voyageDbEntity);

        assertThat(voyage).isEqualTo(MockModels.voyage("AB001"));

    }

    @Test
    void should_map_cargo_itinerary_to_list_of_legs_db_entities() {

        // cargo with itinerary
        Cargo cargo = MockModels.cargo("8E062F47");

        List<LegDbEntity> legDbEntities = mapper.convertItinerary(cargo);

        assertThat(legDbEntities)
                .extracting(LegDbEntity::getId,
                        LegDbEntity::getVoyageId,
                        LegDbEntity::getLoadLocationId,
                        LegDbEntity::getUnloadLocationId,
                        LegDbEntity::getCargoId,
                        LegDbEntity::getLegIndex)
                .containsExactly(tuple(1, 1, 3, 13, 3, 0),
                        tuple(2, 2, 13, 1, 3, 1));

        assertThat(legDbEntities)
                .extracting(LegDbEntity::getLoadTime,
                        LegDbEntity::getUnloadTime)
                .containsExactly(Tuple.tuple(MockModels.localDate("05-07-2022").toInstant(),
                                MockModels.localDate("23-07-2022").toInstant()),
                        tuple(MockModels.localDate("25-07-2022").toInstant(),
                                MockModels.localDate("05-08-2022").toInstant()));
    }
}
