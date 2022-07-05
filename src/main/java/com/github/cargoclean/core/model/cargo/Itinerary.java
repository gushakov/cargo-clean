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

import java.util.List;


/**
 * Modeled after "se.citerus.dddsample.domain.model.cargo.Itinerary".
 */
@Value
@Builder
public class Itinerary {

    @Builder.Default
    List<Leg> legs = List.of();

}
