package com.github.cargoclean.infrastructure.adapter;

import lombok.extern.slf4j.Slf4j;

/*
    Point of interest:
    -----------------
    Which component is responsible for rolling-back the transactions?
    In this implementation, it is the presenter which triggers
    the rollback during error presentation logic. Another way,
    would be to have this functionality in the persistence gateway,
    and let each use case call the rollback operation on the output
    port before passing the control to the presenter. But in this
    way one need to be careful not to forget to call the gateway
    before presenting any error which needs to trigger the rollback.
 */

@Slf4j
public abstract class AbstractErrorHandler {

    protected void logError(Exception t) {
        log.error(t.getMessage(), t);
    }

}
