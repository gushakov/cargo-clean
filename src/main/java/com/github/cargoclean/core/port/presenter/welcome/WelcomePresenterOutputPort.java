package com.github.cargoclean.core.port.presenter.welcome;

import com.github.cargoclean.core.port.error.ErrorHandlingPresenterOutputPort;

public interface WelcomePresenterOutputPort extends ErrorHandlingPresenterOutputPort {

    void presentHomePage(String username);

}
