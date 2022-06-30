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

/**
 * Modeled after original "se.citerus.dddsample.domain.model.location.Location".
 */
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Builder
public class Location {

    // needed to map to auto-generated serial ID in the corresponding entity
    @EqualsAndHashCode.Include(rank = 1)
    Integer id;
    @EqualsAndHashCode.Include(rank = 2)
    UnLocode unLocode;
    String name;

    @Override
    public String toString() {
        return "Location{" +
                "unLocode=" + unLocode +
                '}';
    }
}
