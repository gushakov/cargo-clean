package com.github.cargoclean.infrastructure.adapter.web.editlocations;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AddLocationForm {

    private String unLocode;
    private String location;
    private String region;

    /*
        Point of interest:
        -----------------
        Simple way to declare default values which
        will be used by Lombok if not set explicitly.
     */

    @Builder.Default
    private String unLocodeReferenceUrl = "https://unece.org/trade/cefact/unlocode-code-list-country-and-territory";

    @Builder.Default
    private String hintUnLocode = "AUADL";

    @Builder.Default
    private String hintLocation = "Adelaide";

    @Builder.Default
    private String hintRegion = "Oceania";
}
