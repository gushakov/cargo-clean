package com.github.cargoclean.infrastructure.config;

/*
    Notice on COPYRIGHT:
    -------------------

    Some code in this file is based on, copied from, and or modified from
    the code in the original DDDSample application. Please, see the copyright
    notice in "README.md" and the copy of the original licence in
    "original-license.txt", as well.
 */


import com.github.cargoclean.core.port.events.EventDispatcherOutputPort;
import com.github.cargoclean.core.port.persistence.PersistenceGatewayOutputPort;
import com.github.cargoclean.core.port.routing.RoutingServiceOutputPort;
import com.github.cargoclean.core.port.security.SecurityOutputPort;
import com.github.cargoclean.core.usecase.booking.BookingInputPort;
import com.github.cargoclean.core.usecase.booking.BookingPresenterOutputPort;
import com.github.cargoclean.core.usecase.booking.BookingUseCase;
import com.github.cargoclean.core.usecase.editlocation.EditLocationsInputPort;
import com.github.cargoclean.core.usecase.editlocation.EditLocationsPresenterOutputPort;
import com.github.cargoclean.core.usecase.editlocation.EditLocationsUseCase;
import com.github.cargoclean.core.usecase.handling.HandlingInputPort;
import com.github.cargoclean.core.usecase.handling.HandlingPresenterOutputPort;
import com.github.cargoclean.core.usecase.handling.HandlingUseCase;
import com.github.cargoclean.core.usecase.report.ReportInputPort;
import com.github.cargoclean.core.usecase.report.ReportPresenterOutputPort;
import com.github.cargoclean.core.usecase.report.ReportUseCase;
import com.github.cargoclean.core.usecase.routing.RoutingInputPort;
import com.github.cargoclean.core.usecase.routing.RoutingPresenterOutputPort;
import com.github.cargoclean.core.usecase.routing.RoutingUseCase;
import com.github.cargoclean.core.usecase.tracking.TrackingInputPort;
import com.github.cargoclean.core.usecase.tracking.TrackingPresenterOutputPort;
import com.github.cargoclean.core.usecase.tracking.TrackingUseCase;
import com.github.cargoclean.core.usecase.welcome.WelcomeInputPort;
import com.github.cargoclean.core.usecase.welcome.WelcomePresenterOutputPort;
import com.github.cargoclean.core.usecase.welcome.WelcomeUseCase;
import com.github.cargoclean.infrastructure.adapter.web.LocalDispatcherServlet;
import com.pathfinder.api.GraphTraversalService;
import com.pathfinder.internal.GraphDAOStub;
import com.pathfinder.internal.GraphTraversalServiceImpl;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.autoconfigure.web.servlet.DispatcherServletAutoConfiguration;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.web.servlet.DispatcherServlet;

/*
    Some configuration code copied from original "com.pathfinder.config.PathfinderApplicationContext".
 */
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
        Pathfinder service configuration.
     */

    @Bean
    public GraphTraversalService graphTraversalService() {
        return new GraphTraversalServiceImpl(new GraphDAOStub());
    }

}
