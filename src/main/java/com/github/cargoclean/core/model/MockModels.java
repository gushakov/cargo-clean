package com.github.cargoclean.core.model;

import com.github.cargoclean.core.model.cargo.Cargo;
import com.github.cargoclean.core.model.cargo.Delivery;
import com.github.cargoclean.core.model.cargo.TrackingId;
import com.github.cargoclean.core.model.cargo.TransportStatus;
import com.github.cargoclean.core.model.location.Location;
import com.github.cargoclean.core.model.location.UnLocode;

import java.util.Map;
import java.util.Optional;

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
                        .id(1)
                        .trackingId(TrackingId.builder()
                                .id("75FC0BD4")
                                .build())
                        .origin(location("USDAL"))
                        .delivery(Delivery.builder()
                                .transportStatus(TransportStatus.IN_PORT)
                                .build())
                        .build(),
                "695CF30D",
                Cargo.builder()
                        .id(2)
                        .trackingId(TrackingId.builder()
                                .id("695CF30D")
                                .build())
                        .origin(location("FIHEL"))
                        .delivery(Delivery.builder()
                                .transportStatus(TransportStatus.CLAIMED)
                                .build())
                        .build()
        );
    }

    public static Cargo cargo(String trackingId) {
        return Optional.ofNullable(allCargos().get(trackingId))
                .orElseThrow();
    }

}
