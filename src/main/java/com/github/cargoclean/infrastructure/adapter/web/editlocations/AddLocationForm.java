package com.github.cargoclean.infrastructure.adapter.web.editlocations;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AddLocationForm {

    String unLocode;
    String location;
    String region;
    String city;
    String allRegions;

    /*
        Point of interest:
        -----------------
        Simple way to declare default values which
        will be used by Lombok if not set explicitly.
     */

    @Builder.Default
    String unLocodeReferenceUrl = "https://unece.org/trade/cefact/unlocode-code-list-country-and-territory";

    @Builder.Default
    String hintUnLocode = "AUADL";

    @Builder.Default
    String hintLocation = "Adelaide";

    @Builder.Default
    String hintRegion = "Oceania";
}
