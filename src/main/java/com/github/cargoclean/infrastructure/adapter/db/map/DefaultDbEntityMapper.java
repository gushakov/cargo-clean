package com.github.cargoclean.infrastructure.adapter.db.map;

import com.github.cargoclean.core.model.cargo.*;
import com.github.cargoclean.core.model.location.Location;
import com.github.cargoclean.core.model.voyage.Voyage;
import com.github.cargoclean.infrastructure.adapter.db.cargo.CargoDbEntity;
import com.github.cargoclean.infrastructure.adapter.db.cargo.DeliveryDbEntity;
import com.github.cargoclean.infrastructure.adapter.db.cargo.LegDbEntity;
import com.github.cargoclean.infrastructure.adapter.db.cargo.RouteSpecificationDbEntity;
import com.github.cargoclean.infrastructure.adapter.db.location.LocationDbEntity;
import com.github.cargoclean.infrastructure.adapter.db.voyage.VoyageDbEntity;
import com.github.cargoclean.infrastructure.adapter.map.CommonMapStructConverters;
import com.github.cargoclean.infrastructure.adapter.map.IgnoreForMapping;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

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

    @Mapping(target = "legs", ignore = true)
    abstract CargoDbEntity map(Cargo cargo);

    @AfterMapping
    protected void doAfterMapping(Cargo cargo, @MappingTarget CargoDbEntity.CargoDbEntityBuilder cargoDbEntityBuilder) {
        if (cargo.getItinerary() == null) {
            return;
        }
        cargoDbEntityBuilder.legs(cargo.getItinerary().getLegs().stream()
                .map(this::map)
                .toList());
    }

    @Mapping(target = "origin", ignore = true)
    @Mapping(target = "delivery", ignore = true)
    @Mapping(target = "routeSpecification", ignore = true)
    @Mapping(target = "itinerary", ignore = true)
    abstract Cargo map(CargoDbEntity cargoDbEntity);

    @AfterMapping
    protected void doAfterMapping(CargoDbEntity cargoDbEntity, @MappingTarget Cargo.CargoBuilder cargoBuilder) {
        cargoBuilder.itinerary(Itinerary.builder()
                .legs(cargoDbEntity.getLegs().stream()
                        .map(this::map)
                        .toList())
                .build());
    }

    abstract VoyageDbEntity map(Voyage voyage);

    abstract Voyage map(VoyageDbEntity voyageDbEntity);

    @Mapping(target = "unloadLocationId", source = "unloadLocation.id")
    @Mapping(target = "loadLocationId", source = "loadLocation.id")
    @Mapping(target = "legIndex", ignore = true)
    @Mapping(target = "cargoId", ignore = true)
    abstract LegDbEntity map(Leg leg);

    @Mapping(target = "unloadLocation", ignore = true)
    @Mapping(target = "loadLocation", ignore = true)
    abstract Leg map(LegDbEntity legDbEntity);

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

    @IgnoreForMapping
    @Override
    public Voyage convert(VoyageDbEntity voyageDbEntity) {
        return map(voyageDbEntity);
    }

    @IgnoreForMapping
    @Override
    public VoyageDbEntity convert(Voyage voyage) {
        return map(voyage);
    }
}
