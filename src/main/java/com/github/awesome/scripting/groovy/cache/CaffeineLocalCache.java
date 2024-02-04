package com.github.awesome.scripting.groovy.cache;

import com.github.benmanes.caffeine.cache.Cache;
import groovy.lang.GroovyObject;

import javax.annotation.Nonnull;

/**
 * 适配 Caffeine 作为本地缓存框架
 *
 * @author codeboyzhou
 * @since 0.1.0
 */
public class CaffeineLocalCache implements LocalCache {

    /**
     * Caffeine 缓存实例对象
     *
     * @see Cache
     */
    private final Cache<String, GroovyObject> cache;

    /**
     * 构造方法完成对象初始化
     *
     * @param cache 缓存实例对象
     */
    public CaffeineLocalCache(Cache<String, GroovyObject> cache) {
        this.cache = cache;
    }

    @Override
    public GroovyObject getIfPresent(final String key) {
        return cache.getIfPresent(key);
    }

    @Override
    public void put(@Nonnull final String key, @Nonnull GroovyObject groovyObject) {
        cache.put(key, groovyObject);
    }

    @Override
    public String stats() {
        return String.format("[%s]%s", getClass().getSimpleName(), cache.stats());
    }

}
