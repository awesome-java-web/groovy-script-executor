package com.github.awesome.scripting.groovy.cache;

import javax.annotation.Nullable;
import java.util.LinkedHashMap;
import java.util.Map;

public class SimpleMemoryLruCache<K, V> {

    private long hitCount;

    private long missCount;

    private final Map<K, V> cache;

    public SimpleMemoryLruCache(final int maxSize) {
        this.cache = new LinkedHashMap<K, V>(1, 0.75f, true) {
            @Override
            protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
                return size() > maxSize;
            }
        };
    }

    @Nullable
    public final V getIfPresent(final K key) {
        V value;
        synchronized (this) {
            value = cache.get(key);
            if (value == null) {
                missCount++;
                return null;
            } else {
                hitCount++;
                return value;
            }
        }
    }

    public final void put(final K key, final V value) {
        if (key == null || value == null) {
            throw new NullPointerException("key == null || value == null");
        }

        synchronized (this) {
            cache.put(key, value);
        }
    }

    public final String stats() {
        final String hitRate = String.format("%.2f%%", (double) hitCount / (hitCount + missCount) * 100);
        return String.format("%s{hitCount=%d, missCount=%d, hitRate=%s}", getClass().getSimpleName(), hitCount, missCount, hitRate);
    }

}
