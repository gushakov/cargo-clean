package com.github.cargoclean.core.model.location;

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

/**
 * Modeled after original "se.citerus.dddsample.domain.model.location.Location".
 */
@Value
@Builder
public class Location {

    // needed to map to auto-generated serial ID in the corresponding entity
    Integer id;
    UnLocode unLocode;
    String name;

}
