package com.github.cargoclean.infrastructure.config;

import org.springframework.cache.CacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TestCacheConfig {

    /*
            Point of interest:
            -----------------
            For tests we need a bean of type "CacheManager" so
            it can be wired into our gateway. We declare a
            test-local context configuration with a bean of type
            "org.springframework.cache.concurrent.ConcurrentMapCacheManager"
            which will dynamically create an empty cache during
            the first call.
         */

    @Bean
    public CacheManager cacheManager() {
        return new ConcurrentMapCacheManager();
    }

}
