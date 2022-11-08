package com.github.cargoclean.infrastructure.adapter.web.booking;

import com.github.cargoclean.core.usecase.booking.BookingInputPort;
import com.github.cargoclean.infrastructure.adapter.web.AbstractWebController;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
public class BookingController extends AbstractWebController {

    private final ApplicationContext appContext;

    /*
        Request to prepare new cargo booking (view). We need to
        execute the use case which will load all the needed information.
     */

    @RequestMapping("/bookNewCargo")
    @ResponseBody
    public void bookNewCargo() {

        BookingInputPort useCase = appContext.getBean(BookingInputPort.class);
        useCase.prepareNewCargoBooking();
    }

    /*
        This is where we handle the booking request. The model attribute
        "bookingForm" will contain the parameters for new booking set
        by the user.
     */

    @RequestMapping(method = RequestMethod.POST, value = "/book")
    @ResponseBody
    public void book(@ModelAttribute BookingForm bookingForm) {

        BookingInputPort useCase = appContext.getBean(BookingInputPort.class);
        useCase.bookCargo(bookingForm.getOrigin(), bookingForm.getDestination(),
                bookingForm.getDeliveryDeadline());

    }
}
