package com.github.cargoclean.core.model.handling;

import com.github.cargoclean.core.model.cargo.TrackingId;
import com.github.cargoclean.core.model.location.UnLocode;
import com.github.cargoclean.core.model.voyage.VoyageNumber;
import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import java.time.ZonedDateTime;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Builder
public class HandlingEvent {

    @EqualsAndHashCode.Include
    @NotNull
    EventId eventId;

    VoyageNumber voyageNumber;

    @NotNull
    UnLocode location;

    @NonNull
    TrackingId cargoId;

    @NotNull
    @Past
    ZonedDateTime completionTime;

    @NotNull
    @Past
    ZonedDateTime registrationTime;

    @NotNull
    HandlingEventType type;

    Integer version;

}
