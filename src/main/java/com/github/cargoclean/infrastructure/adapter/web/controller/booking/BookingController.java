package com.github.cargoclean.infrastructure.adapter.web.controller.booking;

import com.github.cargoclean.core.usecase.booking.BookingInputPort;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
public class BookingController {

    private final ApplicationContext appContext;

    @RequestMapping("/bookNewCargo")
    @ResponseBody
    public void bookNewCargo() {
        /*
            Entry point to "Book new cargo" command processing. We get the input port
            for the needed use case from the application context and we transfer the
            control to the use case. There is no input to process or any request model
            to pass to the use case.
         */

        BookingInputPort useCase = appContext.getBean(BookingInputPort.class);

        useCase.prepareNewCargoBooking();
    }


}
