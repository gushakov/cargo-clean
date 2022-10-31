package com.github.cargoclean.infrastructure.adapter.web.booking;

import lombok.Builder;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

@Data
@Builder
public class BookingForm {

    private List<String> locations;

    private String origin;
    private String destination;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date deliveryDeadline;

}
