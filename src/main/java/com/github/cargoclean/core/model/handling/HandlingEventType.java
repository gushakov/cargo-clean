package com.github.cargoclean.core.model.handling;
/*
    Notice on COPYRIGHT:
    -------------------

    Some code in this file is based on, copied from, and or modified from
    the code in the original DDDSample application. Please, see the copyright
    notice in "README.md" and the copy of the original licence in
    "original-license.txt", as well.
 */


/**
 * Modeled after "se.citerus.dddsample.domain.model.handling.HandlingEvent.Type".
 */
public enum HandlingEventType {

    LOAD(true),
    UNLOAD(true),
    RECEIVE(false),
    CUSTOMS(false);

    private final boolean voyageRequired;

    HandlingEventType(boolean voyageRequired) {

        this.voyageRequired = voyageRequired;
    }

    public boolean requiresVoyage() {
        return voyageRequired;
    }
}
