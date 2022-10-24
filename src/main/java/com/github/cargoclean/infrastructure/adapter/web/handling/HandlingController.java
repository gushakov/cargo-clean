package com.github.cargoclean.infrastructure.adapter.web.handling;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;

@Tag(name = "Cargo handling")
@RequiredArgsConstructor
@RestController
public class HandlingController {

    @Operation(summary = "Record event", description = "Record handling event")
    @PostMapping("/recordEvent")
    public void recordEvent(
            @Parameter(name = "completion time")
            @RequestParam Instant completionTime) {


    }

}
