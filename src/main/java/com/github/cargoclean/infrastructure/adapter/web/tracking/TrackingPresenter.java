package com.github.cargoclean.infrastructure.adapter.web.tracking;

import com.github.cargoclean.core.model.cargo.Cargo;
import com.github.cargoclean.core.port.presenter.tracking.TrackingPresenterOutputPort;
import com.github.cargoclean.infrastructure.adapter.web.AbstractWebPresenter;
import com.github.cargoclean.infrastructure.adapter.web.LocalDispatcherServlet;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Component
@Scope(scopeName = "request")
public class TrackingPresenter extends AbstractWebPresenter implements TrackingPresenterOutputPort {
    public TrackingPresenter(LocalDispatcherServlet dispatcher, HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
        super(dispatcher, httpRequest, httpResponse);
    }

    @Override
    public void presentInitialViewForCargoTracking() {
        presentModelAndView(Map.of("trackingForm", TrackingForm.builder().build()), "track-cargo");
    }

    @Override
    public void presentCargoTrackingInformation(Cargo cargo) {
        presentModelAndView(Map.of("trackingForm", TrackingForm.builder().build(),
                "trackingInfo", TrackingInfo.builder()
                                .trackingId(cargo.getTrackingId().toString())
                                .delivery(cargo.getDelivery())
                        .build()), "track-cargo");
    }
}
