package com.github.cargoclean.core.usecase.editlocation;

import com.github.cargoclean.core.model.location.DuplicateLocationError;
import com.github.cargoclean.core.model.location.Location;
import com.github.cargoclean.core.model.location.Region;
import com.github.cargoclean.core.model.location.UnLocode;
import com.github.cargoclean.core.port.operation.persistence.PersistenceGatewayOutputPort;
import com.github.cargoclean.core.port.operation.security.SecurityOutputPort;
import com.github.cargoclean.core.port.presenter.editlocations.EditLocationsPresenterOutputPort;
import lombok.RequiredArgsConstructor;

import javax.transaction.Transactional;

@RequiredArgsConstructor
public class EditLocationsUseCase implements EditLocationsInputPort {

    private final EditLocationsPresenterOutputPort presenter;

    private final SecurityOutputPort securityOps;

    private final PersistenceGatewayOutputPort gatewayOps;

    @Override
    public void prepareAddNewLocationView() {

        try {
            // only manager can add locations
            securityOps.assertThatUserIsManager();
        } catch (Exception e) {
            presenter.presentError(e);
            return;
        }

        presenter.presentAddNewLocationForm();

    }

    @Override
    @Transactional
    public void registerNewLocation(String unLocodeText, String locationName, String regionName) {
        Location savedLocation;
        try {
            // only manager can add locations
            securityOps.assertThatUserIsManager();

            // make new Location from arguments
            final UnLocode unLocode = UnLocode.of(unLocodeText);
            final Region region = Region.of(regionName);
            final Location location = Location.builder()
                    .unlocode(unLocode)
                    .name(locationName)
                    .region(region)
                    .build();

            // check if Location exists already
            if (gatewayOps.locationExists(location)) {
                presenter.presentError(new DuplicateLocationError(location));
                return;
            }

            // save new Location
            savedLocation = gatewayOps.saveLocation(location);

        } catch (Exception e) {
            gatewayOps.rollback();
            presenter.presentError(e);
            return;
        }

        presenter.presentResultOfSuccessfulRegistrationOfNewLocation(savedLocation);
    }
}
