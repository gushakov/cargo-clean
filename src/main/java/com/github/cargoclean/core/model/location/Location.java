package com.github.cargoclean.core.model.location;

/*
    Notice on COPYRIGHT:
    -------------------

    Some code in this file is based on, copied from, and or modified from
    the code in the original DDDSample application. Please, see the copyright
    notice in "README.md" and the copy of the original licence in
    "original-license.txt", as well.
 */

import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import static com.github.cargoclean.core.model.Assert.notBlank;
import static com.github.cargoclean.core.model.Assert.notNull;


/**
 * Modeled after original "se.citerus.dddsample.domain.model.location.Location".
 */
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Location {

    /*
        Copied from "se.citerus.dddsample.domain.model.location.Location#UNKNOWN".
     */
    public static final Location UNKNOWN = Location.builder()
            .name("UNKNOWN")
            .unlocode(UnLocode.of("XXXXX"))
            .region(Region.UNKNOWN)
            .build();

    @EqualsAndHashCode.Include
    UnLocode unlocode;

    String name;

    Region region;

    Integer version;

    @Builder
    public Location(UnLocode unlocode, String name, Region region, Integer version) {
        this.unlocode = notNull(unlocode);
        this.name = notBlank(name);
        this.region = notNull(region);
        this.version = version;
    }

    @Override
    public String toString() {
        return "%s (%s)".formatted(name, unlocode);
    }
}
