package com.github.cargoclean.core.usecase.editlocation;

import com.github.cargoclean.core.model.location.DuplicateLocationError;
import com.github.cargoclean.core.model.location.Location;
import com.github.cargoclean.core.model.location.Region;
import com.github.cargoclean.core.model.location.UnLocode;
import com.github.cargoclean.core.port.persistence.PersistenceGatewayOutputPort;
import com.github.cargoclean.core.port.security.SecurityOutputPort;
import lombok.RequiredArgsConstructor;

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

            presenter.presentAddNewLocationForm();

        } catch (Exception e) {
            presenter.presentError(e);
        }

    }

    @Override
    public void registerNewLocation(String unLocodeText, String locationName, String regionName) {
        try {
            Location savedLocation;
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

            presenter.presentResultOfSuccessfulRegistrationOfNewLocation(savedLocation);
        } catch (Exception e) {
            presenter.presentError(e);
        }

    }
}
