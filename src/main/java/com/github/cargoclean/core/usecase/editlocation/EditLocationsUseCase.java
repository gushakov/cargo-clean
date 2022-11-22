package com.github.cargoclean.core.usecase.editlocation;

import com.github.cargoclean.core.port.operation.security.SecurityOutputPort;
import com.github.cargoclean.core.port.presenter.editlocations.EditLocationsPresenterOutputPort;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class EditLocationsUseCase implements EditLocationsInputPort {

    private final EditLocationsPresenterOutputPort presenter;

    private final SecurityOutputPort securityOps;

    @Override
    public void prepareAddNewLocationView() {

    }
}
