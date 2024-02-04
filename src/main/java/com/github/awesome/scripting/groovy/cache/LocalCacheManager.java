package com.github.awesome.scripting.groovy.cache;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import groovy.lang.GroovyObject;

import javax.annotation.Nonnull;
import java.time.Duration;

/**
 * 本地缓存管理器，这个类相当于所有缓存框架对外的门面，负责与外部其它类做缓存相关的接口交互。
 *
 * @author codeboyzhou
 * @since 0.1.0
 */
public class LocalCacheManager implements LocalCache {

    /**
     * 使用统一接口，屏蔽具体缓存框架的实现差异
     */
    private LocalCache localCache;

    /**
     * 创建当前类的一个新的对象
     *
     * @return 当前类的实例对象
     */
    public static LocalCacheManager newBuilder() {
        return new LocalCacheManager();
    }

    /**
     * 决定使用哪个缓存框架，比如
     * <pre>{@code
     * use(new CaffeineLocalCache(Caffeine.newBuilder().build()))
     * use(new GuavaLocalCache(CacheBuilder.newBuilder().build()))
     * }</pre>
     *
     * @param localCache 具体的缓存框架适配器对象
     * @return 当前类的实例对象，返回 {@code this} 方便链式调用
     * @see CaffeineLocalCache
     * @see GuavaLocalCache
     * @see LocalCache
     */
    public LocalCacheManager use(LocalCache localCache) {
        this.localCache = localCache;
        return this;
    }

    /**
     * 使用默认的缓存框架(Caffeine)，如果你想使用自定义的缓存框架以及配置，请使用 {@link #use(LocalCache)} 方法
     *
     * @return 当前类的实例对象，返回 {@code this} 方便链式调用
     */
    public LocalCacheManager useDefaultCache() {
        Cache<String, GroovyObject> defaultCache = Caffeine.newBuilder()
                .initialCapacity(1)
                .maximumSize(10)
                .expireAfterAccess(Duration.ofDays(1))
                .recordStats()
                .build();
        localCache = new CaffeineLocalCache(defaultCache);
        return this;
    }

    @Override
    public GroovyObject getIfPresent(final String key) {
        return localCache.getIfPresent(key);
    }

    @Override
    public void put(@Nonnull final String key, @Nonnull GroovyObject groovyObject) {
        localCache.put(key, groovyObject);
    }

    @Override
    public String stats() {
        return localCache.stats();
    }

}
