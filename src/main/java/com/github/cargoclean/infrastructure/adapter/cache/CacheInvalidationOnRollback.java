package com.github.cargoclean.infrastructure.adapter.cache;

/**
 * Callback to register cache invalidation when a transaction rolls back.
 * Designed to be called from within an active transaction context.
 * When called outside a transaction, implementations should be a no-op.
 */
@FunctionalInterface
public interface CacheInvalidationOnRollback {

    /**
     * Registers a synchronization callback that will clear all caches
     * if the current transaction is rolled back.
     */
    void register();
}
