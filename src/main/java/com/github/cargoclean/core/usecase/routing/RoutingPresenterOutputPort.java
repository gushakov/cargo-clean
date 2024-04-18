package com.github.cargoclean.core.usecase.routing;

import com.github.cargoclean.core.model.cargo.Cargo;
import com.github.cargoclean.core.model.cargo.Itinerary;
import com.github.cargoclean.core.port.ErrorHandlingPresenterOutputPort;

import java.util.List;

public interface RoutingPresenterOutputPort extends ErrorHandlingPresenterOutputPort {
    void presentCargoDetails(Cargo cargo);

    void presentCandidateRoutes(Cargo cargo, List<Itinerary> itineraries);

    void presentResultOfAssigningRouteToCargo(String trackingId);
}
