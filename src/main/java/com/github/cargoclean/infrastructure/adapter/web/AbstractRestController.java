package com.github.cargoclean.infrastructure.adapter.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Map;

/**
 * @see AbstractWebController
 */
@Slf4j
public abstract class AbstractRestController {

    /*
        This will be called for any exception not handled
        by the presenters. For example, when there is a
        problem with binding request parameters.
     */

    @ExceptionHandler
    public ResponseEntity<Map<String, String>> handleRestError(Exception e) {
        log.error(e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("error", e.getMessage()));
    }

}
