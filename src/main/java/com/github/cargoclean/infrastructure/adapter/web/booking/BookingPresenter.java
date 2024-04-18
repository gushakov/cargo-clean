package com.github.cargoclean.infrastructure.adapter.web.booking;

import com.github.cargoclean.core.model.cargo.TrackingId;
import com.github.cargoclean.core.model.location.Location;
import com.github.cargoclean.core.model.location.UnLocode;
import com.github.cargoclean.core.usecase.booking.BookingPresenterOutputPort;
import com.github.cargoclean.infrastructure.adapter.web.AbstractWebPresenter;
import com.github.cargoclean.infrastructure.adapter.web.LocalDispatcherServlet;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Presenter for the view where we can enter information for booking of a new cargo.
 * Scope "request" is needed since we need to autowire the current {@code HttpServletRequest}
 * and {@code HttpServletResponse} objects (declared in the parent).
 *
 * @see AbstractWebPresenter
 */
@Component
@Scope(scopeName = WebApplicationContext.SCOPE_REQUEST)
public class BookingPresenter extends AbstractWebPresenter implements BookingPresenterOutputPort {
    public BookingPresenter(LocalDispatcherServlet dispatcher, HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
        super(dispatcher, httpRequest, httpResponse);
    }

    @Override
    public void presentNewCargoBookingView(List<Location> locations) {

        /*
            Point of interest:
            -----------------
            Notice that here we are deviating from the CA principles:
            this Presenter actually received a list of domain models,
            and it is here that we shall create the "Response Model"
            based on the need of a specific mode of presentation:
            Thymeleaf template with a backing bean.
         */

        final List<String> allUnLocodes = locations.stream().map(Location::getUnlocode).map(UnLocode::getCode).toList();

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
