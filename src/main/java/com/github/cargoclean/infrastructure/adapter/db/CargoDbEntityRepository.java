package com.github.cargoclean.infrastructure.adapter.db;

import org.springframework.data.repository.CrudRepository;

public interface CargoDbEntityRepository extends CrudRepository<CargoDbEntity, Integer> {

    CargoDbEntity findByTrackingId(String trackingId);

}
