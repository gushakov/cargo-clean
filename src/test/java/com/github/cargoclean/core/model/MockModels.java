package com.github.cargoclean.core.model;

import com.github.cargoclean.core.model.cargo.*;
import com.github.cargoclean.core.model.location.Location;
import com.github.cargoclean.core.model.location.Region;
import com.github.cargoclean.core.model.location.UnLocode;
import com.github.cargoclean.core.model.voyage.VoyageNumber;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;

/**
 * These models are examples of a fully specified graphs of models with
 * realistic data. They will be used in unit tests.
 */
public class MockModels {

    public static RouteSpecification routeSpecification() {
        return RouteSpecification.builder()
                .origin(UnLocode.of("USDAL"))
                .destination(UnLocode.of("AUMEL"))
                .arrivalDeadline(UtcDateTime.of("24-08-2022"))
                .build();
    }

    public static Itinerary itinerary() {
        return itinerary(1, 2);
    }

    public static Map<String, Location> allLocations() {
        return Map.of(
                "JNTKO",
                Location.builder()
                        .name("Tokyo")
                        .unlocode(UnLocode.builder()
                                .code("JNTKO")
                                .build())
                        .region(Region.ASIA)
                        .build(),

                "NLRTM",
                Location.builder()
                        .name("Rotterdam")
                        .unlocode(UnLocode.builder()
                                .code("NLRTM")
                                .build())
                        .region(Region.EUROPE)
                        .build(),

                "USDAL",
                Location.builder()
                        .name("Dallas")
                        .unlocode(UnLocode.builder()
                                .code("USDAL")
                                .build())
                        .region(Region.NORTH_AMERICA)
                        .build(),

                "SEGOT",
                Location.builder()
                        .name("Göteborg")
                        .unlocode(UnLocode.builder()
                                .code("SEGOT")
                                .build())
                        .region(Region.EUROPE)
                        .build(),

                "FIHEL",
                Location.builder()
                        .name("Helsinki")
                        .unlocode(UnLocode.builder()
                                .code("FIHEL")
                                .build())
                        .region(Region.EUROPE)
                        .build(),

                "AUMEL",
                Location.builder()
                        .name("Melbourne")
                        .unlocode(UnLocode.builder()
                                .code("AUMEL")
                                .build())
                        .region(Region.OCEANIA)
                        .build()

        );
    }

    public static Location location(String unlocode) {
        return Optional.ofNullable(allLocations().get(unlocode))
                .orElseThrow();
    }

    public static Map<String, Cargo> allCargos() {
        return Map.of(

                "75FC0BD4",
                Cargo.builder()
                        .trackingId(TrackingId.builder()
                                .id("75FC0BD4")
                                .build())
                        .origin(UnLocode.of("USDAL"))
                        .delivery(Delivery.builder()
                                .transportStatus(TransportStatus.IN_PORT)
                                .routingStatus(RoutingStatus.ROUTED)
                                .eta(UtcDateTime.of("24-08-2022"))
                                .misdirected(false)
                                .build())
                        .routeSpecification(routeSpecification())
                        .build(),

                "695CF30D",
                Cargo.builder()
                        .trackingId(TrackingId.builder()
                                .id("695CF30D")
                                .build())
                        .origin(UnLocode.of("FIHEL"))
                        .delivery(Delivery.builder()
                                .transportStatus(TransportStatus.ONBOARD_CARRIER)
                                .routingStatus(RoutingStatus.ROUTED)
                                .currentVoyage(VoyageNumber.of("0200S"))
                                .eta(UtcDateTime.of("16-08-2022"))
                                .misdirected(false)
                                .build())
                        .routeSpecification(RouteSpecification.builder()
                                .origin(UnLocode.of("FIHEL"))
                                .destination(UnLocode.of("NLRTM"))
                                .arrivalDeadline(UtcDateTime.of("16-08-2022"))
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
                                .routingStatus(RoutingStatus.ROUTED)
                                .currentVoyage(VoyageNumber.of("0100S"))
                                .eta(UtcDateTime.of("10-08-2022"))
                                .misdirected(false)
                                .build())
                        .routeSpecification(RouteSpecification.builder()
                                .origin(UnLocode.of("USDAL"))
                                .destination(UnLocode.of("JNTKO"))
                                .arrivalDeadline(UtcDateTime.of("10-08-2022"))
                                .build())
                        .build(),

                "CC3A58FB",
                Cargo.builder()
                        .trackingId(TrackingId.of("CC3A58FB"))
                        .origin(UnLocode.of("JNTKO"))
                        .delivery(Delivery.builder()
                                .transportStatus(TransportStatus.IN_PORT)
                                .routingStatus(RoutingStatus.ROUTED)
                                .eta(UtcDateTime.of("16-12-2022"))
                                .misdirected(false)
                                .build())
                        .routeSpecification(RouteSpecification.builder()
                                .origin(UnLocode.of("JNTKO"))
                                .destination(UnLocode.of("USNYC"))
                                .arrivalDeadline(UtcDateTime.of("16-12-2022"))
                                .build())
                        .build()

        );
    }

    public static final Map<Integer, Leg> allLegs = Map.of(
            1,
            Leg.builder()
                    .cargoTrackingId(TrackingId.of("8E062F47"))
                    .voyageNumber(VoyageNumber.of("0100S"))
                    .loadLocation(UnLocode.of("USDAL"))
                    .loadTime(UtcDateTime.of("05-07-2022"))
                    .unloadLocation(UnLocode.of("AUMEL"))
                    .unloadTime(UtcDateTime.of("23-07-2022"))
                    .build(),
            2,
            Leg.builder()
                    .cargoTrackingId(TrackingId.of("8E062F47"))
                    .voyageNumber(VoyageNumber.of("0200S"))
                    .loadLocation(UnLocode.of("AUMEL"))
                    .loadTime(UtcDateTime.of("25-07-2022"))
                    .unloadLocation(UnLocode.of("JNTKO"))
                    .unloadTime(UtcDateTime.of("05-08-2022"))
                    .build()
    );

    public static Cargo cargo(String trackingId) {
        return Optional.ofNullable(allCargos().get(trackingId))
                .orElseThrow();
    }

    public static Leg leg(Integer id) {
        return Optional.ofNullable(allLegs.get(id)).orElseThrow();
    }

    public static Itinerary itinerary(Integer... legs) {
        return Itinerary.builder()
                .legs(Arrays.stream(legs).map(MockModels::leg).toList())
                .build();
    }

}
