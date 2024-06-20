package com.github.cargoclean.core.model.cargo;

/*
    Notice on COPYRIGHT:
    -------------------

    Some code in this file is based on, copied from, and or modified from
    the code in the original DDDSample application. Please, see the copyright
    notice in "README.md" and the copy of the original licence in
    "original-license.txt", as well.
 */

import com.github.cargoclean.core.model.UtcDateTime;
import com.github.cargoclean.core.model.handling.HandlingEvent;
import com.github.cargoclean.core.model.handling.HandlingEventType;
import lombok.Builder;
import lombok.Value;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Modeled after original "se.citerus.dddsample.domain.model.cargo.Itinerary".
 */
@Value
public class Itinerary {

    /*
        Copied from "se.citerus.dddsample.domain.model.cargo.Itinerary#EMPTY_ITINERARY".
     */
    public static final Itinerary EMPTY_ITINERARY = Itinerary.builder().build();

    List<Leg> legs;

    public static Itinerary of(List<Leg> legs) {
        return Itinerary.builder()
                .legs(legs)
                .build();
    }

    @Builder
    public Itinerary(List<Leg> legs) {
        this.legs = List.copyOf(Optional.ofNullable(legs).orElse(List.of()));
    }

    public Leg first() {
        return legs.stream().findFirst().orElse(null);
    }

    public Leg last() {
        return legs.stream().skip(legs.size() - 1L).findFirst().orElse(null);
    }

    public List<Leg> intermediate() {
        int size = legs.size();
        if (size < 3) {
            return List.of();
        }
        return Arrays.stream(Arrays.copyOfRange(legs.toArray(new Leg[0]), 1, size - 1))
                .toList();
    }

    /*
        Copied from "se.citerus.dddsample.domain.model.cargo.Itinerary#finalArrivalDate".
     */
    UtcDateTime finalArrivalDate() {
        final Leg lastLeg = lastLeg();

        if (lastLeg == null) {
            return UtcDateTime.END_OF_DAYS;
        } else {
            return lastLeg.getUnloadTime();
        }
    }

    /*
        Copied from "se.citerus.dddsample.domain.model.cargo.Itinerary#lastLeg".
     */
    Leg lastLeg() {
        if (legs.isEmpty()) {
            return null;
        } else {
            return legs.get(legs.size() - 1);
        }
    }

    /*
        Copied and modified from "se.citerus.dddsample.domain.model.cargo.Itinerary#isExpected".
     */
    public boolean isExpected(final HandlingEvent event) {
        if (legs.isEmpty()) {
            return true;
        }

        if (event.getType() == HandlingEventType.RECEIVE) {
            //Check that the first leg's origin is the event's location
            final Leg leg = legs.get(0);
            return (leg.getLoadLocation().equals(event.getLocation()));
        }

        if (event.getType() == HandlingEventType.LOAD) {
            //Check that the there is one leg with same load location and voyage
            for (Leg leg : legs) {
                if (leg.getLoadLocation().equals(event.getLocation()) &&
                        leg.getVoyageNumber().equals(event.getVoyageNumber()))
                    return true;
            }
            return false;
        }

        if (event.getType() == HandlingEventType.UNLOAD) {
            //Check that the there is one leg with same unload location and voyage
            for (Leg leg : legs) {
                if (leg.getUnloadLocation().equals(event.getLocation()) &&
                        leg.getVoyageNumber().equals(event.getVoyageNumber()))
                    return true;
            }
            return false;
        }

        if (event.getType() == HandlingEventType.CLAIM) {
            //Check that the last leg's destination is from the event's location
            final Leg leg = lastLeg();
            return (leg.getUnloadLocation().equals(event.getLocation()));
        }

        // handling event type is HandlingEventType.CUSTOMS
        return true;
    }

}
