package com.github.cargoclean.infrastructure.adapter.db.consigment;

import org.springframework.data.repository.CrudRepository;

/**
 * Spring Data JDBC repository for {@link ConsignmentDbEntity} entities.
 */
public interface ConsignmentDbEntityRepository extends CrudRepository<ConsignmentDbEntity, String> {

}