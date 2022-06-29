package com.github.cargoclean.infrastructure.adapter.db.map;

import com.github.cargoclean.core.model.cargo.Cargo;
import com.github.cargoclean.core.model.cargo.Delivery;
import com.github.cargoclean.core.model.cargo.TrackingId;
import com.github.cargoclean.core.model.cargo.TransportStatus;
import com.github.cargoclean.core.model.location.Location;
import com.github.cargoclean.core.model.location.UnLocode;
import com.github.cargoclean.infrastructure.adapter.db.CargoDbEntity;
import com.github.cargoclean.infrastructure.adapter.db.DeliveryDbEntity;
import com.github.cargoclean.infrastructure.adapter.db.LocationDbEntity;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import static org.assertj.core.api.Assertions.assertThat;

@SpringJUnitConfig(classes = {DefaultDbEntityMapperImpl.class})
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

        assertThat(location)
                .isNotNull()
                .extracting(Location::getUnLocode, Location::getName)
                .containsExactly(UnLocode.of("JNTKO"), "Tokyo");
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

        final Cargo cargo = Cargo.builder()
                .trackingId(TrackingId.builder()
                        .id("75FC0BD4")
                        .build())
                .origin(Location.builder()
                        .name("Dallas")
                        .unLocode(UnLocode.builder()
                                .code("USDAL")
                                .build())
                        .build())
                .delivery(Delivery.builder()
                        .transportStatus(TransportStatus.IN_PORT)
                        .build())
                .build();

        final CargoDbEntity cargoDbEntity = mapper.convert(cargo);

        assertThat(cargoDbEntity.getTrackingId())
                .isEqualTo("75FC0BD4");

        assertThat(cargoDbEntity.getOrigin())
                .extracting(LocationDbEntity::getName, LocationDbEntity::getUnlocode)
                .containsExactly("Dallas", "USDAL");


    }
}
