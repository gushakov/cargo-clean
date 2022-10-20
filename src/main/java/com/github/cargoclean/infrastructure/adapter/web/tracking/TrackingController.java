package com.github.cargoclean.infrastructure.adapter.web.tracking;

import com.github.cargoclean.core.usecase.tracking.TrackingInputPort;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
public class TrackingController {

    private final ApplicationContext appContext;

    @RequestMapping("/trackCargo")
    @ResponseBody
    public void bookNewCargo() {

        useCase().trackCargo();
    }


    private TrackingInputPort useCase(){
        return appContext.getBean(TrackingInputPort.class);
    }

}
