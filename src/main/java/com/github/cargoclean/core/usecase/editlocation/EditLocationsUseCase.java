package com.github.cargoclean.core.usecase.editlocation;

import com.github.cargoclean.core.model.location.DuplicateLocationError;
import com.github.cargoclean.core.model.location.Location;
import com.github.cargoclean.core.model.location.Region;
import com.github.cargoclean.core.model.location.UnLocode;
import com.github.cargoclean.core.port.persistence.PersistenceGatewayOutputPort;
import com.github.cargoclean.core.port.security.SecurityOutputPort;
import com.github.cargoclean.core.port.transaction.TransactionOperationsOutputPort;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.List;

@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class EditLocationsUseCase implements EditLocationsInputPort {

    EditLocationsPresenterOutputPort presenter;

    SecurityOutputPort securityOps;

    PersistenceGatewayOutputPort gatewayOps;

    TransactionOperationsOutputPort txOps;

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

            txOps.doInTransaction(() -> {

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
                    // present error after rollback
                    txOps.doAfterRollback(() -> presenter.presentError(new DuplicateLocationError(location)));
                    return;
                }

                // save new Location
                Location savedLocation = gatewayOps.saveLocation(location);

                txOps.doAfterCommit(() -> presenter.presentResultOfSuccessfulRegistrationOfNewLocation(savedLocation));
            });

        } catch (Exception e) {
            presenter.presentError(e);
        }

    }

    @Override
    public void prepareUpdateLocationView() {
        try {

            // only manager can add locations
            securityOps.assertThatUserIsManager();

            // load all locations
            List<Location> locations = gatewayOps.allLocations();

            presenter.presentUpdateLocationForm(locations);

        }
        catch (Exception e) {
            presenter.presentError(e);
        }
    }
}
