package com.github.cargoclean.core.model.location;

import com.github.cargoclean.core.GenericCargoError;
import lombok.Getter;

public class DuplicateLocationError extends GenericCargoError {

    @Getter
    private transient final Location duplicateLocation;

    public DuplicateLocationError(Location duplicateLocation) {
        super("Location %s exists already".formatted(duplicateLocation));
        this.duplicateLocation = duplicateLocation;
    }
}
