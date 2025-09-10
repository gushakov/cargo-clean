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

@Component
@Scope(scopeName = WebApplicationContext.SCOPE_REQUEST)
public class ConsignmentPresenter extends AbstractWebPresenter implements ConsignmentPresenterOutputPort {

    public ConsignmentPresenter(LocalDispatcherServlet dispatcher, HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
        super(dispatcher, httpRequest, httpResponse);
    }

    @Override
    public void presentConsignmentAdded(String consignmentId, String cargoTrackingId) {

    }

    @Override
    public void presentErrorWhenConsignmentCouldNotBeAdded(String consignmentId, String cargoTrackingId, String message) {

    }

    @Override
    public void presentConsignmentEntryForm(String cargoTrackingId) {
        final ConsignmentEntryForm consignmentEntryForm = ConsignmentEntryForm.builder()
                .cargoTrackingId(cargoTrackingId)
                .build();
        Map<String, Object> responseModel = Map.of("consignmentEntryForm", consignmentEntryForm);
        presentModelAndView(responseModel, "register-new-consignment");
    }
}
