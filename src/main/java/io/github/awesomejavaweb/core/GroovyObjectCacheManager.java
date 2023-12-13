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

    private static Caffeine<String, GroovyObject> customCacheBuilder;

    public static void useCustomCacheBuilder(Caffeine<String, GroovyObject> customCacheBuilder) {
        GroovyObjectCacheManager.customCacheBuilder = customCacheBuilder;
    }

    public static Caffeine<String, GroovyObject> getCustomCacheBuilder() {
        return customCacheBuilder;
    }

    public static GroovyObject getIfPresent(String key) {
        Cache<String, GroovyObject> cache = customCacheBuilder == null ? DEFAULT_CACHE : customCacheBuilder.build();
        return cache.getIfPresent(key);
    }

    public static void put(String key, GroovyObject groovyObject) {
        Cache<String, GroovyObject> cache = customCacheBuilder == null ? DEFAULT_CACHE : customCacheBuilder.build();
        cache.put(key, groovyObject);
    }

}
