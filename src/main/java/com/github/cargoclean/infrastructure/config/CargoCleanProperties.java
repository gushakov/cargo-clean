package com.github.cargoclean.infrastructure.config;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

import java.time.Duration;

@Getter
@Setter
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@ConfigurationProperties(prefix = "cargo")
public class CargoCleanProperties {

    @Getter
    @Setter
    public static class SlowLoad {
        boolean enabled = false;
        int delayMillis = 150;
    }

    @Getter
    @Setter
    public static class CacheConfig {
        String name;
        int initCapacity = 10;
        long maximumSize = 100L;
        Duration ttl = Duration.ofHours(1);
    }

    @NestedConfigurationProperty
    SlowLoad slowLoad = new SlowLoad();

    @Getter
    CacheConfig locationCache = new CacheConfig();
}
