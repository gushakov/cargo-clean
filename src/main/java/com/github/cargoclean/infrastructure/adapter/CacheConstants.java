package com.github.cargoclean.infrastructure.adapter;

import java.time.Duration;

/**
 * Constants used to configure and access caches. Will be used by several different
 * adapters.
 */
public class CacheConstants {

    public static final String LOCATION_CACHE_NAME = "locationCache";

    public static final int LOCATION_CACHE_INITIAL_CAPACITY = 20;

    public static final long LOCATION_CACHE_MAXIMUM_SIZE = 100L;

    public static final Duration LOCATION_CACHE_EXPIRE_AFTER_WRITE_DURATION = Duration.ofHours(1L);

}
