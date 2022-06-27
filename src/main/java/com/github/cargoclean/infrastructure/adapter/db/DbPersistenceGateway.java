package com.github.cargoclean.infrastructure.adapter.db;

import com.github.cargoclean.core.model.location.Location;
import com.github.cargoclean.core.port.operation.PersistenceGatewayOutputPort;
import com.github.cargoclean.infrastructure.adapter.db.map.DbEntityMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
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

    private final LocationDbEntityRepository locationRepo;
    private final DbEntityMapper dbMapper;

    @Override
    public List<Location> allLocations() {
        return toStream(locationRepo.findAll())
                .map(dbMapper::convert).toList();
    }

    private <T> Stream<T> toStream(Iterable<T> iterable) {
        return StreamSupport.stream(iterable.spliterator(), false);
    }
}
