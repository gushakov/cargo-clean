package com.github.cargoclean.infrastructure.adapter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.NoTransactionException;
import org.springframework.transaction.interceptor.TransactionInterceptor;

@Slf4j
public abstract class AbstractErrorHandler {

    protected void logErrorAndRollBack(Exception t) {
        log.error(t.getMessage(), t);

        // roll back any transaction, if needed
        // code from: https://stackoverflow.com/a/23502214
        try {
            TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
        } catch (NoTransactionException e) {
            // do nothing if not running in a transactional context
        }
    }

}
