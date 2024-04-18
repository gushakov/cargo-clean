package com.github.cargoclean.core.usecase.tracking;

import com.github.cargoclean.core.model.cargo.Cargo;
import com.github.cargoclean.core.model.handling.HandlingHistory;
import com.github.cargoclean.core.model.location.Location;
import com.github.cargoclean.core.model.location.UnLocode;
import com.github.cargoclean.core.port.ErrorHandlingPresenterOutputPort;

import java.util.Map;

public interface TrackingPresenterOutputPort extends ErrorHandlingPresenterOutputPort {
    void presentInitialViewForCargoTracking();

    void presentCargoTrackingInformation(Cargo cargo, HandlingHistory handlingHistory,
                                         Map<UnLocode, Location> allLocationsMap);
}
