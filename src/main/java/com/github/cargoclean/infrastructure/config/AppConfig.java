package com.github.cargoclean.infrastructure.config;

import com.github.cargoclean.core.port.operation.PersistenceGatewayOutputPort;
import com.github.cargoclean.core.port.presenter.booking.BookingPresenterOutputPort;
import com.github.cargoclean.core.usecase.booking.BookingInputPort;
import com.github.cargoclean.core.usecase.booking.BookingUseCase;
import com.github.cargoclean.infrastructure.adapter.web.presenter.LocalDispatcherServlet;
import org.springframework.boot.autoconfigure.web.servlet.DispatcherServletAutoConfiguration;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.web.servlet.DispatcherServlet;

@Configuration
public class AppConfig {

    /*
        We need to register our custom override of DispatcherServlet
        instead of original dispatcher.
        Based on this answer: https://stackoverflow.com/a/68536242
    */

    @Bean
    public ServletRegistrationBean localDispatcherRegistration() {
        return new ServletRegistrationBean<>(dispatcherServlet());
    }

    @Bean(name = DispatcherServletAutoConfiguration.DEFAULT_DISPATCHER_SERVLET_BEAN_NAME)
    public DispatcherServlet dispatcherServlet() {
        return new LocalDispatcherServlet();
    }

    /*
        Here we specify all the use cases for our application. Notice the prototype scope.
        This way each request processing thread will get its own instance of each use case
        when it is looked up from the application context (in the controller).
     */

    @Bean
    @Scope("prototype")
    public BookingInputPort newCargoBookingUseCase(BookingPresenterOutputPort presenter,
                                                   PersistenceGatewayOutputPort gatewayOps) {
        return new BookingUseCase(presenter, gatewayOps);
    }

}