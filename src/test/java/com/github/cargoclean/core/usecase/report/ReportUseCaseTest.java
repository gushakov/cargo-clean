package com.github.cargoclean.core.usecase.report;

import com.github.cargoclean.core.model.report.ExpectedArrivals;
import com.github.cargoclean.core.port.ErrorHandlingPresenterOutputPort;
import com.github.cargoclean.core.port.persistence.PersistenceGatewayOutputPort;
import com.github.cargoclean.core.usecase.AbstractUseCaseTestSupport;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/*
    Written with Continue.dev and Gemini 2.0 Flash.
 */

@ExtendWith(MockitoExtension.class)
class ReportUseCaseTest extends AbstractUseCaseTestSupport {

    @Mock
    private ReportPresenterOutputPort presenter;

    @Mock
    private PersistenceGatewayOutputPort gatewayOps;

    private ReportUseCase useCase;

    @Captor
    private ArgumentCaptor<List<ExpectedArrivals>> expectedArrivalsCaptor;

    @BeforeEach
    void setUp() {
        super.commonSetUp();
        useCase = new ReportUseCase(presenter, gatewayOps, txOps);
    }

    @Override
    protected ErrorHandlingPresenterOutputPort getPresenter() {
        return presenter;
    }

    @Test
    void reportExpectedArrivals_success() {
        // Arrange
        List<ExpectedArrivals> expectedArrivalsList = Collections.singletonList(
                new ExpectedArrivals("USNYC", 10) // Corrected: Using constructor
        );
        when(gatewayOps.queryForExpectedArrivals()).thenReturn(expectedArrivalsList);

        // Act
        useCase.reportExpectedArrivals();

        // Assert
        verify(gatewayOps, times(1)).queryForExpectedArrivals();
        verify(presenter, times(1)).presentExpectedArrivals(expectedArrivalsCaptor.capture());

        List<ExpectedArrivals> capturedArrivals = expectedArrivalsCaptor.getValue();
        assertEquals(1, capturedArrivals.size());
        assertEquals("USNYC", capturedArrivals.get(0).getCity()); // Corrected: Using getCity()
        assertEquals(10, capturedArrivals.get(0).getNumberOfArrivals()); // Corrected: Using getNumberOfArrivals()

        noErrorsWerePresented();
    }

    @Test
    void reportExpectedArrivals_noArrivals() {
        // Arrange
        when(gatewayOps.queryForExpectedArrivals()).thenReturn(Collections.emptyList());

        // Act
        useCase.reportExpectedArrivals();

        // Assert
        verify(gatewayOps, times(1)).queryForExpectedArrivals();
        verify(presenter, times(1)).presentExpectedArrivals(expectedArrivalsCaptor.capture());

        List<ExpectedArrivals> capturedArrivals = expectedArrivalsCaptor.getValue();
        assertEquals(0, capturedArrivals.size());

        noErrorsWerePresented();
    }

    @Test
    void reportExpectedArrivals_gatewayException() {
        // Arrange
        when(gatewayOps.queryForExpectedArrivals()).thenThrow(new RuntimeException("Database error"));

        // Act
        useCase.reportExpectedArrivals();

        // Assert
        verify(gatewayOps, times(1)).queryForExpectedArrivals();
        verify(presenter, times(1)).presentError(any(RuntimeException.class));

    }
}