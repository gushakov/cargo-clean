package com.github.cargoclean.infrastructure.adapter.web.consignment;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ConsignmentEntryForm {

    private String cargoTrackingId;
    private int quantityInContainers;

}
