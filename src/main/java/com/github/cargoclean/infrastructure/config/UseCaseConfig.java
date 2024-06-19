package com.github.cargoclean.infrastructure.config;

import com.github.cargoclean.core.port.events.EventDispatcherOutputPort;
import com.github.cargoclean.core.port.persistence.PersistenceGatewayOutputPort;
import com.github.cargoclean.core.port.routing.RoutingServiceOutputPort;
import com.github.cargoclean.core.port.security.SecurityOutputPort;
import com.github.cargoclean.core.port.transaction.TransactionOperationsOutputPort;
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
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

/**
 * Here we specify all the use cases for our application. Notice the prototype scope.
 * This way each request processing thread will get its own instance of each use case
 * when it is looked up from the application context (in the controller).
 */
@Configuration
public class UseCaseConfig {

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public WelcomeInputPort welcomeUseCase(WelcomePresenterOutputPort presenter,
                                           SecurityOutputPort securityOps,
                                           PersistenceGatewayOutputPort gatewayOps) {
        return new WelcomeUseCase(presenter, securityOps, gatewayOps);
    }

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public BookingInputPort bookingUseCase(BookingPresenterOutputPort presenter,
                                           SecurityOutputPort securityOps,
                                           PersistenceGatewayOutputPort gatewayOps,
                                           TransactionOperationsOutputPort txOps) {
        return new BookingUseCase(presenter, securityOps, gatewayOps, txOps);
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
