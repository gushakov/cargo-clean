package com.github.cargoclean.core.usecase.routing;

import com.github.cargoclean.core.AlwaysOkSecurity;
import com.github.cargoclean.core.model.UtcDateTime;
import com.github.cargoclean.core.model.cargo.*;
import com.github.cargoclean.core.model.location.UnLocode;
import com.github.cargoclean.core.port.ErrorHandlingPresenterOutputPort;
import com.github.cargoclean.core.port.persistence.PersistenceGatewayOutputPort;
import com.github.cargoclean.core.port.security.SecurityOutputPort;
import com.github.cargoclean.core.usecase.AbstractUseCaseTestSupport;
import com.github.cargoclean.infrastructure.adapter.externalrouting.ExternalRoutingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static com.github.cargoclean.core.model.MockModels.cargo;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RoutingUseCaseTest extends AbstractUseCaseTestSupport {

    @Mock
    private RoutingPresenterOutputPort presenter;

    private final SecurityOutputPort securityOps = new AlwaysOkSecurity();

    @Mock
    private PersistenceGatewayOutputPort gatewayOps;

    @Mock
    private ExternalRoutingService externalRoutingService;

    @BeforeEach
    void setUp() {
        commonSetUp();
    }

    @Test
    void should_assign_new_route_to_cargo() {

        RoutingInputPort useCase = new RoutingUseCase(presenter, securityOps, gatewayOps, externalRoutingService,
                txOps);

        // get an example cargo
        String trackingId = "8E062F47";
        lenient().when(gatewayOps.obtainCargoByTrackingId(any(TrackingId.class)))
                .thenReturn(cargo(trackingId));

        // set up a direct route for the test, scheduled to arrive on time
        RouteDto routeDto = getValidRoute(trackingId);

        // try to assign the route
        useCase.assignRoute(trackingId, routeDto);

        // verify that there were no errors
        verify(presenter, never()).presentError(any());

        // verify that the gateway was called with updated cargo

        ArgumentCaptor<Cargo> cargoArg = ArgumentCaptor.forClass(Cargo.class);

        verify(gatewayOps, times(1)).saveCargo(cargoArg.capture());

        Cargo cargo = cargoArg.getValue();

        assertThat(cargo.getTrackingId()).isEqualTo(TrackingId.of(trackingId));

        assertThat(cargo.getItinerary().getLegs())
                .extracting(Leg::getCargoTrackingId,
                        Leg::getLoadLocation,
                        Leg::getUnloadLocation,
                        Leg::getLoadTime,
                        Leg::getUnloadTime)
                .containsExactly(tuple(TrackingId.of(trackingId),
                        UnLocode.of("USDAL"),
                        UnLocode.of("JNTKO"),
                        UtcDateTime.of("15-07-2022"),
                        UtcDateTime.of("09-08-2022")));

        assertThat(cargo.isRouted()).isTrue();
        assertThat(cargo.getDelivery().getEta()).isNotNull();

    }

    @Test
    void should_present_routing_error_if_selected_route_does_not_satisfy_specification() {
        RoutingInputPort useCase = new RoutingUseCase(presenter, securityOps, gatewayOps, externalRoutingService,
                txOps);

        // get an example cargo
        String trackingId = "8E062F47";
        lenient().when(gatewayOps.obtainCargoByTrackingId(any(TrackingId.class)))
                .thenReturn(cargo(trackingId));

        // route which does not satisfy the route specification for the test cargo
        RouteDto routeDto = getInvalidRoute(trackingId);

        // try to assign the route
        useCase.assignRoute(trackingId, routeDto);

        // should result in a routing error
        verify(presenter, times(1))
                .presentError(any(RoutingError.class));

    }

    @Test
    void should_present_routing_error_when_routing_cargo_with_existing_itinerary() {

        RoutingInputPort useCase = new RoutingUseCase(presenter, securityOps, gatewayOps, externalRoutingService,
                txOps);

        // create a spy of an existing cargo to simulate a cargo which has already been
        // routed
        String trackingId = "8E062F47";
        Cargo alreadyRoutedCargo = Mockito.spy(cargo(trackingId));
        lenient().when(alreadyRoutedCargo.isRouted()).thenReturn(true);

        lenient().when(gatewayOps.obtainCargoByTrackingId(any(TrackingId.class)))
                .thenReturn(alreadyRoutedCargo);

        RouteDto routeDto = getValidRoute(trackingId);

        // try to assign a new route
        useCase.assignRoute(trackingId, routeDto);

        // should result in a routing error
        verify(presenter, times(1))
                .presentError(any(RoutingError.class));
    }

    private RouteDto getValidRoute(String trackingId) {
        return RouteDto.builder()
                .legs(List.of(LegDto.builder()
                        .cargoTrackingId(trackingId)
                        .voyageNumber("0100S")
                        .from("USDAL")
                        .to("JNTKO")
                        .loadTime(UtcDateTime.of("15-07-2022"))
                        .unloadTime(UtcDateTime.of("09-08-2022"))
                        .build()))
                .build();
    }

    private RouteDto getInvalidRoute(String trackingId) {
        return RouteDto.builder()
                .legs(List.of(LegDto.builder()
                        .cargoTrackingId(trackingId)
                        .voyageNumber("0100S")
                        .from("USDAL")
                        .to("AUMEL")
                        .loadTime(UtcDateTime.of("15-07-2022"))
                        .unloadTime(UtcDateTime.of("09-08-2022"))
                        .build()))
                .build();
    }

    @Override
    protected ErrorHandlingPresenterOutputPort getPresenter() {
        return presenter;
    }
}
