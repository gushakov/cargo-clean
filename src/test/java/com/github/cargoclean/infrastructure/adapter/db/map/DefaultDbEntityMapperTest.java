package com.github.cargoclean.infrastructure.adapter.db.map;

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
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.List;

import static com.github.cargoclean.core.model.MockModels.*;
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

        Voyage voyage = voyage("AB001");

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

        assertThat(voyage).isEqualTo(voyage("AB001"));

    }

}
