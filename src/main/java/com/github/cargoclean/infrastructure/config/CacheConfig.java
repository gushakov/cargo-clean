package com.github.cargoclean.infrastructure.config;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

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
        We are creating "CacheManager" (Spring abstraction) bean backed
        by Caffeine cache as our cache provider. Then we register a cache
        for each model (aggregate root) which we want to cache.
     */


    @Bean("cacheManager")
    @Qualifier("caffeine")
    @Primary
    public CacheManager caffeineCacheManager(CargoCleanProperties props) {

        final CaffeineCacheManager cacheManager = new CaffeineCacheManager();

        cacheManager.setCacheNames(Collections.emptyList());

        // make cache for locations

        cacheManager.registerCustomCache(props.getLocationCache().getName(),
                makeCache(props));

        return cacheManager;
    }

    private Cache<Object, Object> makeCache(CargoCleanProperties props) {
        return Caffeine.newBuilder()
                .initialCapacity(props.getLocationCache().getInitCapacity())
                .maximumSize(props.getLocationCache().getMaximumSize())
                .expireAfterWrite(props.getLocationCache().getTtl())
                .build();
    }

}
