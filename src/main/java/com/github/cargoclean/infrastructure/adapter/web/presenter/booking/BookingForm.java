package com.github.cargoclean.infrastructure.adapter.web.presenter.booking;

import com.github.cargoclean.core.model.location.Location;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
public class BookingForm {

    private Location origin;
    private Location destination;

    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private LocalDate deliveryDeadline;

}
