package com.github.cargoclean.infrastructure.adapter.db.map;

import com.github.cargoclean.core.model.cargo.Cargo;
import com.github.cargoclean.core.model.cargo.Delivery;
import com.github.cargoclean.core.model.cargo.RouteSpecification;
import com.github.cargoclean.core.model.location.Location;
import com.github.cargoclean.core.model.report.ExpectedArrivals;
import com.github.cargoclean.infrastructure.adapter.db.cargo.CargoDbEntity;
import com.github.cargoclean.infrastructure.adapter.db.cargo.DeliveryDbEntity;
import com.github.cargoclean.infrastructure.adapter.db.cargo.RouteSpecificationDbEntity;
import com.github.cargoclean.infrastructure.adapter.db.location.LocationDbEntity;
import com.github.cargoclean.infrastructure.adapter.db.report.ExpectedArrivalsQueryRow;
import com.github.cargoclean.infrastructure.adapter.map.CommonMapStructConverters;
import com.github.cargoclean.infrastructure.adapter.map.IgnoreForMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/*
    References:
    ----------

    1.  Iterate over Stream with indexes: https://www.baeldung.com/java-stream-indices
    2.  Issue with AfterMapping and Lombok builders: https://github.com/mapstruct/mapstruct/issues/1454
 */


/**
 * MapStruct will generate a mapper for us in {@code com.github.cargoclean.infrastructure.adapter.db.map.DefaultDbEntityMapperImpl}.
 */
@Mapper(componentModel = "spring", uses = CommonMapStructConverters.class)
public abstract class DefaultDbEntityMapper implements DbEntityMapper {

    abstract Location map(LocationDbEntity locationDbEntity);

    abstract LocationDbEntity map(Location location);

    abstract DeliveryDbEntity map(Delivery delivery);

    abstract Delivery map(DeliveryDbEntity deliveryDbEntity);

    abstract RouteSpecification map(RouteSpecificationDbEntity routeSpecificationDbEntity);

    abstract RouteSpecificationDbEntity map(RouteSpecification routeSpecification);

    abstract CargoDbEntity map(Cargo cargo);

    abstract Cargo map(CargoDbEntity cargoDbEntity);

    @Mapping(target = "numberOfArrivals", source = "arrivals")
    @Mapping(target = "city.unlocode", source = "unlocode")
    @Mapping(target = "city.name", source = "city")
    abstract ExpectedArrivals map(ExpectedArrivalsQueryRow arrivalsQueryRow);

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

    @Override
    @IgnoreForMapping
    public ExpectedArrivals convert(ExpectedArrivalsQueryRow arrivalsQueryRow){
        return map(arrivalsQueryRow);
    }
}
