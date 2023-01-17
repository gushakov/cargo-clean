package com.github.cargoclean.infrastructure.config;

/*
    Notice on COPYRIGHT:
    -------------------

    Some code in this file is based on, copied from, and or modified from
    the code in the original DDDSample application. Please, see the copyright
    notice in "README.md" and the copy of the original licence in
    "original-license.txt", as well.
 */


import com.github.cargoclean.core.port.operation.events.EventDispatcherOutputPort;
import com.github.cargoclean.core.port.operation.persistence.PersistenceGatewayOutputPort;
import com.github.cargoclean.core.port.operation.routing.RoutingServiceOutputPort;
import com.github.cargoclean.core.port.operation.security.SecurityOutputPort;
import com.github.cargoclean.core.port.presenter.booking.BookingPresenterOutputPort;
import com.github.cargoclean.core.port.presenter.editlocations.EditLocationsPresenterOutputPort;
import com.github.cargoclean.core.port.presenter.handling.HandlingPresenterOutputPort;
import com.github.cargoclean.core.port.presenter.report.ReportPresenterOutputPort;
import com.github.cargoclean.core.port.presenter.routing.RoutingPresenterOutputPort;
import com.github.cargoclean.core.port.presenter.tracking.TrackingPresenterOutputPort;
import com.github.cargoclean.core.port.presenter.welcome.WelcomePresenterOutputPort;
import com.github.cargoclean.core.usecase.booking.BookingInputPort;
import com.github.cargoclean.core.usecase.booking.BookingUseCase;
import com.github.cargoclean.core.usecase.editlocation.EditLocationsInputPort;
import com.github.cargoclean.core.usecase.editlocation.EditLocationsUseCase;
import com.github.cargoclean.core.usecase.handling.HandlingInputPort;
import com.github.cargoclean.core.usecase.handling.HandlingUseCase;
import com.github.cargoclean.core.usecase.report.ReportInputPort;
import com.github.cargoclean.core.usecase.report.ReportUseCase;
import com.github.cargoclean.core.usecase.routing.RoutingInputPort;
import com.github.cargoclean.core.usecase.routing.RoutingUseCase;
import com.github.cargoclean.core.usecase.tracking.TrackingInputPort;
import com.github.cargoclean.core.usecase.tracking.TrackingUseCase;
import com.github.cargoclean.core.usecase.welcome.WelcomeInputPort;
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

    /*
        Here we specify all the use cases for our application. Notice the prototype scope.
        This way each request processing thread will get its own instance of each use case
        when it is looked up from the application context (in the controller).
     */
    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public WelcomeInputPort welcomeUseCase(WelcomePresenterOutputPort presenter,
                                           SecurityOutputPort securityOps,
                                           PersistenceGatewayOutputPort gatewayOps) {
        return new WelcomeUseCase(presenter, securityOps, gatewayOps);
    }

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public BookingInputPort newCargoBookingUseCase(BookingPresenterOutputPort presenter,
                                                   SecurityOutputPort securityOps,
                                                   PersistenceGatewayOutputPort gatewayOps) {
        return new BookingUseCase(presenter, securityOps, gatewayOps);
    }

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public RoutingInputPort routingUseCase(RoutingPresenterOutputPort presenter,
                                           SecurityOutputPort securityOps,
                                           PersistenceGatewayOutputPort gatewayOps,
                                           RoutingServiceOutputPort routingServiceOps) {
        return new RoutingUseCase(presenter, securityOps, gatewayOps, routingServiceOps);
    }

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public ReportInputPort reportUseCase(ReportPresenterOutputPort presenter,
                                         PersistenceGatewayOutputPort gatewayOps) {
        return new ReportUseCase(presenter, gatewayOps);
    }

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public TrackingInputPort trackingUseCase(TrackingPresenterOutputPort presenter,
                                             SecurityOutputPort securityOps,
                                             PersistenceGatewayOutputPort gatewayOps) {
        return new TrackingUseCase(presenter, securityOps, gatewayOps);
    }

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public HandlingInputPort handlingUseCase(HandlingPresenterOutputPort presenter,
                                             SecurityOutputPort securityOps,
                                             PersistenceGatewayOutputPort gatewayOps,
                                             EventDispatcherOutputPort eventsOps) {
        return new HandlingUseCase(presenter, securityOps, gatewayOps, eventsOps);
    }


    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public EditLocationsInputPort editLocationsUseCase(EditLocationsPresenterOutputPort presenter,
                                                       SecurityOutputPort securityOps,
                                                       PersistenceGatewayOutputPort gatewayOps) {
        return new EditLocationsUseCase(presenter, securityOps, gatewayOps);
    }
}
