package com.github.cargoclean.infrastructure.adapter.web.routing;

import com.github.cargoclean.core.model.cargo.RouteDto;
import com.github.cargoclean.core.model.cargo.TrackingId;
import com.github.cargoclean.core.usecase.routing.RoutingInputPort;
import com.github.cargoclean.infrastructure.adapter.web.AbstractWebController;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.List;

/*
    References:
    ----------

    1. Thymeleaf, session attributes: https://www.baeldung.com/spring-mvc-session-attributes

 */


@Controller
@RequiredArgsConstructor
@Slf4j
public class RoutingController extends AbstractWebController {

    private final ApplicationContext appContext;

    @RequestMapping("/showCargoDetails")
    @ResponseBody
    public void showCargoDetails(@RequestParam String trackingId) {
        log.debug("[Routing] Showing cargo details of tracking ID: {}", trackingId);

        RoutingInputPort useCase = appContext.getBean(RoutingInputPort.class);

        useCase.showCargo(TrackingId.of(trackingId));

    }

    @RequestMapping("/selectItinerary")
    @ResponseBody
    public void routeCargo(@RequestParam String trackingId) {
        log.debug("[Routing] Showing cargo details of tracking ID: {}", trackingId);

        RoutingInputPort useCase = appContext.getBean(RoutingInputPort.class);

        useCase.selectItinerary(TrackingId.of(trackingId));

    }

    @RequestMapping(method = RequestMethod.POST, value = "/assignRoute",
            consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    @ResponseBody
    public void assignRoute(@RequestParam String trackingId,
                            @RequestParam Integer selectedRouteIndex,
                            HttpSession session) {

        RoutingInputPort useCase = appContext.getBean(RoutingInputPort.class);

        // get the selected route by accessing the saved list of candidate routes
        String sessionAttrName = "cargo.%s.candidate.routes".formatted(trackingId.toLowerCase());
        List<RouteDto> candidateRoutes = (List<RouteDto>) session
                .getAttribute(sessionAttrName);
        session.removeAttribute(sessionAttrName);

        RouteDto candidateRouteDto = candidateRoutes.get(selectedRouteIndex);
        useCase.assignRoute(trackingId, candidateRouteDto);

    }

}
