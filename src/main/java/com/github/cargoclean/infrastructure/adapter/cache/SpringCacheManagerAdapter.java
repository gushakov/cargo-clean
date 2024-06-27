package com.github.cargoclean.infrastructure.adapter.cache;

import com.github.cargoclean.core.port.cache.CacheOperationsOutputPort;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

@Service
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
@Slf4j
public class SpringCacheManagerAdapter implements CacheOperationsOutputPort {

    CacheManager cacheManager;

    @Override
    public void flushAllCaches() {
        log.debug("[Cache] Flushing all caches");
        for (String cacheName : cacheManager.getCacheNames()) {
            Cache cache = cacheManager.getCache(cacheName);
            if (cache != null) {
                cache.invalidate();
            }
        }
    }
}
