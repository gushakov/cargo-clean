package com.github.cargoclean.infrastructure.adapter.web.presenter.routing;

import com.github.cargoclean.core.model.Constants;
import com.github.cargoclean.core.model.cargo.Leg;
import com.github.cargoclean.core.model.cargo.TrackingId;
import com.github.cargoclean.core.model.location.UnLocode;
import com.github.cargoclean.core.model.voyage.VoyageNumber;
import lombok.Builder;
import lombok.Value;

import java.time.ZonedDateTime;
import java.util.Date;

@Value
@Builder
public class LegDto {

    String cargoTrackingId;
    String voyageNumber;
    String from;
    Date loadTime;
    String to;
    Date unloadTime;

    public static LegDto of(Leg leg){
        return LegDto.builder()
                .cargoTrackingId(leg.getCargoTrackingId().getId())
                .voyageNumber(leg.getVoyageNumber().getNumber())
                .from(leg.getLoadLocation().getCode())
                .loadTime(Date.from(leg.getLoadTime().toInstant()))
                .to(leg.getUnloadLocation().getCode())
                .unloadTime(Date.from(leg.getUnloadTime().toInstant()))
                .build();
    }

    public Leg toLeg(){
        return Leg.builder()
                .cargoTrackingId(TrackingId.of(cargoTrackingId))
                .voyageNumber(VoyageNumber.of(voyageNumber))
                .loadLocation(UnLocode.of(from))
                .loadTime(ZonedDateTime.ofInstant(loadTime.toInstant(), Constants.DEFAULT_ZONE_ID))
                .unloadLocation(UnLocode.of(to))
                .unloadTime(ZonedDateTime.ofInstant(unloadTime.toInstant(), Constants.DEFAULT_ZONE_ID))
                .build();
    }
}
