package com.github.cargoclean.infrastructure.adapter.db.cargo;

import org.springframework.data.repository.CrudRepository;

public interface CargoDbEntityRepository extends CrudRepository<CargoDbEntity, Integer> {

    CargoDbEntity findByTrackingId(String trackingId);

}
