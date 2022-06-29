package com.github.cargoclean.core.model.cargo;

/*
    Notice on COPYRIGHT:
    -------------------

    Some code in this file is based on, copied from, and or modified from
    the code in the original DDDSample application. Please, see the copyright
    notice in "README.md" and the copy of the original licence in
    "original-license.txt", as well.
 */

import com.github.cargoclean.core.annotation.Default;
import lombok.Builder;
import lombok.Value;

/**
 * Modeled after original "se.citerus.dddsample.domain.model.cargo.TrackingId".
 */
@Value
public class TrackingId {

    String id;

    @Builder
    @Default
    public TrackingId(String id) {
        if (id == null || id.isEmpty() || id.isBlank()){
            throw new IllegalArgumentException("Invalid tracking ID: <%s>".formatted(id));
        }
        this.id = id;
    }

    @Override
    public String toString() {
        return id;
    }

    public static TrackingId of(String id){
        return TrackingId.builder()
                .id(id)
                .build();
    }
}
