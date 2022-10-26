package com.github.cargoclean.infrastructure.adapter.web.handling;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
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
public class HandlingController {

    @Operation(summary = "Record event", description = "Record handling event")
    @PostMapping("/recordEvent")
    public void recordEvent(

            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            @Parameter(name = "completion time",
                    description = "Event completion date and time (ISO format)",
                    schema = @Schema(type = "string", format = "date-time"))
            @RequestParam Instant completionTime) {

        System.out.println(">>>>>>>>>>>>>>>>>>>>>" + completionTime);

    }

}
