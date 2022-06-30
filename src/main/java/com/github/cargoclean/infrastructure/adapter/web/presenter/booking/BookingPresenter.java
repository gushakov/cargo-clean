package com.github.cargoclean.infrastructure.adapter.web.presenter.booking;

import com.github.cargoclean.core.model.cargo.TrackingId;
import com.github.cargoclean.core.port.presenter.booking.BookingPresenterOutputPort;
import com.github.cargoclean.infrastructure.adapter.web.presenter.LocalDispatcherServlet;
import com.github.cargoclean.infrastructure.adapter.web.presenter.AbstractWebPresenter;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Presenter for the view where we can enter information for booking of a new cargo.
 * Scope "request" is needed since we need to autowire the current {@code HttpServletRequest}
 * and {@code HttpServletResponse} objects (from the parent).
 *
 * @see AbstractWebPresenter
 */
@Component
@Scope(scopeName = "request")
public class BookingPresenter extends AbstractWebPresenter implements BookingPresenterOutputPort {
    public BookingPresenter(LocalDispatcherServlet dispatcher, HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
        super(dispatcher, httpRequest, httpResponse);
    }

    @Override
    public void presentNewCargoBookingView(List<String> allUnLocodes) {

        // create the form bean which will be used in the view
        final BookingForm bookingForm = BookingForm.builder()
                .deliveryDeadline(new Date(System.currentTimeMillis()))
                .locations(allUnLocodes)
                .build();

        Map<String, Object> responseModel = Map.of("bookingForm", bookingForm);

        // present view
        presentModelAndView(responseModel, "book-new-cargo");

    }

    @Override
    public void presentResultOfNewCargoBooking(TrackingId trackingId) {

        // redirect to cargo routing (show details of the cargo)
        redirect("/showCargoDetails", Map.of("trackingId", trackingId.getId()));

    }
}
