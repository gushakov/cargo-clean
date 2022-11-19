package com.github.cargoclean.infrastructure.adapter.db;

import com.github.cargoclean.CargoCleanApplication;
import com.github.cargoclean.core.model.UtcDateTime;
import com.github.cargoclean.core.model.cargo.Cargo;
import com.github.cargoclean.core.model.cargo.TrackingId;
import com.github.cargoclean.core.model.cargo.TransportStatus;
import com.github.cargoclean.core.model.handling.EventId;
import com.github.cargoclean.core.model.handling.HandlingEvent;
import com.github.cargoclean.core.model.handling.HandlingEventType;
import com.github.cargoclean.core.model.handling.HandlingHistory;
import com.github.cargoclean.core.model.location.Location;
import com.github.cargoclean.core.model.location.Region;
import com.github.cargoclean.core.model.location.UnLocode;
import com.github.cargoclean.core.model.report.ExpectedArrivals;
import com.github.cargoclean.core.model.voyage.VoyageNumber;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.List;
import java.util.Map;

import static com.github.cargoclean.core.model.MockModels.cargo;
import static com.github.cargoclean.core.model.MockModels.itinerary;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;
import static org.junit.jupiter.api.Assertions.assertThrows;

/*
    References:
    ----------

    1.  Summing integers with streams: https://www.baeldung.com/java-stream-sum
 */

/*
    Notes:
    -----

    1.  These are integrations tests intended to be run from the IDE to
        verify the workings of the persistence gateway with the running
        Postgres database (using local Docker instance).
        IMPORTANT: some of these tests will attempt to modify the database.
 */

@SpringBootTest(classes = {CargoCleanApplication.class}, webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class DbPersistenceGatewayTestIT {

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    private DbPersistenceGateway dbGateway;

    @Test
    void should_process_flyway_initialization_scripts() {
        final Integer locationsCount = jdbcTemplate.queryForObject("select count(*) from public.location",
                Map.of(), Integer.class);
        assertThat(locationsCount).isGreaterThan(10);
    }

    @Test
    void should_load_all_locations() {
        final List<Location> locations = dbGateway.allLocations();
        assertThat(locations)
                .extracting(Location::getUnlocode,
                        Location::getName,
                        Location::getRegion)
                .contains(tuple(UnLocode.of("USNYC"), "New York", Region.NorthAmerica));

        // locations should not be modifiable

        assertThrows(UnsupportedOperationException.class, locations::clear);
    }

    @ParameterizedTest
    @ValueSource(strings = {"75FC0BD4"})
    void should_save_cargo_successfully(String unlocode) {

        dbGateway.deleteCargo(TrackingId.of(unlocode));

        final Cargo cargoToSave = cargo(unlocode);
        final Cargo savedCargo = dbGateway.saveCargo(cargoToSave);
        assertThat(savedCargo.exists()).isTrue();
        assertThat(savedCargo)
                .extracting(Cargo::getTrackingId,
                        Cargo::getOrigin)
                .containsExactly(cargoToSave.getTrackingId(), cargoToSave.getOrigin());
    }

    @Test
    void should_query_for_number_of_arrivals_by_destination_city() {
        List<ExpectedArrivals> expectedArrivals = dbGateway.queryForExpectedArrivals();
        assertThat(expectedArrivals.stream()
                .map(ExpectedArrivals::getNumberOfArrivals)
                .mapToInt(Integer::intValue)
                .sum()).isGreaterThan(0);
    }

    @Test
    void should_save_cargo_load_route_update_delivery_progress_save_load_again() {
        TrackingId trackingId = TrackingId.of("8E062F47");
        dbGateway.deleteCargo(trackingId);
        final Cargo cargo = cargo(trackingId.getId());
        final Cargo savedCargo = dbGateway.saveCargo(cargo);

        // update itinerary
        Cargo routedCargo = savedCargo.assignItinerary(itinerary(1, 2));

        // update delivery
        HandlingHistory handlingHistory = HandlingHistory.builder()
                .handlingEvents(List.of(HandlingEvent.builder()
                        .type(HandlingEventType.UNLOAD)
                        .cargoId(trackingId)
                        .voyageNumber(VoyageNumber.of("0200S"))
                        .location(UnLocode.of("JNTKO"))
                        .eventId(EventId.of(1L))
                        .completionTime(UtcDateTime.of("05-08-2022"))
                        .registrationTime(UtcDateTime.of("05-08-2022"))
                        .build()))
                .build();

        final Cargo updatedCargo = routedCargo.updateDeliveryProgress(handlingHistory);
        Cargo savedAgainCargo = dbGateway.saveCargo(updatedCargo);
        Cargo loadedAgainCargo = dbGateway.obtainCargoByTrackingId(savedAgainCargo.getTrackingId());

        assertThat(loadedAgainCargo.getDelivery().getTransportStatus())
                .isEqualTo(TransportStatus.IN_PORT);
        assertThat(loadedAgainCargo.getDelivery().isMisdirected())
                .isFalse();
        assertThat(loadedAgainCargo.getDelivery().getLastKnownLocation())
                .isEqualTo(UnLocode.of("JNTKO"));
    }

}
