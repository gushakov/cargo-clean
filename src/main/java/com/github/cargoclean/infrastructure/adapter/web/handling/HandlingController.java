package com.github.cargoclean.infrastructure.adapter.web.handling;

import com.github.cargoclean.core.model.handling.HandlingEventType;
import com.github.cargoclean.core.usecase.handling.HandlingInputPort;
import com.github.cargoclean.infrastructure.adapter.web.AbstractRestController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;

/*
    References:
    ----------

    1. Swagger 2, "date-time" format: https://stackoverflow.com/questions/65617640/how-to-use-java-time-instant-with-swagger-2-x
    2. Date-time API paramter: https://stackoverflow.com/questions/65617640/how-to-use-java-time-instant-with-swagger-2-x
    3. DateTimeFormatter, JavaDoc: https://docs.oracle.com/javase/8/docs/api/java/time/format/DateTimeFormatter.html
 */

@Tag(name = "Cargo handling")
@RequiredArgsConstructor
@RestController
public class HandlingController extends AbstractRestController {

    private final ApplicationContext applicationContext;

    @Operation(summary = "Record event", description = "Record handling event")
    @PostMapping("/recordEvent")
    public void recordEvent(

            // voyage number is not required
            @Parameter(name = "voyageNumber", description = "Voyage number")
            @RequestParam(required = false) String voyageNumber,

            @Parameter(name = "location", description = "Location: UnLocode")
            @RequestParam String location,

            @Parameter(name = "cargoId", description = "Cargo tracking ID")
            @RequestParam String cargoId,

            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            @Parameter(name = "completionTime",
                    description = "Event completion date and time (ISO format)",
                    schema = @Schema(type = "string", format = "date-time"))
            @RequestParam Instant completionTime,

            @Parameter(name = "type", description = "Event type: loading, unloading, etc.")
            @RequestParam HandlingEventType type

    ) {

        // register new handling event
        useCase().recordHandlingEvent(voyageNumber, location, cargoId, completionTime, type);

        // we are updating cargo's delivery synchronously
        useCase().updateDeliveryAfterHandlingActivity(cargoId);

    }

    private HandlingInputPort useCase() {
        return applicationContext.getBean(HandlingInputPort.class);
    }

}
