package com.github.cargoclean.core.usecase.routing;

import com.github.cargoclean.core.model.cargo.Cargo;
import com.github.cargoclean.core.model.cargo.Itinerary;
import com.github.cargoclean.core.model.cargo.RouteSpecification;
import com.github.cargoclean.core.model.cargo.TrackingId;
import com.github.cargoclean.core.port.operation.PersistenceGatewayOutputPort;
import com.github.cargoclean.core.port.operation.RoutingServiceOutputPort;
import com.github.cargoclean.core.port.presenter.routing.RoutingPresenterOutputPort;
import com.github.cargoclean.core.validator.Validator;
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

            // FIXME: show error is cargo is already routed


            // get route specification for cargo
            RouteSpecification routeSpecification = cargo.getRouteSpecification();

            // get candidate itineraries from external service
            itineraries = routingServiceOps.fetchRoutesForSpecification(trackingId, routeSpecification);


        } catch (Exception e) {
            presenter.presentError(e);
            return;
        }

        presenter.presentCandidateItinerariesForSelection(cargo, itineraries);
    }
}
