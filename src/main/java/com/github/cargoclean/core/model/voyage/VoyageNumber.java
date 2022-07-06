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

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * Modeled after original "se.citerus.dddsample.domain.model.voyage.VoyageNumber".
 */
@Value
@Builder
public class VoyageNumber {

    @NotNull
    @NotBlank
    String number;

    public static VoyageNumber of(String number){
        return VoyageNumber.builder()
                .number(number)
                .build();
    }
}
