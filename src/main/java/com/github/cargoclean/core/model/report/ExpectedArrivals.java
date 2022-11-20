package com.github.cargoclean.core.model.report;

import lombok.Value;

/**
 * Value object representing the result of the query for the number
 * of expected arrivals by city of the destination. Notice the use
 * of valid domain model {@code Location}. This object is immutable
 * and can be used as part of Response Model for presentation.
 */
@Value
public class ExpectedArrivals {

    String city;
    Integer numberOfArrivals;

}
