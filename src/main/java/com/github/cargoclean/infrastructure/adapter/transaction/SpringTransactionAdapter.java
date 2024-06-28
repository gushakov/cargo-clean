package com.github.cargoclean.infrastructure.adapter.transaction;

import com.github.cargoclean.core.port.transaction.TransactionOperationsOutputPort;
import com.github.cargoclean.core.port.transaction.TransactionRunnableWithResult;
import com.github.cargoclean.core.port.transaction.TransactionRunnableWithoutResult;
import com.github.cargoclean.infrastructure.adapter.CacheUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionInterceptor;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.concurrent.atomic.AtomicReference;

/*
    References:
    ----------
    1.  This class is copied from https://github.com/gushakov/cleanddd/blob/main/src/main/java/com/github/cleanddd/infrastructure/adapter/transaction/SpringTransactionAdapter.java
 */

/**
 * Default implementation of {@linkplain TransactionOperationsOutputPort} using Spring's
 * transaction SPI.
 *
 * @see TransactionTemplate
 * @see TransactionInterceptor
 * @see TransactionSynchronizationManager
 */
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
@Slf4j
@Service
public class SpringTransactionAdapter implements TransactionOperationsOutputPort {

    TransactionTemplate transactionTemplate;
    CacheManager cacheManager;

    @Qualifier("read-only")
    TransactionTemplate readOnlyTransactionTemplate;

    @Override
    public void doInTransaction(boolean readOnly, TransactionRunnableWithoutResult runnableWithoutResult) {
        log.debug("[Transaction] Executing runnable (without a result) in a transaction, read-only: {}", readOnly);
        if (readOnly) {
            readOnlyTransactionTemplate.executeWithoutResult(transactionStatus -> runnableWithoutResult.run());
        } else {
            transactionTemplate.executeWithoutResult(transactionStatus -> {
                /*
                    Point of interest:
                    -----------------
                    We need to register a callback which will clear
                    caches in case of a rollback of the current transaction.
                 */
                registerCacheInvalidationOnRollback();
                runnableWithoutResult.run();
            });
        }
    }

    @Override
    public <R> R doInTransactionWithResult(boolean readOnly, TransactionRunnableWithResult<R> runnableWithResult) {
        log.debug("[Transaction] Executing runnable (with a result) in a transaction, read-only: {}", readOnly);
        if (readOnly) {
            return readOnlyTransactionTemplate.execute(transactionStatus -> runnableWithResult.run());
        } else {
            return transactionTemplate.execute(transactionStatus -> {
                registerCacheInvalidationOnRollback();
                return runnableWithResult.run();
            });
        }
    }

    @Override
    public void doAfterCommit(TransactionRunnableWithoutResult runnableWithoutResult) {
        if (!TransactionSynchronizationManager.isActualTransactionActive()) {
            // not in transaction, just execute the runnable
            log.debug("[Transaction] Not in transaction, executing runnable (without a result) from \"doAfterCommit\" directly");
            runnableWithoutResult.run();
            return;
        }

        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCompletion(int status) {
                if (status == TransactionSynchronization.STATUS_COMMITTED) {
                    log.debug("[Transaction][After commit] Executing runnable (without a result) after commit");
                    runnableWithoutResult.run();
                }
            }
        });
    }

    @Override
    public <R> R doAfterCommitWithResult(TransactionRunnableWithResult<R> runnableWithResult) {
        if (!TransactionSynchronizationManager.isActualTransactionActive()) {
            // not in transaction, just execute the runnable
            log.debug("[Transaction] Not in transaction, executing runnable (with a result) from \"doAfterCommitWithResult\" directly");
            return runnableWithResult.run();
        }

        final AtomicReference<R> result = new AtomicReference<>();
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {

            @Override
            public void afterCompletion(int status) {
                if (status == TransactionSynchronization.STATUS_COMMITTED) {
                    log.debug("[Transaction][After commit] Executing runnable (with a result) after commit");
                    result.set(runnableWithResult.run());
                }
            }

        });
        return result.get();
    }

    @Override
    public void doAfterRollback(TransactionRunnableWithoutResult runnableWithoutResult) {
        if (!TransactionSynchronizationManager.isActualTransactionActive()) {
            // not in transaction, just execute the runnable
            log.debug("[Transaction] Not in transaction, executing runnable (without a result) from \"doAfterRollback\" directly");
            runnableWithoutResult.run();
            return;
        }

        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCompletion(int status) {
                if (status == TransactionSynchronization.STATUS_ROLLED_BACK) {
                    log.debug("[Transaction][After rollback] Executing runnable (without a result) after rollback");
                    runnableWithoutResult.run();
                }
            }
        });
    }

    @Override
    public <R> R doAfterRollbackWithResult(TransactionRunnableWithResult<R> runnableWithResult) {
        if (!TransactionSynchronizationManager.isActualTransactionActive()) {
            // not in transaction, just execute the runnable
            log.debug("[Transaction] Not in transaction, executing runnable (with a result) from \"doAfterRollbackWithResult\" directly");
            return runnableWithResult.run();
        }

        final AtomicReference<R> result = new AtomicReference<>();
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCompletion(int status) {
                if (status == TransactionSynchronization.STATUS_ROLLED_BACK) {
                    log.debug("[Transaction] Executing runnable (with a result) after rollback");
                    result.set(runnableWithResult.run());
                }
            }
        });
        return result.get();
    }

    /**
     * Registers a custom transaction synchronization callback which will
     * clear all caches after a rollback of a transaction.
     */
    private void registerCacheInvalidationOnRollback() {
        if (!TransactionSynchronizationManager.isActualTransactionActive()) {
            return;
        }
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCompletion(int status) {
                if (status == TransactionSynchronization.STATUS_ROLLED_BACK) {
                    log.debug("[Transaction] [Cache] Clearing all caches on rollback");
                    CacheUtils.clearAllCaches(cacheManager);
                }
            }
        });
    }
}
