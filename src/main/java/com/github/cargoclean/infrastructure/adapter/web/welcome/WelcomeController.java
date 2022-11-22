package com.github.cargoclean.infrastructure.adapter.web.welcome;

import com.github.cargoclean.core.usecase.welcome.WelcomeInputPort;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
public class WelcomeController {

    private final ApplicationContext appContext;

    @RequestMapping("/")
    @ResponseBody
    public void welcome() {
        useCase().welcome();
    }

    private WelcomeInputPort useCase() {
        return appContext.getBean(WelcomeInputPort.class);
    }

}
