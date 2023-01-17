package com.github.cargoclean.infrastructure.adapter.events;

import com.github.cargoclean.core.model.CargoEvent;
import com.github.cargoclean.core.model.cargo.TrackingId;
import com.github.cargoclean.core.model.handling.HandlingEvent;
import com.github.cargoclean.core.usecase.handling.HandlingUseCase;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

/*
    Reference:
    ---------

    1.  Baeldung, Spring Events: https://www.baeldung.com/spring-events
 */
@Service
@Slf4j
public class CargoSpringEventProcessingAdapter {

    private final ApplicationContext appContext;

    public CargoSpringEventProcessingAdapter(ApplicationContext appContext) {
        this.appContext = appContext;
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleCargoEvent(CargoEvent event){

        if (event instanceof HandlingEvent handlingEvent){
            TrackingId trackingId = handlingEvent.getCargoId();
            log.debug("[Event] Handling event for cargo {} was recorded, will update delivery history.", trackingId);
            HandlingUseCase useCase = appContext.getBean(HandlingUseCase.class);
            useCase.updateDeliveryAfterHandlingActivity(trackingId.toString());
        }

    }

}
