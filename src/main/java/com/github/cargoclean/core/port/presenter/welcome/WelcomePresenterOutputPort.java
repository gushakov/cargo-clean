package com.github.cargoclean.core.port.presenter.welcome;

import com.github.cargoclean.core.model.cargo.CargoInfo;
import com.github.cargoclean.core.port.error.ErrorHandlingPresenterOutputPort;

import java.util.List;

public interface WelcomePresenterOutputPort extends ErrorHandlingPresenterOutputPort {

    void presentHomePage(String username, List<CargoInfo> cargoes);

}
