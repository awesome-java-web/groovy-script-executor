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

    private static Cache<String, GroovyObject> customCache;

    public static void useCustomCache(Cache<String, GroovyObject> customCache) {
        GroovyObjectCacheManager.customCache = customCache;
    }

    public static GroovyObject getIfPresent(String key) {
        return customCache == null ? DEFAULT_CACHE.getIfPresent(key) : customCache.getIfPresent(key);
    }

    public static void put(String key, GroovyObject groovyObject) {
        Cache<String, GroovyObject> cache = customCache == null ? DEFAULT_CACHE : customCache;
        cache.put(key, groovyObject);
    }

}
