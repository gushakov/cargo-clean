package com.github.cargoclean.infrastructure.adapter.web.consignment;

import com.github.cargoclean.core.usecase.consignment.ConsignmentPresenterOutputPort;
import com.github.cargoclean.infrastructure.adapter.web.AbstractWebPresenter;
import com.github.cargoclean.infrastructure.adapter.web.LocalDispatcherServlet;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * Presenter for consignment-related views.
 * Scope "request" is needed since we need to autowire the current {@code HttpServletRequest}
 * and {@code HttpServletResponse} objects (declared in the parent).
 *
 * @see AbstractWebPresenter
 */
@Component
@Scope(scopeName = WebApplicationContext.SCOPE_REQUEST)
public class ConsignmentPresenter extends AbstractWebPresenter implements ConsignmentPresenterOutputPort {

    public ConsignmentPresenter(LocalDispatcherServlet dispatcher, HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
        super(dispatcher, httpRequest, httpResponse);
    }

    @Override
    public void presentConsignmentAdded(String consignmentId, String cargoTrackingId) {
        // Redirect to a page showing the details of the cargo with the new consignment
        redirect("/showCargoDetails", Map.of("trackingId", cargoTrackingId));
    }

    @Override
    public void presentErrorWhenConsignmentCouldNotBeAdded(String consignmentId, String cargoTrackingId, String message) {
        // Present an error view, possibly with details about why the consignment could not be added
        Map<String, Object> responseModel = Map.of(
                "errorMessage", message,
                "consignmentId", consignmentId,
                "cargoTrackingId", cargoTrackingId
        );
        presentModelAndView(responseModel, "error"); // Assuming an error.html template exists
    }

    @Override
    public void presentConsignmentEntryForm(String cargoTrackingId) {
        // Create a new ConsignmentEntryForm and present the view for adding a new consignment
        final ConsignmentEntryForm consignmentEntryForm = ConsignmentEntryForm.builder()
                .cargoTrackingId(cargoTrackingId)
                .build();

        Map<String, Object> responseModel = Map.of("consignmentEntryForm", consignmentEntryForm);

        // present view
        presentModelAndView(responseModel, "add-consignment"); // Assuming an add-consignment.html template exists
    }
}

