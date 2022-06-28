package com.github.cargoclean.infrastructure.adapter.web.presenter.booking;

import com.github.cargoclean.core.model.location.Location;
import lombok.Builder;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

@Data
@Builder
public class BookingForm {

    private List<Location> locations;

    private Location origin;
    private Location destination;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date deliveryDeadline;

}
