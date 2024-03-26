package com.github.awesome.scripting.groovy.cache;

import groovy.lang.GroovyObject;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class DefaultLocalCache implements LocalCache {

    private final SimpleMemoryLruCache<String, GroovyObject> cache;

    public DefaultLocalCache(SimpleMemoryLruCache<String, GroovyObject> cache) {
        this.cache = cache;
    }

    @Nullable
    @Override
    public GroovyObject getIfPresent(String key) {
        return cache.getIfPresent(key);
    }

    @Override
    public void put(@Nonnull String key, @Nonnull GroovyObject groovyObject) {
        cache.put(key, groovyObject);
    }

    @Override
    public String stats() {
        return String.format("[%s]%s", getClass().getSimpleName(), cache.stats());
    }

}
