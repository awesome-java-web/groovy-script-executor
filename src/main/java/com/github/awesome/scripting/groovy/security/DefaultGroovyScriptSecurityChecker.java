package com.github.awesome.scripting.groovy.security;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

public class DefaultGroovyScriptSecurityChecker extends AbstractGroovyScriptSecurityChecker {

    @Override
    public Collection<String> shouldInterceptKeywords() {
        DefaultShouldInterceptKeywords[] keywords = DefaultShouldInterceptKeywords.values();
        return Arrays.stream(keywords).map(DefaultShouldInterceptKeywords::getKeyword).collect(Collectors.toSet());
    }

}
