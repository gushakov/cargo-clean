package com.github.cargoclean.infrastructure.adapter.web.consignment;

import com.github.cargoclean.core.usecase.consignment.ConsignmentInputPort;
import com.github.cargoclean.infrastructure.adapter.web.AbstractWebController;
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
public class ConsignmentController extends AbstractWebController {

    private final ApplicationContext appContext;

    @RequestMapping("/addNewConsignment")
    @ResponseBody
    public void registerNewConsignment(@RequestParam("trackingId") String cargoTrackingId) {
        log.debug("[Consignment] Registering new consignment for cargo with tracking ID: {}", cargoTrackingId);
        ConsignmentInputPort useCase = appContext.getBean(ConsignmentInputPort.class);
        useCase.agentInitializesConsignmentEntry(cargoTrackingId);
    }
}
