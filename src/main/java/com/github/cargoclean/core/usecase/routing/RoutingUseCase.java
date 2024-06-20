package com.github.cargoclean.core.usecase.routing;

import com.github.cargoclean.core.model.cargo.*;
import com.github.cargoclean.core.model.handling.HandlingHistory;
import com.github.cargoclean.core.port.persistence.PersistenceGatewayOutputPort;
import com.github.cargoclean.core.port.routing.RoutingServiceOutputPort;
import com.github.cargoclean.core.port.security.SecurityOutputPort;
import com.github.cargoclean.core.port.transaction.TransactionOperationsOutputPort;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
@Slf4j
public class RoutingUseCase implements RoutingInputPort {

    RoutingPresenterOutputPort presenter;

    SecurityOutputPort securityOps;

    PersistenceGatewayOutputPort gatewayOps;

    // output port for external routing service
    RoutingServiceOutputPort routingServiceOps;

    TransactionOperationsOutputPort txOps;

    @Override
    public void showCargo(String cargoTrackingId) {
        try {

            txOps.doInTransaction(true, () -> {

                securityOps.assertThatUserIsAgent();

                final Cargo cargo = gatewayOps.obtainCargoByTrackingId(TrackingId.of(cargoTrackingId));

                txOps.doAfterCommit(() -> presenter.presentCargoDetails(cargo));
            });

        } catch (Exception e) {
            presenter.presentError(e);
        }

    }

    @Override
    public void selectItinerary(String cargoTrackingId) {
        try {

            txOps.doInTransaction(true, () -> {

                securityOps.assertThatUserIsAgent();

                TrackingId trackingId = TrackingId.of(cargoTrackingId);

                // load cargo
                Cargo cargo = gatewayOps.obtainCargoByTrackingId(trackingId);

                if (cargo.isRouted()) {
                    throw new RoutingError("Cargo <%s> is already routed.".formatted(trackingId.toString()));
                }

                // get route specification for cargo
                RouteSpecification routeSpecification = cargo.getRouteSpecification();

                // get candidate itineraries from external service
                List<Itinerary> itineraries = routingServiceOps.fetchRoutesForSpecification(trackingId, routeSpecification);

                txOps.doAfterCommit(() -> presenter.presentCandidateRoutes(cargo, itineraries));
            });

        } catch (Exception e) {
            presenter.presentError(e);
        }

    }

    @Override
    public void assignRoute(String trackingId, RouteDto selectedRoute) {

        try {

            txOps.doInTransaction(() -> {
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

                txOps.doAfterCommit(() -> presenter.presentResultOfAssigningRouteToCargo(trackingId));
            });

        } catch (Exception e) {
            presenter.presentError(e);
        }

    }
}
