package com.github.cargoclean.infrastructure.adapter.web.tracking;

import com.github.cargoclean.core.usecase.tracking.TrackingInputPort;
import com.github.cargoclean.infrastructure.adapter.web.AbstractWebController;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
public class TrackingController extends AbstractWebController {

    private final ApplicationContext appContext;

    @RequestMapping("/cargoTracking")
    @ResponseBody
    public void initializeTracking() {
        useCase().initializeCargoTrackingView();
    }

    @PostMapping("/trackCargo")
    @ResponseBody
    public void trackCargo(@ModelAttribute TrackingForm trackingId) {
        useCase().trackCargo(trackingId.getTrackingId());
    }

    private TrackingInputPort useCase() {
        return appContext.getBean(TrackingInputPort.class);
    }

}
