package com.github.awesome.scripting.groovy.cache;

import groovy.lang.GroovyObject;

public interface LocalCache {

    GroovyObject getIfPresent(String key);

    void put(String key, GroovyObject groovyObject);

    String stats();
}
