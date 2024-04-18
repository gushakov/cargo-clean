package com.github.cargoclean.core.port.events;

import com.github.cargoclean.core.model.CargoEvent;

public interface EventDispatcherOutputPort {

    void dispatch(CargoEvent cargoEvent);

}
