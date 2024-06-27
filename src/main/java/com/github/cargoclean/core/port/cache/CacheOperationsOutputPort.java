package com.github.cargoclean.core.port.cache;

public interface CacheOperationsOutputPort {

    /**
     * Clears all caches.
     * @throws CacheOperationError if something went wrong
     */
    void flushAllCaches();

}
