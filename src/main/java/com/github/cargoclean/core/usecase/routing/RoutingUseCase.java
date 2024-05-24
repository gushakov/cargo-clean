package com.github.cargoclean.core.usecase.routing;

import com.github.cargoclean.core.model.cargo.*;
import com.github.cargoclean.core.model.handling.HandlingHistory;
import com.github.cargoclean.core.port.persistence.PersistenceGatewayOutputPort;
import com.github.cargoclean.core.port.routing.RoutingServiceOutputPort;
import com.github.cargoclean.core.port.security.SecurityOutputPort;
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
        try {
            final Cargo cargo;
            securityOps.assertThatUserIsAgent();
            cargo = gatewayOps.obtainCargoByTrackingId(TrackingId.of(cargoTrackingId));
            presenter.presentCargoDetails(cargo);
        } catch (Exception e) {
            presenter.presentError(e);
        }

    }

    @Override
    public void selectItinerary(String cargoTrackingId) {
        try {
            Cargo cargo;
            List<Itinerary> itineraries;
            TrackingId trackingId;

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

            presenter.presentCandidateRoutes(cargo, itineraries);
        } catch (Exception e) {
            presenter.presentError(e);
        }

    }

    @Transactional
    @Override
    public void assignRoute(String trackingId, RouteDto selectedRoute) {

        try {

            securityOps.assertThatUserIsAgent();

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

            // Update: 26.11.2022
            // fixed: update delivery progress of the cargo (routing status, ETA),
            // handling history is empty for newly routed cargo
            Cargo updatedCargo = routedCargo.updateDeliveryProgress(HandlingHistory.EMPTY_HISTORY);

            // persist updated cargo
            gatewayOps.saveCargo(updatedCargo);

            presenter.presentResultOfAssigningRouteToCargo(trackingId);
        } catch (Exception e) {
            presenter.presentError(e);
        }

    }
}
