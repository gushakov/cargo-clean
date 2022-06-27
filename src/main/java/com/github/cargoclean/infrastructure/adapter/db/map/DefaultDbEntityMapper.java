package com.github.cargoclean.infrastructure.adapter.db.map;

import com.github.cargoclean.core.model.location.Location;
import com.github.cargoclean.core.model.location.UnLocode;
import com.github.cargoclean.infrastructure.adapter.db.LocationDbEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * MapStruct will generate a mapper for us in {@code com.github.cargoclean.infrastructure.adapter.db.map.DefaultDbEntityMapperImpl}.
 */
@Mapper(componentModel = "spring")
public abstract class DefaultDbEntityMapper implements DbEntityMapper {

    // we need to tell MapStruct how to map from strings to UnLocode
    // value objects
    protected UnLocode map(String unlocode){
        return UnLocode.builder()
                .code(unlocode)
                .build();
    };

    @Mapping(target = "unLocode", source = "unlocode")
    abstract Location map(LocationDbEntity locationDbEntity);

    @Override
    public Location convert(LocationDbEntity locationDbEntity) {
        return map(locationDbEntity);
    }
}
