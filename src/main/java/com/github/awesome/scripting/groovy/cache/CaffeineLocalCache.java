package com.github.awesome.scripting.groovy.cache;

import com.github.benmanes.caffeine.cache.Cache;
import groovy.lang.GroovyObject;

public class CaffeineLocalCache implements LocalCache {

    private final Cache<String, GroovyObject> cache;

    public CaffeineLocalCache(Cache<String, GroovyObject> cache) {
        this.cache = cache;
    }

    @Override
    public GroovyObject getIfPresent(String key) {
        return this.cache.getIfPresent(key);
    }

    @Override
    public void put(String key, GroovyObject groovyObject) {
        this.cache.put(key, groovyObject);
    }

    @Override
    public String stats() {
        return this.cache.stats().toString();
    }

}
