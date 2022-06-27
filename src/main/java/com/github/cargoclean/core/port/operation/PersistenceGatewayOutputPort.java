package com.github.cargoclean.core.port.operation;

import com.github.cargoclean.core.model.location.Location;

import java.util.List;

public interface PersistenceGatewayOutputPort {

    List<Location> allLocations();

}
