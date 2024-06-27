package com.github.cargoclean.infrastructure.config;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.cargoclean.infrastructure.adapter.CacheConstants;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.time.Duration;
import java.util.Collections;

/*
    References
    ----------

    1. Custom Caffeine caches: https://stackoverflow.com/questions/44507309/multiple-caffeine-loadingcaches-added-to-spring-caffeinecachemanager/44561626#44561626
 */


@Configuration
public class CacheConfig {

    /*
        Point of interest:
        -----------------

        1.  We are creating "CacheManager" (Spring abstraction) bean backed by Caffeine cache
            as our cache provider.
     */


    @Bean("cacheManager")
    @Qualifier("caffeine")
    @Primary
    public CacheManager caffeineCacheManager() {

        final CaffeineCacheManager cacheManager = new CaffeineCacheManager();

        cacheManager.setCacheNames(Collections.emptyList());

        // make cache for locations

        cacheManager.registerCustomCache(CacheConstants.LOCATION_CACHE_NAME, makeCache(CacheConstants.LOCATION_CACHE_INITIAL_CAPACITY,
                CacheConstants.LOCATION_CACHE_MAXIMUM_SIZE, CacheConstants.LOCATION_CACHE_EXPIRE_AFTER_WRITE_DURATION));

        return cacheManager;
    }

    private Cache<Object, Object> makeCache(int initialCapacity, long maximumSize, Duration expireAfterWrite) {
        return Caffeine.newBuilder()
                .initialCapacity(initialCapacity)
                .maximumSize(maximumSize)
                .expireAfterWrite(expireAfterWrite)
                .build();
    }

}
