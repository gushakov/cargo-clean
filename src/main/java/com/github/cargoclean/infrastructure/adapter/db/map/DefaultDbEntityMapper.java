package com.github.cargoclean.infrastructure.adapter.db.map;

import com.github.cargoclean.core.GenericCargoError;
import com.github.cargoclean.core.model.InvalidDomainObjectError;
import com.github.cargoclean.core.model.cargo.*;
import com.github.cargoclean.core.model.handling.HandlingEvent;
import com.github.cargoclean.core.model.location.Location;
import com.github.cargoclean.core.model.report.ExpectedArrivals;
import com.github.cargoclean.infrastructure.adapter.db.cargo.*;
import com.github.cargoclean.infrastructure.adapter.db.handling.HandlingEventEntity;
import com.github.cargoclean.infrastructure.adapter.db.location.LocationDbEntity;
import com.github.cargoclean.infrastructure.adapter.db.report.ExpectedArrivalsQueryRow;
import com.github.cargoclean.infrastructure.adapter.map.CommonMapStructConverters;
import com.github.cargoclean.infrastructure.adapter.map.IgnoreForMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

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

    abstract Leg map(LegDbEntity legDbEntity);

    abstract LegDbEntity map(Leg leg);

    @Mapping(target = "legs", source = "itinerary.legs")
    abstract CargoDbEntity map(Cargo cargo);

    @Mapping(target = "itinerary.legs", source = "legs")
    abstract Cargo map(CargoDbEntity cargoDbEntity);

    /*
        Notice how just map the information from the query result DTO
        to Location model object. That's way we avoid a lookup in the DB
        for each "unlocode".
     */
    @Mapping(target = "numberOfArrivals", source = "arrivals")
    abstract ExpectedArrivals map(ExpectedArrivalsQueryRow arrivalsQueryRow);

    abstract HandlingEvent map(HandlingEventEntity handlingEventEntity);

    abstract HandlingEventEntity map(HandlingEvent handlingEvent);

    abstract HandlingActivity map(HandlingActivityDbEntity handlingActivityDbEntity);

    abstract HandlingActivityDbEntity map(HandlingActivity handlingActivity);

    @Mapping(target = "routed", source = "routingStatus", qualifiedByName = "convertRoutingStatusStringToBoolean")
    abstract CargoInfo map(CargoInfoRow cargoInfoRow);

    @Named("convertRoutingStatusStringToBoolean")
    protected boolean convertRoutingStatusStringToBoolean(String routingStatus) {
        if (routingStatus == null) {
            // should not happen
            throw new GenericCargoError("Routing status is null");
        }

        final RoutingStatus status;
        try {
            status = RoutingStatus.valueOf(routingStatus);
        } catch (IllegalArgumentException e) {
            throw new InvalidDomainObjectError("Cannot determine routing status from %s".formatted(routingStatus), e);
        }

        return status == RoutingStatus.ROUTED;

    }

    @IgnoreForMapping
    @Override
    public Location convert(LocationDbEntity locationDbEntity) {
        return map(locationDbEntity);
    }

    @IgnoreForMapping
    @Override
    public LocationDbEntity convert(Location location) {
        return map(location);
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
    public ExpectedArrivals convert(ExpectedArrivalsQueryRow arrivalsQueryRow) {
        return map(arrivalsQueryRow);
    }

    @IgnoreForMapping
    @Override
    public HandlingEvent convert(HandlingEventEntity handlingEventEntity) {
        return map(handlingEventEntity);
    }

    @IgnoreForMapping
    @Override
    public HandlingEventEntity convert(HandlingEvent handlingEvent) {
        return map(handlingEvent);
    }

    @IgnoreForMapping
    @Override
    public CargoInfo convert(CargoInfoRow cargoInfoRow) {
        return map(cargoInfoRow);
    }
}
