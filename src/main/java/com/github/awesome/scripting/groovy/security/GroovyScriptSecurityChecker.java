package com.github.awesome.scripting.groovy.security;

import java.util.Collection;

public interface GroovyScriptSecurityChecker {

    Collection<String> shouldInterceptKeywords();

    void checkOrThrow(String script);
}
