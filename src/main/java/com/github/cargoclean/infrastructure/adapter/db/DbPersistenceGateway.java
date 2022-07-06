package com.github.cargoclean.infrastructure.adapter.db;

/*
    Notice on COPYRIGHT:
    -------------------

    Some code in this file is based on, copied from, and or modified from
    the code in the original DDDSample application. Please, see the copyright
    notice in "README.md" and the copy of the original licence in
    "original-license.txt", as well.
 */

/*
    References:
    ----------

    1.  Hikari CP, connection timeout: https://github.com/brettwooldridge/HikariCP#configuration-knobs-baby
 */

import com.github.cargoclean.core.model.cargo.Cargo;
import com.github.cargoclean.core.model.cargo.TrackingId;
import com.github.cargoclean.core.model.location.Location;
import com.github.cargoclean.core.model.location.UnLocode;
import com.github.cargoclean.core.port.operation.PersistenceGatewayOutputPort;
import com.github.cargoclean.core.port.operation.PersistenceOperationError;
import com.github.cargoclean.infrastructure.adapter.db.cargo.CargoDbEntity;
import com.github.cargoclean.infrastructure.adapter.db.cargo.CargoDbEntityRepository;
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
 * This is a classical "Repository pattern" implementation. We have a full control
 * over the mapping between database rows and domain entities, but since we are not
 * using Hibernate, we load (eagerly) all relations for each aggregate.
 *
 * @see DbEntityMapper
 */
@Service
@RequiredArgsConstructor
public class DbPersistenceGateway implements PersistenceGatewayOutputPort {

    private static final String VERSION = "1";

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
        try {
            return toStream(locationRepository.findAll())
                    .map(dbMapper::convert).toList();
        } catch (Exception e) {
            throw new PersistenceOperationError("Cannot retrieve all locations", e);
        }
    }

    @Override
    public Location obtainLocationByUnLocode(UnLocode unLocode) {
        return dbMapper.convert(locationRepository.findById(unLocode.getCode())
                .orElseThrow(() -> new PersistenceOperationError("Cannot load location with UnLocode: <%s>"
                        .formatted(unLocode))));
    }

    @Override
    public Cargo saveCargo(Cargo cargoToSave) {

        try {
            final CargoDbEntity cargoDbEntity = dbMapper.convert(cargoToSave);

            // save cargo
            cargoRepository.save(cargoDbEntity);

            // convert and load relations
            return obtainCargoByTrackingId(cargoToSave.getTrackingId());
        } catch (Exception e) {
            throw new PersistenceOperationError("Cannot save cargo with tracking ID: <%s>"
                    .formatted(cargoToSave.getTrackingId()), e);
        }

    }

    @Override
    public Cargo obtainCargoByTrackingId(TrackingId trackingId) {
        CargoDbEntity cargoDbEntity = cargoRepository.findById(trackingId.getId())
                .orElseThrow(() -> new PersistenceOperationError("Cannot find Cargo with tracking ID: <%s>"
                        .formatted(trackingId)));
//        cargoDbEntity.setVersion(VERSION);
        return dbMapper.convert(cargoDbEntity);

    }


    private <T> Stream<T> toStream(Iterable<T> iterable) {
        return StreamSupport.stream(iterable.spliterator(), false);
    }
}
