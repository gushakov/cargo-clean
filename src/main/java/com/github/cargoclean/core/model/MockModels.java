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

    private static Map<String, Cargo> allCargos() {
        return Map.of(
                "75FC0BD4",
                Cargo.builder()
                        .trackingId(TrackingId.builder()
                                .id("75FC0BD4")
                                .build())
                        .origin(Location.builder()
                                .name("Dallas")
                                .unLocode(UnLocode.builder()
                                        .code("USDAL")
                                        .build())
                                .build())
                        .delivery(Delivery.builder()
                                .transportStatus(TransportStatus.IN_PORT)
                                .build())
                        .build()
        );
    }

    public static Cargo cargo(String trackingId){
        return Optional.ofNullable(allCargos().get(trackingId))
                .orElseThrow();
    }

}
