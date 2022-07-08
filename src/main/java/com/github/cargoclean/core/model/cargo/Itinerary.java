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
import java.util.Optional;

/**
 * Modeled after original "se.citerus.dddsample.domain.model.cargo.Itinerary".
 */
@Value
public class Itinerary {

    @Builder.Default
    List<Leg> legs;

    public static Itinerary of(List<Leg> legs){
        return Itinerary.builder()
                .legs(legs)
                .build();
    }

    @Builder
    public Itinerary(List<Leg> legs) {
        this.legs = List.copyOf(Optional.ofNullable(legs).orElse(List.of()));
    }

    public Leg first(){
       return legs.stream().findFirst().orElse(null);
    }

    public Leg last(){
        return legs.stream().skip(legs.size()-1).findFirst().orElse(null);
    }
}
