package com.github.cargoclean.infrastructure.adapter.db.map;

import com.github.cargoclean.core.annotation.IgnoreForMapping;
import com.github.cargoclean.core.model.cargo.Cargo;
import com.github.cargoclean.core.model.cargo.Delivery;
import com.github.cargoclean.core.model.cargo.RouteSpecification;
import com.github.cargoclean.core.model.location.Location;
import com.github.cargoclean.infrastructure.adapter.db.cargo.CargoDbEntity;
import com.github.cargoclean.infrastructure.adapter.db.cargo.DeliveryDbEntity;
import com.github.cargoclean.infrastructure.adapter.db.cargo.RouteSpecificationDbEntity;
import com.github.cargoclean.infrastructure.adapter.db.location.LocationDbEntity;
import com.github.cargoclean.infrastructure.adapter.map.CommonMapStructConverters;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * MapStruct will generate a mapper for us in {@code com.github.cargoclean.infrastructure.adapter.db.map.DefaultDbEntityMapperImpl}.
 */
@Mapper(componentModel = "spring", uses = CommonMapStructConverters.class)
public abstract class DefaultDbEntityMapper implements DbEntityMapper {

    @Mapping(target = "unLocode", source = "unlocode")
    abstract Location map(LocationDbEntity locationDbEntity);

    @Mapping(target = "unlocode", source = "unLocode")
    abstract LocationDbEntity map(Location location);

    abstract DeliveryDbEntity map(Delivery delivery);

    abstract Delivery map(DeliveryDbEntity deliveryDbEntity);

    @Mapping(target = "origin", ignore = true)
    @Mapping(target = "destination", ignore = true)
    abstract RouteSpecification map(RouteSpecificationDbEntity routeSpecificationDbEntity);

    abstract RouteSpecificationDbEntity map(RouteSpecification routeSpecification);

    abstract CargoDbEntity map(Cargo cargo);

    /*
        Notice how we are ignoring member entities when mapping
        the aggregate route (Cargo), they will be mapped by
        the persistence gateway which will have to look up
        and convert any related entities from the database
        (Location, etc.).
     */

    @Mapping(target = "origin", ignore = true)
    @Mapping(target = "delivery", ignore = true)
    @Mapping(target = "routeSpecification", ignore = true)
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

    @IgnoreForMapping
    @Override
    public Delivery convert(DeliveryDbEntity deliveryDbEntity) {
        return map(deliveryDbEntity);
    }

    @IgnoreForMapping
    @Override
    public DeliveryDbEntity convert(Delivery delivery) {
        return map(delivery);
    }

    @IgnoreForMapping
    @Override
    public RouteSpecification convert(RouteSpecificationDbEntity routeSpecificationDbEntity) {
        return map(routeSpecificationDbEntity);
    }

    @IgnoreForMapping
    @Override
    public RouteSpecificationDbEntity convert(RouteSpecification routeSpecification) {
        return map(routeSpecification);
    }

}
