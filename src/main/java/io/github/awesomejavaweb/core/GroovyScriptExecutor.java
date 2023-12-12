package io.github.awesomejavaweb.core;

import groovy.lang.GroovyClassLoader;
import groovy.lang.GroovyObject;
import io.github.awesomejavaweb.exception.GroovyObjectInvokeMethodException;
import io.github.awesomejavaweb.exception.GroovyScriptParseException;
import io.github.awesomejavaweb.exception.InvalidGroovyScriptException;
import io.github.awesomejavaweb.util.Md5Utils;

public class GroovyScriptExecutor {

    private static final GroovyClassLoader GROOVY_CLASS_LOADER = new GroovyClassLoader();

    public Object execute(final String script, final String function, final Object... parameters) {
        if (script == null || script.trim().isEmpty()) {
            throw new InvalidGroovyScriptException("Groovy script is null or empty");
        }

        // Find groovy object from cache first
        final String trimmedScript = script.trim();
        final String scriptCacheKey = Md5Utils.md5Hex(trimmedScript);
        GroovyObject groovyObjectCache = GroovyObjectCacheManager.getIfPresent(scriptCacheKey);

        // Parse the script and put it into cache instantly if it is not in cache
        if (groovyObjectCache == null) {
            groovyObjectCache = parseScript(trimmedScript);
            GroovyObjectCacheManager.put(scriptCacheKey, groovyObjectCache);
        }

        // Script is parsed successfully
        return invokeMethod(groovyObjectCache, function, parameters);
    }

    private GroovyObject parseScript(final String script) {
        try {
            Class<?> scriptClass = GROOVY_CLASS_LOADER.parseClass(script);
            return (GroovyObject) scriptClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
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
