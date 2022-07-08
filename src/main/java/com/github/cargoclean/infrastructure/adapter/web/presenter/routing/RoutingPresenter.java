package com.github.cargoclean.infrastructure.adapter.web.presenter.routing;

import com.github.cargoclean.core.model.cargo.Cargo;
import com.github.cargoclean.core.model.cargo.Itinerary;
import com.github.cargoclean.core.port.presenter.routing.RoutingPresenterOutputPort;
import com.github.cargoclean.infrastructure.adapter.web.presenter.AbstractWebPresenter;
import com.github.cargoclean.infrastructure.adapter.web.presenter.LocalDispatcherServlet;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@Scope(scopeName = "request")
public class RoutingPresenter extends AbstractWebPresenter implements RoutingPresenterOutputPort {
    public RoutingPresenter(LocalDispatcherServlet dispatcher, HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
        super(dispatcher, httpRequest, httpResponse);
    }

    @Override
    public void presentCargoForRouting(Cargo cargo) {

        /*
            We are creating a "Response Model" (flat POJO) object
            tailored specifically to the needs of the view at hand.
            It is different from the CA's principles, because we
            are crossing the boundary between Use Cases
            layer and the Interface Adapters layer with a domain
            object as a parameter (instead of a DTO).
         */

        final CargoInfoDto dto = CargoInfoDto.builder()
                .trackingId(cargo.getTrackingId().getId())
                .origin(cargo.getOrigin().getCode())
                .destination(cargo.getRouteSpecification().getDestination().getCode())
                .arrivalDeadline(cargo.getRouteSpecification().getArrivalDeadline()
                        .format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))
                .routed(cargo.isRouted())
                .build();

        presentModelAndView(Map.of("cargo", dto), "show");

    }

    @Override
    public void presentCandidateItinerariesForSelection(Cargo cargo, List<Itinerary> itineraries) {

        // construct the Response Model with candidate routes, convert all domain models
        // to DTOs

        ItineraryAssigmentForm itineraryAssigmentForm = ItineraryAssigmentForm.builder()
                .trackingId(cargo.getTrackingId())
                .cargoOrigin(cargo.getOrigin())
                .cargoDestination(cargo.getRouteSpecification().getDestination())
                .candidateRoutes(itineraries.stream().map(itinerary -> CandidateRouteDto.builder()
                        .legs(itinerary.getLegs().stream().map(LegDto::of).toList())
                        .build()).toList())
                .selectedRoute(null)
                .build();

        presentModelAndView(Map.of("itineraryAssigmentForm", itineraryAssigmentForm), "select-itinerary");
    }
}
