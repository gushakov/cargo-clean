package com.github.cargoclean.infrastructure.adapter.web.controller.report;

import com.github.cargoclean.core.usecase.report.ReportInputPort;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
public class ReportController {

    private final ApplicationContext appContext;

    @RequestMapping("/reportExpectedArrivals")
    @ResponseBody
    public void reportExpectedArrivals() {
        ReportInputPort useCase = appContext.getBean(ReportInputPort.class);
        useCase.reportExpectedArrivals();
    }

}
