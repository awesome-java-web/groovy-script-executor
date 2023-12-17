package com.github.awesome.scripting.groovy.cache;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import groovy.lang.GroovyObject;

import java.time.Duration;

public class LocalCacheManager implements LocalCache {

    private LocalCache localCache;

    public static LocalCacheManager newBuilder() {
        return new LocalCacheManager();
    }

    public LocalCacheManager use(LocalCache localCache) {
        this.localCache = localCache;
        return this;
    }

    public LocalCacheManager useDefaultCache() {
        Cache<String, GroovyObject> defaultCache = Caffeine.newBuilder()
                .initialCapacity(1)
                .maximumSize(10)
                .expireAfterAccess(Duration.ofDays(1))
                .build();
        this.localCache = new CaffeineLocalCache(defaultCache);
        return this;
    }

    @Override
    public GroovyObject getIfPresent(String key) {
        return this.localCache.getIfPresent(key);
    }

    @Override
    public void put(String key, GroovyObject groovyObject) {
        this.localCache.put(key, groovyObject);
    }

    @Override
    public String stats() {
        return this.localCache.stats();
    }

}
