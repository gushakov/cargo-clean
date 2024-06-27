package com.github.cargoclean.core.usecase.editlocation;

public interface EditLocationsInputPort {

    void prepareAddNewLocationView();

    void registerNewLocation(String unLocodeText, String locationName, String regionName);

    void prepareUpdateLocationView();
}
