package com.github.cargoclean.infrastructure.adapter.db;

/*
    Notice on COPYRIGHT:
    -------------------

    Some code in this file is based on, copied from, and or modified from
    the code in the original DDDSample application. Please, see the copyright
    notice in "README.md" and the copy of the original licence in
    "original-license.txt", as well.
 */


import com.github.cargoclean.core.model.cargo.Cargo;
import com.github.cargoclean.core.model.cargo.Delivery;
import com.github.cargoclean.core.model.cargo.RouteSpecification;
import com.github.cargoclean.core.model.cargo.TrackingId;
import com.github.cargoclean.core.model.location.Location;
import com.github.cargoclean.core.model.location.UnLocode;
import com.github.cargoclean.core.port.operation.PersistenceGatewayOutputPort;
import com.github.cargoclean.infrastructure.adapter.db.cargo.CargoDbEntity;
import com.github.cargoclean.infrastructure.adapter.db.cargo.CargoDbEntityRepository;
import com.github.cargoclean.infrastructure.adapter.db.cargo.DeliveryDbEntity;
import com.github.cargoclean.infrastructure.adapter.db.cargo.RouteSpecificationDbEntity;
import com.github.cargoclean.infrastructure.adapter.db.location.LocationDbEntityRepository;
import com.github.cargoclean.infrastructure.adapter.db.map.DbEntityMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * Default implementation of the persistence gateway. It uses one Spring Data JDBC
 * repository for each aggregate root. And it relies on MapStruct mapper to map between
 * DB entities and models.
 * <p>
 * This is a classical "Repository pattern" implementation. We have a greater control
 * over the mapping between database rows and domain entities than if we were using
 * an ORM (Hibernate).
 *
 * @see DbEntityMapper
 */
@Service
@RequiredArgsConstructor
public class DbPersistenceGateway implements PersistenceGatewayOutputPort {

    private final LocationDbEntityRepository locationRepository;
    private final CargoDbEntityRepository cargoRepository;
    private final DbEntityMapper dbMapper;

    @Override
    public TrackingId nextTrackingId() {

        /*
            Based on tracking ID generator in the original:
            "se.citerus.dddsample.infrastructure.persistence.hibernate.CargoRepositoryHibernate"
         */
        final String uuid = UUID.randomUUID().toString();
        return TrackingId.of(uuid.substring(0, uuid.indexOf("-")).toUpperCase());
    }

    @Override
    public List<Location> allLocations() {
        return toStream(locationRepository.findAll())
                .map(dbMapper::convert).toList();
    }

    @Override
    public Location obtainLocationByUnLocode(UnLocode unLocode) {
        return dbMapper.convert(locationRepository.findByUnlocode(unLocode.getCode()));
    }

    @Override
    public Cargo saveCargo(Cargo cargoToSave) {

        final CargoDbEntity cargoDbEntity = dbMapper.convert(cargoToSave);

        // save cargo
        final CargoDbEntity savedCargoDbEntity = cargoRepository.save(cargoDbEntity);

        // convert and load relations
        return convertAndLoadRelations(savedCargoDbEntity);

    }

    @Override
    public Cargo obtainCargoByTrackingId(TrackingId trackingId) {
        return convertAndLoadRelations(cargoRepository.findByTrackingId(trackingId.getId()).orElseThrow());
    }

    /*
        This is where we have to implement (eager) loading of the relations
        ourselves, since we are not using ORM.
     */

    private Cargo convertAndLoadRelations(CargoDbEntity cargoDbEntity) {
        final Cargo partialCargo = dbMapper.convert(cargoDbEntity);
        final Location origin = dbMapper.convert(locationRepository.findById(cargoDbEntity.getOrigin()).orElseThrow());
        final Delivery delivery = convertAndLoadRelations(cargoDbEntity.getDelivery());
        final RouteSpecification routeSpec = convertAndLoadRelations(cargoDbEntity.getRouteSpecification());
        return partialCargo.withOrigin(origin)
                .withDelivery(delivery)
                .withRouteSpecification(routeSpec);
    }

    private Delivery convertAndLoadRelations(DeliveryDbEntity deliveryDbEntity) {
        return dbMapper.convert(deliveryDbEntity);
    }

    private RouteSpecification convertAndLoadRelations(RouteSpecificationDbEntity specDbEntity) {
        RouteSpecification partialRouteSpec = dbMapper.convert(specDbEntity);
        final Location origin = dbMapper.convert(locationRepository
                .findById(specDbEntity.getOrigin()).orElseThrow());
        final Location destination = dbMapper.convert(locationRepository
                .findById(specDbEntity.getDestination()).orElseThrow());
        return partialRouteSpec.withOrigin(origin)
                .withDestination(destination);
    }

    private <T> Stream<T> toStream(Iterable<T> iterable) {
        return StreamSupport.stream(iterable.spliterator(), false);
    }
}
