package com.github.awesome.scripting.groovy;

import com.github.awesome.scripting.groovy.cache.LocalCacheManager;
import com.github.awesome.scripting.groovy.exception.GroovyObjectInvokeMethodException;
import com.github.awesome.scripting.groovy.exception.GroovyScriptParseException;
import com.github.awesome.scripting.groovy.exception.InvalidGroovyScriptException;
import com.github.awesome.scripting.groovy.security.DefaultGroovyScriptSecurityChecker;
import com.github.awesome.scripting.groovy.security.GroovyScriptSecurityChecker;
import com.github.awesome.scripting.groovy.util.Md5Utils;
import groovy.lang.GroovyClassLoader;
import groovy.lang.GroovyObject;

import java.io.IOException;

public class GroovyScriptExecutor {

    private LocalCacheManager localCacheManager;

    private GroovyScriptSecurityChecker groovyScriptSecurityChecker;

    public static GroovyScriptExecutor newBuilder() {
        return new GroovyScriptExecutor();
    }

    public GroovyScriptExecutor() {
        this.groovyScriptSecurityChecker = new DefaultGroovyScriptSecurityChecker();
    }

    public GroovyScriptExecutor withCacheManager(LocalCacheManager localCacheManager) {
        this.localCacheManager = localCacheManager;
        return this;
    }

    public GroovyScriptExecutor withSecurityChecker(GroovyScriptSecurityChecker groovyScriptSecurityChecker) {
        this.groovyScriptSecurityChecker = groovyScriptSecurityChecker;
        return this;
    }

    public LocalCacheManager getCacheManager() {
        return localCacheManager;
    }

    public Object execute(final String classScript, final String function, final Object... parameters) {
        if (classScript == null) {
            throw new InvalidGroovyScriptException("Groovy script is null");
        }

        final String trimmedScript = classScript.trim();
        if (trimmedScript.isEmpty()) {
            throw new InvalidGroovyScriptException("Groovy script is empty");
        }

        // Check if the script contains any unsafe keywords
        this.groovyScriptSecurityChecker.checkOrThrow(trimmedScript);

        // Find groovy object from cache first
        final String scriptCacheKey = Md5Utils.md5Hex(trimmedScript);
        GroovyObject groovyObjectCache = this.localCacheManager.getIfPresent(scriptCacheKey);

        // Parse the script and put it into cache instantly if it is not in cache
        if (groovyObjectCache == null) {
            groovyObjectCache = parseClassScript(trimmedScript);
            this.localCacheManager.put(scriptCacheKey, groovyObjectCache);
        }

        // Script is parsed successfully
        return invokeMethod(groovyObjectCache, function, parameters);
    }

    private GroovyObject parseClassScript(final String classScript) {
        try (GroovyClassLoader groovyClassLoader = new GroovyClassLoader()) {
            Class<?> scriptClass = groovyClassLoader.parseClass(classScript);
            return (GroovyObject) scriptClass.newInstance();
        } catch (IOException | InstantiationException | IllegalAccessException e) {
            throw new GroovyScriptParseException("Failed to parse groovy script, the nested exception is: " + e.getMessage());
        }
    }

    private Object invokeMethod(GroovyObject groovyObject, String function, Object... parameters) {
        try {
            return groovyObject.invokeMethod(function, parameters);
        } catch (Exception e) {
            final String errorMessage = String.format("Failed to invoke groovy method '%s', the nested exception is: %s", function, e.getMessage());
            throw new GroovyObjectInvokeMethodException(errorMessage);
        }
    }

}
