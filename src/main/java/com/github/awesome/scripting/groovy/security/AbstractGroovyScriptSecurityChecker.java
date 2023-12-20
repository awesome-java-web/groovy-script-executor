package com.github.awesome.scripting.groovy.security;

import com.github.awesome.scripting.groovy.exception.GroovyScriptSecurityException;

import java.util.Collection;

public abstract class AbstractGroovyScriptSecurityChecker implements GroovyScriptSecurityChecker {

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
                    keyword, DefaultShouldInterceptKeywords.fromKeyword(keyword), keywords
                );
                throw new GroovyScriptSecurityException(errorMessage);
            }
        }
    }

}
