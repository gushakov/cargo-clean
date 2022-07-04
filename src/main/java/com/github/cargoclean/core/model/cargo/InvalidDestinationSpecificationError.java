package com.github.cargoclean.core.model.cargo;

import com.github.cargoclean.core.GenericCargoError;
import com.github.cargoclean.core.model.location.UnLocode;
import lombok.Getter;

public class InvalidDestinationSpecificationError extends GenericCargoError {

    @Getter
    private final TrackingId trackingId;

    public InvalidDestinationSpecificationError(TrackingId trackingId, UnLocode origin, UnLocode destination) {
        super("Cargo %s has invalid route specification: destination <%s> cannot be the same as origin: <%s>."
                .formatted(trackingId, destination, origin));
        this.trackingId = trackingId;
    }

}
