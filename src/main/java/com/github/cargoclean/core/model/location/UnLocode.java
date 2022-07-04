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

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * Modeled after original "se.citerus.dddsample.domain.model.location.UnLocode".
 */
@Value
public class UnLocode {

    @NotNull
    @NotBlank
    String code;

    @Builder
    public UnLocode(String code) {

        if (code == null ||
                !code.matches("^[a-zA-Z]{2}[a-zA-Z2-9]{3}$")) {
            throw new IllegalArgumentException("Invalid UN/LOCODE: <%s>".formatted(code));
        }
        this.code = code.toUpperCase();
    }

    public static UnLocode of(String code){
        return UnLocode.builder().code(code).build();
    }

    @Override
    public String toString() {
        return code;
    }
}
