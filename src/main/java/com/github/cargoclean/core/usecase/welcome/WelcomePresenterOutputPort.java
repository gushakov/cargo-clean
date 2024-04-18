package com.github.cargoclean.core.usecase.welcome;

import com.github.cargoclean.core.model.cargo.CargoInfo;
import com.github.cargoclean.core.port.ErrorHandlingPresenterOutputPort;

import java.util.List;

public interface WelcomePresenterOutputPort extends ErrorHandlingPresenterOutputPort {

    void presentHomePage(String username, List<CargoInfo> cargoes);

}
