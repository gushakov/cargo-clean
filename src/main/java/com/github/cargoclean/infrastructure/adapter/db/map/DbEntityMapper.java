package com.github.cargoclean.infrastructure.adapter.db.map;

import com.github.cargoclean.core.model.location.Location;
import com.github.cargoclean.infrastructure.adapter.db.LocationDbEntity;

public interface DbEntityMapper {

    Location convert(LocationDbEntity locationDbEntity);

}
