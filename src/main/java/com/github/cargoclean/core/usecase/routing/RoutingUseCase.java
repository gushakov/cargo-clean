package com.github.cargoclean.core.usecase.routing;

import com.github.cargoclean.core.GenericCargoError;
import com.github.cargoclean.core.model.cargo.*;
import com.github.cargoclean.core.port.operation.PersistenceGatewayOutputPort;
import com.github.cargoclean.core.port.operation.RoutingServiceOutputPort;
import com.github.cargoclean.core.port.operation.security.SecurityOutputPort;
import com.github.cargoclean.core.port.presenter.routing.RoutingPresenterOutputPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.transaction.Transactional;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
public class RoutingUseCase implements RoutingInputPort {

    private final RoutingPresenterOutputPort presenter;

    private final SecurityOutputPort securityOps;

    private final PersistenceGatewayOutputPort gatewayOps;

    // output port for external routing service
    private final RoutingServiceOutputPort routingServiceOps;

    @Override
    public void showCargo(String cargoTrackingId) {
        final Cargo cargo;
        try {
            securityOps.assertThatUserIsAgent();
            cargo = gatewayOps.obtainCargoByTrackingId(TrackingId.of(cargoTrackingId));
        } catch (Exception e) {
            presenter.presentError(e);
            return;
        }

        presenter.presentCargoDetails(cargo);

    }

    @Override
    public void selectItinerary(String cargoTrackingId) {
        Cargo cargo;
        List<Itinerary> itineraries;
        TrackingId trackingId;
        try {

            securityOps.assertThatUserIsAgent();

            trackingId = TrackingId.of(cargoTrackingId);

            // load cargo
            cargo = gatewayOps.obtainCargoByTrackingId(trackingId);

            if (cargo.isRouted()) {
                throw new RoutingError("Cargo <%s> is already routed.".formatted(trackingId.toString()));
            }

            // get route specification for cargo
            RouteSpecification routeSpecification = cargo.getRouteSpecification();

            // get candidate itineraries from external service
            itineraries = routingServiceOps.fetchRoutesForSpecification(trackingId, routeSpecification);

        } catch (GenericCargoError e) {
            presenter.presentError(e);
            return;
        }

        presenter.presentCandidateRoutes(cargo, itineraries);
    }

    @Transactional
    @Override
    public void assignRoute(String trackingId, RouteDto selectedRoute) {

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

            /*
                Point of interest:
                -----------------
                This is how we can do domain object security in the use case.
                We check that user has permission to route the cargo through
                the selected itinerary.
             */

            securityOps.assertThatUserHasPermissionToRouteCargoThroughRegions(itinerary,
                    gatewayOps.allRegionsMap());

            // actually route this cargo
            Cargo routedCargo = cargo.assignItinerary(itinerary);

            // persist updated cargo
            gatewayOps.saveCargo(routedCargo);

        } catch (GenericCargoError e) {
            gatewayOps.rollback();
            presenter.presentError(e);
            return;
        }

        presenter.presentResultOfAssigningRouteToCargo(trackingId);

    }
}
