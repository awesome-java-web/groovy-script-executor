package com.github.awesome.scripting.groovy.security;

import com.github.awesome.scripting.groovy.exception.GroovyScriptSecurityException;

import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;

public class DefaultGroovyScriptSecurityChecker implements GroovyScriptSecurityChecker {

    @Override
    public Collection<String> shouldInterceptKeywords() {
        DefaultShouldInterceptKeywords[] keywords = DefaultShouldInterceptKeywords.values();
        return Arrays.stream(keywords).map(DefaultShouldInterceptKeywords::getKeyword).collect(Collectors.toSet());
    }

    @Override
    public void checkOrThrow(String script) {
        Collection<String> keywords = shouldInterceptKeywords();
        if (keywords == null || keywords.isEmpty()) {
            return;
        }
        for (String keyword : keywords) {
            if (script.contains(keyword)) {
                final String errorMessage = String.format(
                    "Groovy script contains potentially unsafe keyword: '%s', %s, please notice the following keywords are all considered unsafe: %s",
                    keyword, Objects.requireNonNull(DefaultShouldInterceptKeywords.fromKeyword(keyword)).getExplanation(), keywords
                );
                throw new GroovyScriptSecurityException(errorMessage);
            }
        }
    }

}
