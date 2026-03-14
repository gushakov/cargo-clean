package com.github.cargoclean.infrastructure.adapter.cache;

import com.github.cargoclean.infrastructure.adapter.cache.CacheUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

/**
 * Registers a transaction synchronization callback which will
 * clear all caches after a rollback of a transaction.
 * This keeps cache invalidation concerns within the caching layer
 * rather than coupling them to the transaction adapter.
 *
 * @see CacheUtils#clearAllCaches(CacheManager)
 */
@Component
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
@Slf4j
public class SpringCacheInvalidationOnRollback implements CacheInvalidationOnRollback {

    CacheManager cacheManager;

    @Override
    public void register() {
        if (!TransactionSynchronizationManager.isActualTransactionActive()) {
            return;
        }
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCompletion(int status) {
                if (status == TransactionSynchronization.STATUS_ROLLED_BACK) {
                    log.debug("[Cache] Clearing all caches on rollback");
                    CacheUtils.clearAllCaches(cacheManager);
                }
            }
        });
    }
}
