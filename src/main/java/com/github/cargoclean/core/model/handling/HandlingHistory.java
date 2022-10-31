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

import java.util.*;

/*
    References:
    ----------

    1. JavaDoc for "java.util.LinkedHashSet".
 */

/*
    Modeled after "se.citerus.dddsample.domain.model.handling.HandlingHistory".
 */

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class HandlingHistory {

    private static final Comparator<HandlingEvent> BY_COMPLETION_LATEST_FIRST_COMPARATOR =
            Comparator.comparing(HandlingEvent::getCompletionTime).reversed();

    List<HandlingEvent> handlingEvents;

    @Builder
    public HandlingHistory(List<HandlingEvent> handlingEvents) {

        if (handlingEvents == null || handlingEvents.isEmpty()) {
            this.handlingEvents = List.of();
            return;
        }

        // Sort the events by completion time, the latest event first
        List<HandlingEvent> sortedEvents = new ArrayList<>(List.copyOf(handlingEvents));
        sortedEvents.sort(BY_COMPLETION_LATEST_FIRST_COMPARATOR);

        // Create a set, effectively eliminating the duplicate events, "LinkedHashSet"
        // guarantees the order of traversal.
        LinkedHashSet<HandlingEvent> set = new LinkedHashSet<>(sortedEvents);

        this.handlingEvents = new ArrayList<>(set.stream().toList());
    }

    public Optional<HandlingEvent> mostRecentlyCompletedEvent() {
        if (handlingEvents.isEmpty()) {
            return Optional.empty();
        } else {
            // Note, this is different from the original "DDDSample":
            // our events are sorted with by the latest completion time
            // first.
            return Optional.ofNullable(handlingEvents.get(0));
        }
    }

    int numberOfEvents() {
        return handlingEvents.size();
    }

}
