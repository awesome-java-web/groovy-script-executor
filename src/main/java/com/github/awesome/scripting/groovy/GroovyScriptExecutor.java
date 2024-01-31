package com.github.awesome.scripting.groovy;

import com.github.awesome.scripting.groovy.cache.LocalCacheManager;
import com.github.awesome.scripting.groovy.exception.GroovyObjectInvokeMethodException;
import com.github.awesome.scripting.groovy.exception.GroovyScriptParseException;
import com.github.awesome.scripting.groovy.exception.InvalidGroovyScriptException;
import com.github.awesome.scripting.groovy.util.Md5Utils;
import groovy.lang.GroovyClassLoader;
import groovy.lang.GroovyObject;
import groovy.lang.GroovyShell;
import org.codehaus.groovy.control.CompilerConfiguration;

import java.io.IOException;

public class GroovyScriptExecutor {

    private GroovyScriptCompiler groovyScriptCompiler;

    private LocalCacheManager localCacheManager;

    public static GroovyScriptExecutor newBuilder() {
        return new GroovyScriptExecutor();
    }

    public GroovyScriptExecutor() {
        this.groovyScriptCompiler = GroovyScriptCompiler.asDefault();
        this.localCacheManager = LocalCacheManager.newBuilder().useDefaultCache();
    }

    public GroovyScriptExecutor with(GroovyScriptCompiler groovyScriptCompiler) {
        this.groovyScriptCompiler = groovyScriptCompiler;
        return this;
    }

    public GroovyScriptExecutor with(LocalCacheManager localCacheManager) {
        this.localCacheManager = localCacheManager;
        return this;
    }

    public LocalCacheManager getLocalCacheManager() {
        return localCacheManager;
    }

    public Object execute(final String classScript, final String function, final Object... parameters) {
        if (classScript == null || classScript.trim().isEmpty()) {
            throw new InvalidGroovyScriptException("Groovy script is null or empty");
        }

        // Find groovy object from cache first
        final String trimmedScript = classScript.trim();
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

    public Object evaluate(final String scriptText, final String function, final Object... parameters) {
        if (scriptText == null || scriptText.trim().isEmpty()) {
            throw new InvalidGroovyScriptException("Groovy script is null or empty");
        }

        // Find groovy object from cache first
        final String trimmedScript = scriptText.trim();
        final String scriptCacheKey = Md5Utils.md5Hex(trimmedScript);
        GroovyObject groovyObjectCache = this.localCacheManager.getIfPresent(scriptCacheKey);

        // Parse the script and put it into cache instantly if it is not in cache
        if (groovyObjectCache == null) {
            groovyObjectCache = parseScriptSnippet(trimmedScript);
            this.localCacheManager.put(scriptCacheKey, groovyObjectCache);
        }

        // Script is parsed successfully
        return invokeMethod(groovyObjectCache, function, parameters);
    }

    private GroovyObject parseClassScript(final String classScript) {
        ClassLoader currentClassLoader = getClass().getClassLoader();
        CompilerConfiguration configuration = this.groovyScriptCompiler.getConfiguration();
        try (GroovyClassLoader groovyClassLoader = new GroovyClassLoader(currentClassLoader, configuration)) {
            Class<?> scriptClass = groovyClassLoader.parseClass(classScript);
            return (GroovyObject) scriptClass.newInstance();
        } catch (IOException | InstantiationException | IllegalAccessException e) {
            final String errorMessage = String.format("Failed to parse groovy class script, nested exception is %s", e);
            throw new GroovyScriptParseException(errorMessage, e);
        }
    }

    private GroovyObject parseScriptSnippet(final String scriptText) {
        CompilerConfiguration configuration = this.groovyScriptCompiler.getConfiguration();
        GroovyShell groovyShell = new GroovyShell(configuration);
        return groovyShell.parse(scriptText);
    }

    private Object invokeMethod(GroovyObject groovyObject, String function, Object... parameters) {
        try {
            return groovyObject.invokeMethod(function, parameters);
        } catch (Exception e) {
            final String errorMessage = String.format("Failed to invoke groovy method '%s', nested exception is %s", function, e);
            throw new GroovyObjectInvokeMethodException(errorMessage, e);
        }
    }

}
