package com.github.cargoclean.core.model.cargo;

import com.github.cargoclean.core.model.UtcDateTime;
import com.github.cargoclean.core.model.location.UnLocode;
import com.github.cargoclean.core.model.voyage.VoyageNumber;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class LegDto {

    String cargoTrackingId;
    String voyageNumber;
    String from;
    UtcDateTime loadTime;
    String to;
    UtcDateTime unloadTime;

    public static LegDto of(Leg leg) {
        return LegDto.builder()
                .cargoTrackingId(leg.getCargoTrackingId().getId())
                .voyageNumber(leg.getVoyageNumber().getNumber())
                .from(leg.getLoadLocation().getCode())
                .loadTime(leg.getLoadTime())
                .to(leg.getUnloadLocation().getCode())
                .unloadTime(leg.getUnloadTime())
                .build();
    }

    public Leg toLeg() {
        return Leg.builder()
                .cargoTrackingId(TrackingId.of(cargoTrackingId))
                .voyageNumber(VoyageNumber.of(voyageNumber))
                .loadLocation(UnLocode.of(from))
                .loadTime(loadTime)
                .unloadLocation(UnLocode.of(to))
                .unloadTime(unloadTime)
                .build();
    }
}
