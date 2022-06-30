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
import com.github.cargoclean.core.model.cargo.TrackingId;
import com.github.cargoclean.core.model.location.Location;
import com.github.cargoclean.core.model.location.UnLocode;
import com.github.cargoclean.core.port.operation.PersistenceGatewayOutputPort;
import com.github.cargoclean.infrastructure.adapter.db.map.DbEntityMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * Default implementation of the persistence gateway. It uses one Spring Data JDBC
 * repository for each aggregate. And it relies on MapStruct mapper to map between
 * DB entities and models.
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
        return convertAndLoadRelations(cargoRepository.findByTrackingId(trackingId.getId()));
    }

    private Cargo convertAndLoadRelations(CargoDbEntity cargoDbEntity){
        final Cargo partialCargo = dbMapper.convert(cargoRepository.save(cargoDbEntity));
        final Location origin = dbMapper.convert(locationRepository.findById(cargoDbEntity.getOrigin()).orElseThrow());
        return partialCargo.withOrigin(origin);
    }

    private <T> Stream<T> toStream(Iterable<T> iterable) {
        return StreamSupport.stream(iterable.spliterator(), false);
    }
}
