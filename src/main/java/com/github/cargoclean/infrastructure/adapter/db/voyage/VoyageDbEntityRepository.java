package com.github.cargoclean.infrastructure.adapter.db.voyage;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface VoyageDbEntityRepository extends CrudRepository<VoyageDbEntity, Integer> {

    Optional<VoyageDbEntity> findByVoyageNumber(String voyageNumber);

}
