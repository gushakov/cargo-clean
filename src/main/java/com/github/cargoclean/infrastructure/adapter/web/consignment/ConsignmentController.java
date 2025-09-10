package com.github.cargoclean.infrastructure.adapter.web.consignment;

import com.github.cargoclean.core.usecase.consignment.ConsignmentInputPort;
import com.github.cargoclean.infrastructure.adapter.web.AbstractWebController;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
public class ConsignmentController extends AbstractWebController {

    private final ApplicationContext appContext;

    /*
        This method handles the request to display a form for adding a new consignment entry.
        It retrieves the ConsignmentInputPort from the application context
        and calls the 'agentAddsNewConsignment' method, which will prepare the view.
     */
    @RequestMapping("/addConsignment")
    @ResponseBody
    public void agentAddsNewConsignment() {
        ConsignmentInputPort useCase = appContext.getBean(ConsignmentInputPort.class);
        // Assuming ConsignmentInputPort will have an 'agentAddsNewConsignment' method
        useCase.agentAddsNewConsignment();
    }
}
