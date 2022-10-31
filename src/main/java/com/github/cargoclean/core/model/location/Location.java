package com.github.cargoclean.core.model.location;

/*
    Notice on COPYRIGHT:
    -------------------

    Some code in this file is based on, copied from, and or modified from
    the code in the original DDDSample application. Please, see the copyright
    notice in "README.md" and the copy of the original licence in
    "original-license.txt", as well.
 */

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotNull;

/**
 * Modeled after original "se.citerus.dddsample.domain.model.location.Location".
 */
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Builder
public class Location {

    /*
        Copied from "se.citerus.dddsample.domain.model.location.Location#UNKNOWN".
     */
    public static final Location UNKNOWN = Location.builder()
            .name("UNKNOWN")
            .unlocode(UnLocode.of("XXXXX"))
            .build();

    @EqualsAndHashCode.Include
    @NotNull
    UnLocode unlocode;

    @NotNull
    String name;

    @Override
    public String toString() {
        return "%s, %s".formatted(name, unlocode);
    }
}
