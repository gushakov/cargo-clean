package com.github.cargoclean.core.model.voyage;

/*
    Notice on COPYRIGHT:
    -------------------

    Some code in this file is based on, copied from, and or modified from
    the code in the original DDDSample application. Please, see the copyright
    notice in "README.md" and the copy of the original licence in
    "original-license.txt", as well.
 */

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotNull;

/**
 * Modeled after original "se.citerus.dddsample.domain.model.voyage.Voyage".
 */
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Builder
public class Voyage {

    @EqualsAndHashCode.Include
    Integer id;

    @NotNull
    @EqualsAndHashCode.Include
    VoyageNumber voyageNumber;

    public Voyage withNullId(){
        return newVoyage().id(null).build();
    }

    private VoyageBuilder newVoyage(){
        return Voyage.builder()
                .id(id)
                .voyageNumber(voyageNumber);
    }

}
