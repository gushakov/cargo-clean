package com.github.cargoclean.infrastructure.adapter.db.location;

import org.springframework.data.repository.CrudRepository;

/**
 * Spring Data JDBC repository for {@link LocationDbEntity} entities.
 */
public interface LocationDbEntityRepository extends CrudRepository<LocationDbEntity, Integer> {

    LocationDbEntity findByUnlocode(String unlocode);

}
