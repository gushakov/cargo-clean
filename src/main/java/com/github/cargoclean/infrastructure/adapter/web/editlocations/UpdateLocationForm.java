package com.github.cargoclean.infrastructure.adapter.web.editlocations;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class UpdateLocationForm {

    List<String> locations;
    String selectedUnlocode;
    String city;
    String region;

}
