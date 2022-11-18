package com.github.cargoclean.infrastructure.adapter;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AbstractErrorHandler {

    protected void logError(Exception t) {
        log.error(t.getMessage(), t);
    }

}
