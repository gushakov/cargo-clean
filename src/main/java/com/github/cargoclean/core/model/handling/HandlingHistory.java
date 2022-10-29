package com.github.cargoclean.core.model.handling;

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
import lombok.experimental.FieldDefaults;
import org.springframework.util.comparator.Comparators;

import java.util.*;

/*
    Modeled after "se.citerus.dddsample.domain.model.handling.HandlingHistory".
 */

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class HandlingHistory {

    private static final Comparator<HandlingEvent> BY_COMPLETION_TIME_NATURAL_COMPARATOR =
            (event1, event2) -> Comparators.comparable().compare(event1.getCompletionTime(),
                    event2.getCompletionTime());


    List<HandlingEvent> handlingEvents;

    @Builder
    public HandlingHistory(List<HandlingEvent> handlingEvents) {
        // Discard any duplicates, see "HandlingEvent" equals method for more details.
        this.handlingEvents = new ArrayList<>(Set.copyOf(handlingEvents));
        this.handlingEvents.sort(BY_COMPLETION_TIME_NATURAL_COMPARATOR);
    }

    public Optional<HandlingEvent> mostRecentlyCompletedEvent() {
        if (handlingEvents.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.ofNullable(handlingEvents.get(handlingEvents.size() - 1));
        }
    }

}
