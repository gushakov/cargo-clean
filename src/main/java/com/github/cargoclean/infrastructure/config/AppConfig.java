package com.github.cargoclean.infrastructure.config;

/*
    Notice on COPYRIGHT:
    -------------------

    Some code in this file is based on, copied from, and or modified from
    the code in the original DDDSample application. Please, see the copyright
    notice in "README.md" and the copy of the original licence in
    "original-license.txt", as well.
 */


import com.github.cargoclean.core.port.operation.PersistenceGatewayOutputPort;
import com.github.cargoclean.core.port.operation.RoutingServiceOutputPort;
import com.github.cargoclean.core.port.presenter.booking.BookingPresenterOutputPort;
import com.github.cargoclean.core.port.presenter.report.ReportPresenterOutputPort;
import com.github.cargoclean.core.port.presenter.routing.RoutingPresenterOutputPort;
import com.github.cargoclean.core.port.presenter.tracking.TrackingPresenterOutputPort;
import com.github.cargoclean.core.usecase.booking.BookingInputPort;
import com.github.cargoclean.core.usecase.booking.BookingUseCase;
import com.github.cargoclean.core.usecase.report.ReportInputPort;
import com.github.cargoclean.core.usecase.report.ReportUseCase;
import com.github.cargoclean.core.usecase.routing.RoutingInputPort;
import com.github.cargoclean.core.usecase.routing.RoutingUseCase;
import com.github.cargoclean.core.usecase.tracking.TrackingInputPort;
import com.github.cargoclean.core.usecase.tracking.TrackingUseCase;
import com.github.cargoclean.core.validator.BeanValidator;
import com.github.cargoclean.core.validator.Validator;
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
        Create validator service to be used in use cases.
     */

    @Bean
    public Validator validator() {
        return new BeanValidator();
    }

    /*
        Pathfinder service configuration.
     */

    @Bean
    public GraphTraversalService graphTraversalService(){
        return new GraphTraversalServiceImpl(new GraphDAOStub());
    }

    /*
        Here we specify all the use cases for our application. Notice the prototype scope.
        This way each request processing thread will get its own instance of each use case
        when it is looked up from the application context (in the controller).
     */

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public BookingInputPort newCargoBookingUseCase(BookingPresenterOutputPort presenter,
                                                   Validator validator,
                                                   PersistenceGatewayOutputPort gatewayOps) {
        return new BookingUseCase(presenter, validator, gatewayOps);
    }

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public RoutingInputPort routingUseCase(RoutingPresenterOutputPort presenter,
                                           Validator validator,
                                           PersistenceGatewayOutputPort gatewayOps,
                                           RoutingServiceOutputPort routingServiceOps) {
        return new RoutingUseCase(presenter, validator, gatewayOps, routingServiceOps);
    }

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public ReportInputPort reportUseCase(ReportPresenterOutputPort presenter,
                                         PersistenceGatewayOutputPort gatewayOps){
        return new ReportUseCase(presenter, gatewayOps);
    }

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public TrackingInputPort trackingUseCase(TrackingPresenterOutputPort presenter){
        return new TrackingUseCase(presenter);
    }
}
