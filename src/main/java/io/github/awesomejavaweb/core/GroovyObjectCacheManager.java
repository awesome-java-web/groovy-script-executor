package io.github.awesomejavaweb.core;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import groovy.lang.GroovyObject;

import java.time.Duration;

public final class GroovyObjectCacheManager {

    public GroovyObjectCacheManager() {
        throw new UnsupportedOperationException("Utility class should not be instantiated");
    }

    private static final Cache<String, GroovyObject> CACHE = Caffeine.newBuilder()
        .expireAfterAccess(Duration.ofDays(1))
        .initialCapacity(1)
        .maximumSize(10)
        .build();

    public static GroovyObject getIfPresent(String key) {
        return CACHE.getIfPresent(key);
    }

    public static void put(String key, GroovyObject groovyObject) {
        CACHE.put(key, groovyObject);
    }

}
