package com.github.cargoclean.core.usecase.routing;

import com.github.cargoclean.core.model.cargo.Cargo;
import com.github.cargoclean.core.model.cargo.Itinerary;
import com.github.cargoclean.core.model.cargo.RouteSpecification;
import com.github.cargoclean.core.model.cargo.TrackingId;
import com.github.cargoclean.core.port.operation.PersistenceGatewayOutputPort;
import com.github.cargoclean.core.port.operation.RoutingServiceOutputPort;
import com.github.cargoclean.core.port.presenter.routing.RoutingPresenterOutputPort;
import com.github.cargoclean.core.validator.Validator;
import com.github.cargoclean.infrastructure.adapter.web.presenter.routing.CandidateRouteDto;
import com.github.cargoclean.infrastructure.adapter.web.presenter.routing.LegDto;
import com.github.cargoclean.infrastructure.adapter.web.presenter.routing.RoutingError;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
public class RoutingUseCase implements RoutingInputPort {

    private final RoutingPresenterOutputPort presenter;

    // validation service
    private final Validator validator;

    private final PersistenceGatewayOutputPort gatewayOps;

    // output port for external routing service
    private final RoutingServiceOutputPort routingServiceOps;

    @Override
    public void showCargo(TrackingId trackingId) {
        final Cargo cargo;
        try {
            cargo = validator.validate(gatewayOps.obtainCargoByTrackingId(trackingId));
        } catch (Exception e) {
            presenter.presentError(e);
            return;
        }

        presenter.presentCargoForRouting(cargo);

    }

    @Override
    public void selectItinerary(TrackingId trackingId) {
        Cargo cargo;
        List<Itinerary> itineraries;
        try {

            // load cargo
            cargo = validator.validate(gatewayOps.obtainCargoByTrackingId(trackingId));

            if (cargo.isRouted()) {
                throw new RoutingError("Cargo <%s> is already routed.".formatted(trackingId));
            }

            // get route specification for cargo
            RouteSpecification routeSpecification = cargo.getRouteSpecification();

            // get candidate itineraries from external service
            itineraries = routingServiceOps.fetchRoutesForSpecification(trackingId, routeSpecification);


        } catch (Exception e) {
            presenter.presentError(e);
            return;
        }

        presenter.presentCandidateRoutes(cargo, itineraries);
    }

    @Override
    public void assignRoute(String trackingId, CandidateRouteDto selectedRoute) {

        try {

            // make sure we retrieved candidate route from the session successfully
            if (selectedRoute == null) {
                throw new RoutingError("Cannot route cargo <%s>: no route selected."
                        .formatted(trackingId));
            }

            // convert selected route DTO to Itinerary
            Itinerary itinerary = Itinerary.of(selectedRoute.getLegs().stream()
                    .map(LegDto::toLeg)
                    .toList());

            // load cargo
            Cargo cargo = gatewayOps.obtainCargoByTrackingId(TrackingId.of(trackingId));

            // make sure the cargo needs routing
            if (cargo.isRouted()){
                throw new RoutingError("Cargo <%s> is already routed.".formatted(trackingId));
            }

            // make sure selected itinerary satisfies the route specification for the cargo
            if (!cargo.getRouteSpecification().isSatisfiedBy(itinerary)){
                throw new RoutingError("Provided itinerary does not satisfy route specification for cargo <%s>."
                        .formatted(trackingId));
            }

            // actually route this cargo and validate
            Cargo routedCargo = validator.validate(cargo.assignItinerary(itinerary));

            // persist updated cargo
            gatewayOps.saveCargo(routedCargo);

        } catch (Exception e) {
            presenter.presentError(e);
            return;
        }

        presenter.presentResultOfAssigningRouteToCargo(trackingId);

    }
}
