package com.github.cargoclean.infrastructure.adapter.db.map;

import com.github.cargoclean.core.model.cargo.Cargo;
import com.github.cargoclean.core.model.cargo.Delivery;
import com.github.cargoclean.core.model.cargo.TrackingId;
import com.github.cargoclean.core.model.location.Location;
import com.github.cargoclean.core.model.location.UnLocode;
import com.github.cargoclean.infrastructure.adapter.db.CargoDbEntity;
import com.github.cargoclean.infrastructure.adapter.db.DeliveryDbEntity;
import com.github.cargoclean.infrastructure.adapter.db.LocationDbEntity;
import com.github.cargoclean.core.annotation.IgnoreForMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * MapStruct will generate a mapper for us in {@code com.github.cargoclean.infrastructure.adapter.db.map.DefaultDbEntityMapperImpl}.
 */
@Mapper(componentModel = "spring")
public abstract class DefaultDbEntityMapper implements DbEntityMapper {

    // we need to tell MapStruct how to map some value objects

    protected String map(UnLocode unLocode){
        return unLocode.toString();
    }

    protected UnLocode map(String unlocode) {
        return UnLocode.builder()
                .code(unlocode)
                .build();
    }

    protected String map(TrackingId trackingId){
        return trackingId.toString();
    }
    protected TrackingId mapToTrackingId(String id){
        return TrackingId.builder()
                .id(id)
                .build();
    }

    @Mapping(target = "unLocode", source = "unlocode")
    abstract Location map(LocationDbEntity locationDbEntity);

    abstract DeliveryDbEntity map(Delivery delivery);

    abstract Delivery map(DeliveryDbEntity deliveryDbEntity);

    abstract CargoDbEntity map(Cargo cargo);

    abstract Cargo map(CargoDbEntity cargoDbEntity);

    @IgnoreForMapping
    @Override
    public Location convert(LocationDbEntity locationDbEntity) {
        return map(locationDbEntity);
    }

    @IgnoreForMapping
    @Override
    public CargoDbEntity convert(Cargo cargo) {
        return map(cargo);
    }

    @IgnoreForMapping
    @Override
    public Cargo convert(CargoDbEntity cargoDbEntity) {
        return map(cargoDbEntity);
    }
}
