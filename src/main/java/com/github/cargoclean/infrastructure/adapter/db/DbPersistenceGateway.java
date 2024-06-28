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
    2.  Spring, TransactionTemplate: source code and JavaDoc
 */

import com.github.cargoclean.core.model.cargo.Cargo;
import com.github.cargoclean.core.model.cargo.CargoInfo;
import com.github.cargoclean.core.model.cargo.TrackingId;
import com.github.cargoclean.core.model.handling.EventId;
import com.github.cargoclean.core.model.handling.HandlingEvent;
import com.github.cargoclean.core.model.handling.HandlingHistory;
import com.github.cargoclean.core.model.location.Location;
import com.github.cargoclean.core.model.location.UnLocode;
import com.github.cargoclean.core.model.report.ExpectedArrivals;
import com.github.cargoclean.core.port.persistence.PersistenceGatewayOutputPort;
import com.github.cargoclean.core.port.persistence.PersistenceOperationError;
import com.github.cargoclean.infrastructure.adapter.db.cargo.CargoDbEntity;
import com.github.cargoclean.infrastructure.adapter.db.cargo.CargoDbEntityRepository;
import com.github.cargoclean.infrastructure.adapter.db.cargo.CargoInfoRow;
import com.github.cargoclean.infrastructure.adapter.db.handling.HandlingEventEntity;
import com.github.cargoclean.infrastructure.adapter.db.handling.HandlingEventEntityRepository;
import com.github.cargoclean.infrastructure.adapter.db.location.AllUnlocodesQueryRow;
import com.github.cargoclean.infrastructure.adapter.db.location.LocationDbEntityRepository;
import com.github.cargoclean.infrastructure.adapter.db.location.LocationExistsQueryRow;
import com.github.cargoclean.infrastructure.adapter.db.map.DbEntityMapper;
import com.github.cargoclean.infrastructure.adapter.db.report.ExpectedArrivalsQueryRow;
import com.github.cargoclean.infrastructure.config.CargoCleanProperties;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.interceptor.SimpleKey;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

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
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class DbPersistenceGateway implements PersistenceGatewayOutputPort {

    LocationDbEntityRepository locationRepository;
    CargoDbEntityRepository cargoRepository;
    HandlingEventEntityRepository handlingEventRepository;
    NamedParameterJdbcOperations queryTemplate;
    DbEntityMapper dbMapper;
    CacheManager cacheManager;
    CargoCleanProperties props;

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
    public EventId nextEventId() {

        // Just for demo, should probably be based on a UUID instead.

        return EventId.of(System.currentTimeMillis());
    }


    /*
        Point of interest:
        -----------------

        1.  We are loading all "LocationDbEntity" and converting
            them to models only if they are not already in the cache.
     */

    @Transactional(readOnly = true)
    @Override
    public List<Location> allLocations() {
        try {

            /*
                Point of interest:
                -----------------
                Query for UnLocode (IDs) of all locations, without loading
                Location aggregates themselves.
             */

            List<UnLocode> unlocodes = queryTemplate.query(AllUnlocodesQueryRow.SQL,
                            new BeanPropertyRowMapper<>(AllUnlocodesQueryRow.class))
                    .stream().map(AllUnlocodesQueryRow::getUnlocode)
                    .map(UnLocode::of)
                    .toList();

            /*
                For each UnLocode, first see if there is a corresponding Location
                already in the cache, if not, then load Location DB entity and
                convert it to Location model updating the cache in the process.
             */
            Cache cache = getLocationCache();
            return unlocodes.stream()
                    .map(unLocode -> cache.get(new SimpleKey(unLocode),
                            () -> locationRepository.findById(unLocode.getCode())
                                    .map(dbMapper::convert)
                                    .orElseThrow(() -> new PersistenceOperationError(
                                            "No location found for %s in the database".formatted(unLocode)))
                    )).toList();
        } catch (Exception e) {
            throw new PersistenceOperationError("Cannot retrieve all locations", e);
        }
    }

    @Transactional(readOnly = true)
    @Override
    public Location obtainLocationByUnLocode(UnLocode unLocode) {
        try {
            /*
                Try to get the location from the cache first. If it is not
                there, load DB entity from the database and convert to
                the model.
             */
            return getLocationCache().get(new SimpleKey(unLocode),
                    () -> dbMapper.convert(locationRepository.findById(unLocode.getCode())
                            .orElseThrow(() -> new PersistenceOperationError(
                                    "No location found for %s in the database".formatted(unLocode)))));
        } catch (Exception e) {
            throw new PersistenceOperationError("Cannot obtain location with unLocode: <%s>"
                    .formatted(unLocode), e);
        }
    }

    @Transactional
    @Override
    public void saveCargo(Cargo cargoToSave) {

        try {
            final CargoDbEntity cargoDbEntity = dbMapper.convert(cargoToSave);

            // save cargo
            cargoRepository.save(cargoDbEntity);
        } catch (Exception e) {
            throw new PersistenceOperationError("Cannot save cargo with tracking ID: <%s>"
                    .formatted(cargoToSave.getTrackingId()), e);
        }

    }

    @Transactional(readOnly = true)
    @Override
    public Cargo obtainCargoByTrackingId(TrackingId trackingId) {
        try {
            CargoDbEntity cargoDbEntity = cargoRepository.findById(trackingId.getId()).orElseThrow();
            return dbMapper.convert(cargoDbEntity);
        } catch (Exception e) {
            throw new PersistenceOperationError("Cannot obtain cargo with tracking ID: <%s>"
                    .formatted(trackingId), e);
        }

    }

    @Transactional
    @Override
    public void deleteCargo(TrackingId trackingId) {
        try {
            cargoRepository.deleteById(trackingId.getId());
        } catch (Exception e) {
            throw new PersistenceOperationError("Cannot delete cargo with tracking ID: <%s>"
                    .formatted(trackingId), e);
        }
    }

    @Transactional(readOnly = true)
    @Override
    public List<ExpectedArrivals> queryForExpectedArrivals() {

        try {
            List<ExpectedArrivalsQueryRow> rows = queryTemplate.query(ExpectedArrivalsQueryRow.SQL,
                    new BeanPropertyRowMapper<>(ExpectedArrivalsQueryRow.class));

            return rows.stream().map(dbMapper::convert).toList();
        } catch (DataAccessException e) {
            throw new PersistenceOperationError("Cannot execute a query for expected arrivals", e);
        }
    }

    @Transactional
    @Override
    public void recordHandlingEvent(HandlingEvent event) {
        try {
            HandlingEventEntity eventEntity = dbMapper.convert(event);
            handlingEventRepository.save(eventEntity);
        } catch (Exception e) {
            throw new PersistenceOperationError("Cannot record handling event %s".formatted(event), e);
        }
    }

    @Transactional(readOnly = true)
    @Override
    public HandlingHistory handlingHistory(TrackingId cargoId) {
        try {
            return HandlingHistory.builder()
                    .handlingEvents(handlingEventRepository.findAllByCargoId(cargoId.getId())
                            .stream().map(dbMapper::convert).toList())
                    .build();
        } catch (Exception e) {
            throw new PersistenceOperationError("Cannot obtain handling history for cargo with tracking ID: %s"
                    .formatted(cargoId), e);
        }
    }

    @Transactional(readOnly = true)
    @Override
    public boolean locationExists(Location location) {

        try {
            return Optional.ofNullable(queryTemplate.queryForObject(LocationExistsQueryRow.SQL,
                            Map.of("unlocode", location.getUnlocode().getCode()),
                            new BeanPropertyRowMapper<>(LocationExistsQueryRow.class)))
                    .orElseThrow().exists();
        } catch (Exception e) {
            throw new PersistenceOperationError("Error when querying if location %s exists already"
                    .formatted(location.getUnlocode()), e);
        }

    }

    @Transactional
    @Override
    public void saveLocation(Location location) {
        try {
            // save location and update the cache
            locationRepository.save(dbMapper.convert(location));
            getLocationCache().put(new SimpleKey(location.getUnlocode()), location);
        } catch (Exception e) {
            throw new PersistenceOperationError("Error when saving location %s"
                    .formatted(location.getUnlocode()), e);
        }
    }

    @Transactional(readOnly = true)
    @Override
    public List<CargoInfo> allCargoes() {

        try {
            List<CargoInfoRow> rows = queryTemplate.query(CargoInfoRow.SQL,
                    new BeanPropertyRowMapper<>(CargoInfoRow.class));

            return rows.stream().map(dbMapper::convert).toList();
        } catch (DataAccessException e) {
            throw new PersistenceOperationError("Error when querying for information about all cargoes", e);
        }
    }

    private Cache getLocationCache() {
        String cacheName = props.getLocationCache().getName();
        return Optional.ofNullable(cacheManager.getCache(cacheName))
                .orElseThrow(() -> new IllegalStateException("Cache %s not found".formatted(cacheName)));
    }
}
