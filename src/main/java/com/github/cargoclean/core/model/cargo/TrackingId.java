package com.github.cargoclean.core.model.cargo;

/*
    Notice on COPYRIGHT:
    -------------------

    Some code in this file is based on, copied from, and or modified from
    the code in the original DDDSample application. Please, see the copyright
    notice in "README.md" and the copy of the original licence in
    "original-license.txt", as well.
 */

import lombok.Builder;
import lombok.Value;

import static com.github.cargoclean.core.model.Assert.notBlank;

/**
 * Modeled after original "se.citerus.dddsample.domain.model.cargo.TrackingId".
 */
@Value
public class TrackingId {

    String id;

    @Builder
    public TrackingId(String id) {
        // Make sure we cannot construct an invalid tracking ID.
        this.id = notBlank(id);
    }

    @Override
    public String toString() {
        return id;
    }

    public static TrackingId of(String id) {
        return TrackingId.builder()
                .id(id)
                .build();
    }
}
