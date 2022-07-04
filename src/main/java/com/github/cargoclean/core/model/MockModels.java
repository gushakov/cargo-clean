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
                        .id(1)
                        .name("Tokyo")
                        .unLocode(UnLocode.builder()
                                .code("JNTKO")
                                .build())
                        .build(),

                "NLRTM",
                Location.builder()
                        .id(2)
                        .name("Rotterdam")
                        .unLocode(UnLocode.builder()
                                .code("NLRTM")
                                .build())
                        .build(),

                "USDAL",
                Location.builder()
                        .id(3)
                        .name("Dallas")
                        .unLocode(UnLocode.builder()
                                .code("USDAL")
                                .build())
                        .build(),

                "FIHEL",
                Location.builder()
                        .id(10)
                        .name("Helsinki")
                        .unLocode(UnLocode.builder()
                                .code("FIHEL")
                                .build())
                        .build(),

                "AUMEL",
                Location.builder()
                        .id(13)
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

                // ---------------------------------------------------------------

                "75FC0BD4",
                Cargo.builder()
                        .id(1)
                        .trackingId(TrackingId.builder()
                                .id("75FC0BD4")
                                .build())
                        .origin(location("USDAL"))
                        .delivery(Delivery.builder()
                                .transportStatus(TransportStatus.IN_PORT)
                                .build())
                        .routeSpecification(RouteSpecification.builder()
                                .origin(location("USDAL"))
                                .destination(location("AUMEL"))
                                .arrivalDeadline(localDate("24-08-2022"))
                                .build())
                        .build(),

                // ---------------------------------------------------------------

                "695CF30D",
                Cargo.builder()
                        .id(2)
                        .trackingId(TrackingId.builder()
                                .id("695CF30D")
                                .build())
                        .origin(location("FIHEL"))
                        .delivery(Delivery.builder()
                                .transportStatus(TransportStatus.ONBOARD_CARRIER)
                                .build())
                        .routeSpecification(RouteSpecification.builder()
                                .origin(location("FIHEL"))
                                .destination(location("NLRTM"))
                                .arrivalDeadline(localDate("16-08-2022"))
                                .build())
                        .build()
        );
    }

    public static ZonedDateTime localDate(String date) {
        return ZonedDateTime.of(LocalDate.parse(date, DateTimeFormatter.ofPattern("dd-MM-yyyy")).atStartOfDay(),
                Constants.DEFAULT_ZONE_ID);
    }

    public static Instant localInstant(String date){
        return localDate(date).toInstant();
    }

    public static Cargo cargo(String trackingId) {
        return Optional.ofNullable(allCargos().get(trackingId))
                .orElseThrow();
    }

}