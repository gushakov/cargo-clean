package com.github.cargoclean.infrastructure.adapter.db.handling;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface HandlingEventEntityRepository extends CrudRepository<HandlingEventEntity, Long> {

    List<HandlingEventEntity> findAllByCargoId(String cargoId);

}
