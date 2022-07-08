package com.github.cargoclean.core.port.presenter.routing;

import com.github.cargoclean.core.model.cargo.Cargo;
import com.github.cargoclean.core.model.cargo.Itinerary;
import com.github.cargoclean.core.port.error.ErrorHandlingPresenterOutputPort;

import java.util.List;

public interface RoutingPresenterOutputPort extends ErrorHandlingPresenterOutputPort {
    void presentCargoForRouting(Cargo cargo);

    void presentCandidateRoutes(Cargo cargo, List<Itinerary> itineraries);

    void presentResultOfAssigningRouteToCargo(String trackingId);
}
