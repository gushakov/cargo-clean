package com.github.cargoclean.infrastructure.adapter.web.consignment;

import com.github.cargoclean.core.usecase.consignment.ConsignmentInputPort;
import com.github.cargoclean.infrastructure.adapter.web.AbstractWebController;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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

    /**
     * Handles the POST request from the register-new-consignment.html form.
     * It retrieves the ConsignmentInputPort and calls the agentAssignsConsignmentToCargo method.
     *
     * @param consignmentEntryForm The form data submitted by the user.
     */
    @PostMapping("/registerConsignment")
    @ResponseBody
    public void handleRegisterConsignment(@ModelAttribute ConsignmentEntryForm consignmentEntryForm) {
        log.debug("[Consignment] Handling new consignment registration for cargo tracking ID: {} with quantity: {}",
                consignmentEntryForm.getCargoTrackingId(), consignmentEntryForm.getQuantityInContainers());

        ConsignmentInputPort useCase = appContext.getBean(ConsignmentInputPort.class);
        useCase.agentAssignsNewConsignmentToCargo(consignmentEntryForm.getCargoTrackingId(), consignmentEntryForm.getQuantityInContainers());
    }
}
