package com.github.cargoclean.core.usecase.editlocation;

public interface EditLocationsInputPort {

    void prepareAddNewLocationView();

    void registerNewLocation(String unLocodeStr, String locationName, String regionName);

    void prepareUpdateLocationView();

    void selectLocationForUpdate(String unLocodeStr);

    void registerUpdatedLocation(String unLocodeStr, String city);
}
