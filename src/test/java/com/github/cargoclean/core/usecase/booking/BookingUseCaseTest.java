package com.github.cargoclean.core.usecase.booking;

import com.github.cargoclean.core.model.Constants;
import com.github.cargoclean.core.model.MockModels;
import com.github.cargoclean.core.model.cargo.Cargo;
import com.github.cargoclean.core.model.cargo.InvalidDestinationSpecificationError;
import com.github.cargoclean.core.model.cargo.TrackingId;
import com.github.cargoclean.core.model.location.UnLocode;
import com.github.cargoclean.core.port.operation.PersistenceGatewayOutputPort;
import com.github.cargoclean.core.port.presenter.booking.BookingPresenterOutputPort;
import com.github.cargoclean.core.validator.BeanValidator;
import com.github.cargoclean.core.validator.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookingUseCaseTest {

    private final Validator validator = new BeanValidator();

    @Mock
    private BookingPresenterOutputPort presenter;

    @Mock
    private PersistenceGatewayOutputPort gatewayOps;

    private final TrackingId trackingId = TrackingId.of("ABCDEF01");

    private BookingInputPort useCase;

    @BeforeEach
    void setUp() {

        lenient().when(gatewayOps.nextTrackingId()).thenReturn(trackingId);

        lenient().when(gatewayOps.obtainLocationByUnLocode(any(UnLocode.class)))
                .thenAnswer(invocationOnMock -> {
                    UnLocode unLocode = invocationOnMock.getArgument(0);
                    return MockModels.location(unLocode.getCode());
                });

        useCase = new BookingUseCase(presenter, validator, gatewayOps);

    }

    @Test
    void should_create_and_save_new_cargo() {

        Date monthFromNow = Date.from(LocalDate.now(Constants.DEFAULT_ZONE_ID).plusMonths(1)
                .atStartOfDay(Constants.DEFAULT_ZONE_ID).toInstant());

        // booking a new cargo from Dallas to Melbourne, to arrive no later then a month from now
        useCase.bookCargo("USDAL", "AUMEL",
                monthFromNow);

        // make sure presenter was not called to present any errors
        verify(presenter, never()).presentError(any());

        ArgumentCaptor<Cargo> cargoArg = ArgumentCaptor.forClass(Cargo.class);
        verify(gatewayOps, times(1)).saveCargo(cargoArg.capture());

        Cargo cargo = cargoArg.getValue();

        // new cargo have null version
        assertThat(cargo.exists()).isFalse();

        // verify new cargo object
        assertThat(cargo.getTrackingId()).isEqualTo(trackingId);
        assertThat(cargo.getOrigin()).isEqualTo(UnLocode.of("USDAL"));
        assertThat(cargo.getRouteSpecification().getDestination())
                .isEqualTo(UnLocode.of("AUMEL"));
        assertThat(cargo.getRouteSpecification().getArrivalDeadline())
                .isEqualToIgnoringSeconds(ZonedDateTime.ofInstant(monthFromNow.toInstant(),
                        Constants.DEFAULT_ZONE_ID));

        // verify presenter was called to present the tracking ID of the new cargo

        ArgumentCaptor<TrackingId> trackingIdArg = ArgumentCaptor.forClass(TrackingId.class);
        verify(presenter, times(1)).presentResultOfNewCargoBooking(trackingIdArg.capture());

        assertThat(trackingIdArg.getValue()).isEqualTo(trackingId);
    }

    @Test
    void should_not_book_cargo_with_invalid_route_specification_same_origin_destination() {

        Date monthFromNow = Date.from(LocalDate.now(Constants.DEFAULT_ZONE_ID).plusMonths(1)
                .atStartOfDay(Constants.DEFAULT_ZONE_ID).toInstant());

        // booking a new cargo from Dallas to Melbourne, to arrive no later then a month from now
        useCase.bookCargo("USDAL", "USDAL",
                monthFromNow);

        ArgumentCaptor<Exception> errorArg = ArgumentCaptor
                .forClass(Exception.class);
        verify(presenter, times(1)).presentError(errorArg.capture());

        assertThat(errorArg.getValue()).isInstanceOf(InvalidDestinationSpecificationError.class);
    }
}
