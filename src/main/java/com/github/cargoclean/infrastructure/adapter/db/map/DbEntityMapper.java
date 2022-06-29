package com.github.cargoclean.infrastructure.adapter.db.map;

import com.github.cargoclean.core.model.cargo.Cargo;
import com.github.cargoclean.core.model.cargo.Delivery;
import com.github.cargoclean.core.model.location.Location;
import com.github.cargoclean.infrastructure.adapter.db.CargoDbEntity;
import com.github.cargoclean.infrastructure.adapter.db.DeliveryDbEntity;
import com.github.cargoclean.infrastructure.adapter.db.LocationDbEntity;

public interface DbEntityMapper {

    Location convert(LocationDbEntity locationDbEntity);

    CargoDbEntity convert(Cargo cargo);

    Cargo convert(CargoDbEntity cargoDbEntity);

}
