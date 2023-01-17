package com.github.cargoclean.infrastructure.adapter.events;

import com.github.cargoclean.core.model.CargoEvent;
import com.github.cargoclean.core.port.operation.events.EventDispatcherOutputPort;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

/*
    Reference:
    ---------

    1.  Baeldung, Spring Events: https://www.baeldung.com/spring-events
 */

@Service
public class CargoSpringEventDispatcher implements EventDispatcherOutputPort {

    private final ApplicationEventPublisher eventPublisher;

    public CargoSpringEventDispatcher(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    @Override
    public void dispatch(CargoEvent cargoEvent) {
        eventPublisher.publishEvent(cargoEvent);
    }
}
