package com.github.cargoclean.core.model;

import com.github.cargoclean.core.model.cargo.*;
import com.github.cargoclean.core.model.location.Location;
import com.github.cargoclean.core.model.location.UnLocode;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Optional;

/**
 * These models are examples of a fully specified graphs of models with
 * realistic data. They will be used in unit tests.
 */
public class MockModels {

    private static Map<String, Location> allLocations() {
        return Map.of(
                "JNTKO",
                Location.builder()
                        .name("Tokyo")
                        .unLocode(UnLocode.builder()
                                .code("JNTKO")
                                .build())
                        .build(),

                "NLRTM",
                Location.builder()
                        .name("Rotterdam")
                        .unLocode(UnLocode.builder()
                                .code("NLRTM")
                                .build())
                        .build(),

                "USDAL",
                Location.builder()
                        .name("Dallas")
                        .unLocode(UnLocode.builder()
                                .code("USDAL")
                                .build())
                        .build(),

                "SEGOT",
                Location.builder()
                        .name("Göteborg")
                        .unLocode(UnLocode.builder()
                                .code("SEGOT")
                                .build())
                        .build(),

                "FIHEL",
                Location.builder()
                        .name("Helsinki")
                        .unLocode(UnLocode.builder()
                                .code("FIHEL")
                                .build())
                        .build(),

                "AUMEL",
                Location.builder()
                        .name("Melbourne")
                        .unLocode(UnLocode.builder()
                                .code("AUMEL")
                                .build())
                        .build()

        );
    }

    public static Location location(String unlocode) {
        return Optional.ofNullable(allLocations().get(unlocode))
                .orElseThrow();
    }

    private static Map<String, Cargo> allCargos() {
        return Map.of(

                "75FC0BD4",
                Cargo.builder()
                        .trackingId(TrackingId.builder()
                                .id("75FC0BD4")
                                .build())
                        .origin(UnLocode.of("USDAL"))
                        .delivery(Delivery.builder()
                                .transportStatus(TransportStatus.IN_PORT)
                                .build())
                        .routeSpecification(RouteSpecification.builder()
                                .origin(UnLocode.of("USDAL"))
                                .destination(UnLocode.of("AUMEL"))
                                .arrivalDeadline(localDate("24-08-2022"))
                                .build())
                        .build(),

                "695CF30D",
                Cargo.builder()
                        .trackingId(TrackingId.builder()
                                .id("695CF30D")
                                .build())
                        .origin(UnLocode.of("FIHEL"))
                        .delivery(Delivery.builder()
                                .transportStatus(TransportStatus.ONBOARD_CARRIER)
                                .build())
                        .routeSpecification(RouteSpecification.builder()
                                .origin(UnLocode.of("FIHEL"))
                                .destination(UnLocode.of("NLRTM"))
                                .arrivalDeadline(localDate("16-08-2022"))
                                .build())
                        .build(),

                "8E062F47",
                Cargo.builder()
                        .trackingId(TrackingId.builder()
                                .id("8E062F47")
                                .build())
                        .origin(UnLocode.of("USDAL"))
                        .delivery(Delivery.builder()
                                .transportStatus(TransportStatus.ONBOARD_CARRIER)
                                .build())
                        .routeSpecification(RouteSpecification.builder()
                                .origin(UnLocode.of("USDAL"))
                                .destination(UnLocode.of("JNTKO"))
                                .arrivalDeadline(localDate("10-08-2022"))
                                .build())
                        .build()
        );
    }

    /**
     * Converts string in format "dd-MM-yyyy" to an instance of {@link ZonedDateTime}
     *
     * @param date local date as sting
     * @return date and time at the start of the day with the default timezone
     */
    public static ZonedDateTime localDate(String date) {
        return ZonedDateTime.of(LocalDate.parse(date, DateTimeFormatter.ofPattern("dd-MM-yyyy")).atStartOfDay(),
                Constants.DEFAULT_ZONE_ID);
    }

    public static Instant localInstant(String date) {
        return localDate(date).toInstant();
    }

    public static Cargo cargo(String trackingId) {
        return Optional.ofNullable(allCargos().get(trackingId))
                .orElseThrow();
    }


}
