package com.github.cargoclean.infrastructure.adapter.web.controller.routing;

import com.github.cargoclean.core.model.cargo.TrackingId;
import com.github.cargoclean.core.usecase.routing.RoutingInputPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
@Slf4j
public class RoutingController {

    private final ApplicationContext appContext;

    @RequestMapping("/showCargoDetails")
    @ResponseBody
    public void showCargoDetails(@RequestParam String trackingId) {
        log.debug("[Routing] Showing cargo details of tracking ID: {}", trackingId);

        RoutingInputPort useCase = appContext.getBean(RoutingInputPort.class);

        useCase.showCargo(TrackingId.of(trackingId));

    }

}
