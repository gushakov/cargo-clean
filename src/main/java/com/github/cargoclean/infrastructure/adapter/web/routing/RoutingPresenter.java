package com.github.cargoclean.infrastructure.adapter.web.routing;

import com.github.cargoclean.core.model.cargo.Cargo;
import com.github.cargoclean.core.model.cargo.Itinerary;
import com.github.cargoclean.core.port.routing.CargoInfoDto;
import com.github.cargoclean.core.port.routing.LegDto;
import com.github.cargoclean.core.port.routing.RouteDto;
import com.github.cargoclean.core.usecase.routing.RoutingPresenterOutputPort;
import com.github.cargoclean.infrastructure.adapter.web.AbstractWebPresenter;
import com.github.cargoclean.infrastructure.adapter.web.LocalDispatcherServlet;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Component
@Scope(scopeName = "request")
public class RoutingPresenter extends AbstractWebPresenter implements RoutingPresenterOutputPort {
    public RoutingPresenter(LocalDispatcherServlet dispatcher, HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
        super(dispatcher, httpRequest, httpResponse);
    }

    @Override
    public void presentCargoDetails(Cargo cargo) {

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
                .routeDto(mapItineraryToRouteDto(cargo.getItinerary()))
                .build();

        presentModelAndView(Map.of("cargo", dto), "show");

    }

    @Override
    public void presentCandidateRoutes(Cargo cargo, List<Itinerary> itineraries) {

        // construct a Response Model by converting itineraries to RouteDto DTOs
        List<RouteDto> candidateRoutes = itineraries.stream().map(this::mapItineraryToRouteDto).toList();

        // store candidate routes in session so that we can access them later
        storeInSession("cargo.%s.candidate.routes".formatted(cargo.getTrackingId().getId().toLowerCase()), candidateRoutes);

        ItineraryAssigmentForm itineraryAssigmentForm = ItineraryAssigmentForm.builder()
                .trackingId(cargo.getTrackingId())
                .cargoOrigin(cargo.getOrigin())
                .cargoDestination(cargo.getRouteSpecification().getDestination())
                .candidateRoutes(candidateRoutes)
                .build();

        presentModelAndView(Map.of("itineraryAssigmentForm", itineraryAssigmentForm), "select-itinerary");
    }

    private RouteDto mapItineraryToRouteDto(Itinerary itinerary) {

        if (itinerary == null) {
            return null;
        }

        return RouteDto.builder()
                .legs(itinerary.getLegs().stream().map(LegDto::of).toList())
                .build();
    }

    @Override
    public void presentResultOfAssigningRouteToCargo(String trackingId) {
        redirect("/showCargoDetails", Map.of("trackingId", trackingId));
    }
}
