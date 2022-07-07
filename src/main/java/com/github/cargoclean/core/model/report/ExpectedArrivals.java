package com.github.cargoclean.core.model.report;

import com.github.cargoclean.core.model.location.Location;
import lombok.Value;

@Value
public class ExpectedArrivals {

    Location city;
    Integer numberOfArrivals;

}
