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
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * MapStruct will generate a mapper for us in {@code com.github.cargoclean.infrastructure.adapter.db.map.DefaultDbEntityMapperImpl}.
 */
@Mapper(componentModel = "spring", uses = MapStructConverters.class)
public abstract class DefaultDbEntityMapper implements DbEntityMapper {

    @Mapping(target = "unLocode", source = "unlocode")
    abstract Location map(LocationDbEntity locationDbEntity);

    @InheritInverseConfiguration
    abstract LocationDbEntity map(Location location);

    abstract DeliveryDbEntity map(Delivery delivery);

    abstract Delivery map(DeliveryDbEntity deliveryDbEntity);

    abstract CargoDbEntity map(Cargo cargo);

    @Mapping(target = "origin", ignore = true)
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
