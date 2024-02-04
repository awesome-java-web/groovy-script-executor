package com.github.awesome.scripting.groovy.cache;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.google.common.cache.CacheBuilder;
import groovy.lang.GroovyObject;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * 这是一个本地缓存聚合接口，主要是为了适配底层不同的本地缓存框架，在上层抽象一套统一的缓存操作接口。
 * <p>
 * 如果想要新接入一种缓存框架，必须实现这个接口，比如像 {@link CaffeineLocalCache} 和 {@link GuavaLocalCache} 这样。
 *
 * @author codeboyzhou
 * @since 0.1.0
 */
public interface LocalCache {

    /**
     * 从缓存中获取指定key的缓存对象，如果缓存不存在，则返回 {@code null}
     *
     * @param key 缓存key
     * @return {@link GroovyObject} 缓存对象
     */
    @Nullable
    GroovyObject getIfPresent(final String key);

    /**
     * 将指定key的缓存对象存入缓存，key和缓存对象都不能为 {@code null}
     *
     * @param key          缓存key
     * @param groovyObject {@link GroovyObject} 缓存对象
     */
    void put(@Nonnull final String key, @Nonnull GroovyObject groovyObject);

    /**
     * 获取缓存的统计信息，注意使用这个方法前必须保证开启了缓存框架的统计功能
     * <p>
     * 比如像 {@link Caffeine#recordStats()} 和 {@link CacheBuilder#recordStats()}
     *
     * @return 缓存的统计信息
     */
    String stats();

}
