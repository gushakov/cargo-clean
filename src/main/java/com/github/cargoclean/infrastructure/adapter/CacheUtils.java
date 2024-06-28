package com.github.cargoclean.infrastructure.adapter;

import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

import java.util.Optional;

/**
 * Helper class with static methods to work with {@linkplain CacheManager}.
 * May be shared by several adapters.
 */
public class CacheUtils {

    /**
     * Invalidates (clears) all caches registered with the given {@code cacheManager}.
     *
     * @param cacheManager cache manager
     * @see Cache#invalidate()
     */
    public static void clearAllCaches(CacheManager cacheManager) {
        cacheManager.getCacheNames().forEach(name -> Optional.ofNullable(cacheManager.getCache(name))
                .orElseThrow().invalidate());
    }

}
