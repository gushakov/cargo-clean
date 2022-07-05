package com.github.cargoclean.infrastructure.adapter.db.map;

import com.github.cargoclean.core.model.cargo.Cargo;
import com.github.cargoclean.core.model.cargo.Delivery;
import com.github.cargoclean.core.model.cargo.Leg;
import com.github.cargoclean.core.model.cargo.RouteSpecification;
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
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/*
    References:
    ----------

    1.  Iterate over Stream with indexes: https://www.baeldung.com/java-stream-indices
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
    @Mapping(target = "itinerary", ignore = true)
    abstract Cargo map(CargoDbEntity cargoDbEntity);

    abstract VoyageDbEntity map(Voyage voyage);

    abstract Voyage map(VoyageDbEntity voyageDbEntity);

    @Mapping(target = "unloadLocationId", source = "unloadLocation.id")
    @Mapping(target = "loadLocationId", source = "loadLocation.id")
    @Mapping(target = "voyageId", source = "voyage.id")
    @Mapping(target = "legIndex", ignore = true)
    @Mapping(target = "cargoId", ignore = true)
    abstract LegDbEntity map(Leg leg);

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

    @IgnoreForMapping
    @Override
    public List<LegDbEntity> convertItinerary(Cargo cargoWithItinerary) {

        if (cargoWithItinerary.getItinerary() == null) {
            return List.of();
        }

        List<Leg> legs = cargoWithItinerary.getItinerary().getLegs();
        return streamWithIndex(legs)
                .map(entry -> {
                    LegDbEntity legDbEntity = this.map(entry.getValue());
                    legDbEntity.setCargoId(cargoWithItinerary.getId());
                    legDbEntity.setLegIndex(entry.getKey());
                    return legDbEntity;
                }).toList();
    }

    private <T> Stream<Map.Entry<Integer, T>> streamWithIndex(List<T> list) {
        return IntStream.range(0, list.size())
                .boxed()
                .map(i -> Map.entry(i, list.get(i)));
    }
}
