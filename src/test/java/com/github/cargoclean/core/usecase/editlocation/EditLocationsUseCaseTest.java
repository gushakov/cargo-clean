package com.github.cargoclean.core.usecase.editlocation;

import com.github.cargoclean.core.model.location.DuplicateLocationError;
import com.github.cargoclean.core.model.location.Location;
import com.github.cargoclean.core.model.location.Region;
import com.github.cargoclean.core.model.location.UnLocode;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/*
    Written with Continue.dev and Gemini 2.0 Flash.
 */

@ExtendWith(MockitoExtension.class)
class EditLocationsUseCaseTest extends AbstractUseCaseTestSupport {

    @Mock
    private EditLocationsPresenterOutputPort presenter;

    @Mock
    private PersistenceGatewayOutputPort gatewayOps;

    private EditLocationsUseCase useCase;

    @Captor
    private ArgumentCaptor<Location> locationCaptor;

    @BeforeEach
    void setUp() {
        super.commonSetUp(); // call common setup from the abstract class
        useCase = new EditLocationsUseCase(presenter, securityOps, gatewayOps, txOps);
    }

    @Override
    protected ErrorHandlingPresenterOutputPort getPresenter() {
        return presenter;
    }

    @Test
    void registerNewLocation_success() {
        // Arrange
        String unLocodeStr = "USNYC";
        String locationName = "New York";
        String regionName = "North America"; // Using region from V1.8__Add_region.sql
        Location savedLocation = Location.builder()
                .unlocode(UnLocode.of(unLocodeStr))
                .name(locationName)
                .region(Region.of(regionName))
                .build();

        when(gatewayOps.locationExists(any(Location.class))).thenReturn(false);
        when(gatewayOps.obtainLocationByUnLocode(UnLocode.of(unLocodeStr))).thenReturn(savedLocation);

        // Act
        useCase.registerNewLocation(unLocodeStr, locationName, regionName);

        // Assert
        verify(securityOps, times(1)).assertThatUserIsManager();
        verify(gatewayOps, times(1)).saveLocation(locationCaptor.capture());
        Location capturedLocation = locationCaptor.getValue();
        assertEquals(unLocodeStr, capturedLocation.getUnlocode().getCode()); // Corrected line
        assertEquals(locationName, capturedLocation.getName());
        assertEquals(regionName, capturedLocation.getRegion().toString()); // Using name() for enum comparison
        verify(presenter, times(1)).presentResultOfSuccessfulRegistrationOfNewLocation(savedLocation);
        noErrorsWerePresented();
    }

    @Test
    void registerNewLocation_duplicateLocation() {
        // Arrange
        String unLocodeStr = "USNYC";
        String locationName = "New York";
        String regionName = "North America"; // Using region from V1.8__Add_region.sql
        Location location = Location.builder()
                .unlocode(UnLocode.of(unLocodeStr))
                .name(locationName)
                .region(Region.of(regionName))
                .build();

        when(gatewayOps.locationExists(any(Location.class))).thenReturn(true);

        // Act
        useCase.registerNewLocation(unLocodeStr, locationName, regionName);

        // Assert
        verify(securityOps, times(1)).assertThatUserIsManager();
        verify(gatewayOps, times(0)).saveLocation(any());
        verify(presenter, times(1)).presentError(any(DuplicateLocationError.class));
    }

    @Test
    void registerNewLocation_securityException() {
        // Arrange
        String unLocodeStr = "USNYC";
        String locationName = "New York";
        String regionName = "North America"; // Using region from V1.8__Add_region.sql
        doThrow(new SecurityException("Unauthorized")).when(securityOps).assertThatUserIsManager();

        // Act
        useCase.registerNewLocation(unLocodeStr, locationName, regionName);

        // Assert
        verify(securityOps, times(1)).assertThatUserIsManager();
        verify(gatewayOps, times(0)).saveLocation(any());
        verify(presenter, times(1)).presentError(any(SecurityException.class));
    }
}