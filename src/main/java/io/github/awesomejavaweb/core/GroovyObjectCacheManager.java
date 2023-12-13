package io.github.awesomejavaweb.core;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import groovy.lang.GroovyObject;

import java.time.Duration;

public final class GroovyObjectCacheManager {

    public GroovyObjectCacheManager() {
        throw new UnsupportedOperationException("Utility class should not be instantiated");
    }

    private static final Cache<String, GroovyObject> DEFAULT_CACHE;

    static {
        DEFAULT_CACHE = Caffeine.newBuilder().initialCapacity(1).maximumSize(10).expireAfterAccess(Duration.ofDays(1)).build();
    }

    private static Cache<String, GroovyObject> cache = DEFAULT_CACHE;

    public static void useCustomCacheBuilder(Caffeine<String, GroovyObject> customCacheBuilder) {
        cache = customCacheBuilder.build();
    }

    public static GroovyObject getIfPresent(String key) {
        return cache.getIfPresent(key);
    }

    public static void put(String key, GroovyObject groovyObject) {
        cache.put(key, groovyObject);
    }

}
