package com.github.cargoclean.core.model.voyage;

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

import static com.github.cargoclean.core.model.Assert.notBlank;

/**
 * Modeled after original "se.citerus.dddsample.domain.model.voyage.VoyageNumber".
 */
@Value
public class VoyageNumber {

    String number;

    @Builder
    public VoyageNumber(String number) {
        this.number = notBlank(number);
    }

    public static VoyageNumber of(String number) {
        return VoyageNumber.builder()
                .number(number)
                .build();
    }

    @Override
    public String toString() {
        return number;
    }
}
