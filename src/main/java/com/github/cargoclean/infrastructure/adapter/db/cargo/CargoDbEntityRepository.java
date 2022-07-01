package com.github.cargoclean.infrastructure.adapter.db.cargo;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface CargoDbEntityRepository extends CrudRepository<CargoDbEntity, Integer> {

    Optional<CargoDbEntity> findByTrackingId(String trackingId);

}
