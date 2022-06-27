package com.github.cargoclean.infrastructure.adapter.web.presenter.booking;

import com.github.cargoclean.core.model.location.Location;
import com.github.cargoclean.core.port.presenter.booking.BookingPresenterOutputPort;
import com.github.cargoclean.infrastructure.adapter.web.presenter.LocalDispatcherServlet;
import com.github.cargoclean.infrastructure.adapter.web.presenter.AbstractWebPresenter;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * Presenter for the view where we can enter information for booking of a new cargo.
 * Scope "request" is needed since we need to autowire the current {@code HttpServletRequest}
 * and {@code HttpServletResponse} objects.
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
    public void presentNewCargoBookingView(List<Location> locations, LocalDate dateNow) {

        // store the locations and the date in the map to be used in the view-model

        Map<String, Object> responseModel = Map.of("locations", locations,
                "dateNow", dateNow);

        presentModelAndView(responseModel, "book-new-cargo");

    }
}
