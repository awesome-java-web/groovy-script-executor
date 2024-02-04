package com.github.awesome.scripting.groovy;

import com.github.awesome.scripting.groovy.cache.LocalCacheManager;
import com.github.awesome.scripting.groovy.exception.GroovyObjectInvokeMethodException;
import com.github.awesome.scripting.groovy.exception.GroovyScriptParseException;
import com.github.awesome.scripting.groovy.exception.InvalidGroovyScriptException;
import com.github.awesome.scripting.groovy.util.MessageDigestUtils;
import groovy.lang.GroovyClassLoader;
import groovy.lang.GroovyObject;
import groovy.lang.GroovyShell;
import org.codehaus.groovy.control.CompilerConfiguration;

import javax.annotation.Nullable;
import java.io.IOException;

/**
 * Groovy 脚本执行器，对外部开放的核心类以及方法都在这个类中，主要负责解析和执行 Groovy 脚本，并返回对应结果。
 *
 * @author codeboyzhou
 * @since 0.1.0
 */
public class GroovyScriptExecutor {

    /**
     * 自定义的 Groovy 脚本编译器
     */
    private GroovyScriptCompiler groovyScriptCompiler;

    /**
     * 本地缓存管理器
     */
    private LocalCacheManager localCacheManager;

    /**
     * 创建一个默认的 {@link GroovyScriptExecutor} 对象。
     *
     * @return 当前类的实例对象
     */
    public static GroovyScriptExecutor newBuilder() {
        return new GroovyScriptExecutor();
    }

    /**
     * 默认构造方法，初始化内置的默认配置，包括默认编译器配置、默认缓存配置等。
     */
    public GroovyScriptExecutor() {
        this.groovyScriptCompiler = GroovyScriptCompiler.asDefault();
        this.localCacheManager = LocalCacheManager.newBuilder().useDefaultCache();
    }

    /**
     * 设置自定义的 {@link GroovyScriptCompiler} 对象。
     *
     * @param groovyScriptCompiler 自定义的 Groovy 脚本编译器
     * @return 当前类的实例对象，返回 {@code this} 方便链式调用
     * @since 0.2.0
     */
    public GroovyScriptExecutor with(GroovyScriptCompiler groovyScriptCompiler) {
        this.groovyScriptCompiler = groovyScriptCompiler;
        return this;
    }

    /**
     * 设置自定义的 {@link LocalCacheManager} 对象。
     *
     * @param localCacheManager 自定义的本地缓存管理器
     * @return 当前类的实例对象，返回 {@code this} 方便链式调用
     * @since 0.2.0
     */
    public GroovyScriptExecutor with(LocalCacheManager localCacheManager) {
        this.localCacheManager = localCacheManager;
        return this;
    }

    /**
     * 返回本地缓存管理器对象，方便从外部进行缓存框架扩展
     *
     * @return 本地缓存管理器对象
     * @since 0.2.0
     */
    public LocalCacheManager getLocalCacheManager() {
        return localCacheManager;
    }

    /**
     * 执行 Groovy 脚本，返回执行结果，这个方法用于执行 {@code class} 形式的脚本。
     *
     * @param classScript Groovy Class 脚本内容
     * @param function    想要在脚本中执行的函数名称
     * @param parameters  对应函数的参数
     * @return 脚本执行结果
     * @since 0.1.0
     */
    public Object execute(final String classScript, final String function, final Object... parameters) {
        // Parse the script and put it into cache instantly if it is not in cache
        GroovyObject groovyObjectCache = this.getGroovyObjectCacheFromScript(classScript);
        if (groovyObjectCache == null) {
            final String trimmedScript = classScript.trim();
            final String scriptCacheKey = MessageDigestUtils.md5Hex(trimmedScript);
            groovyObjectCache = parseClassScript(trimmedScript);
            this.localCacheManager.put(scriptCacheKey, groovyObjectCache);
        }

        // Script is parsed successfully
        return invokeMethod(groovyObjectCache, function, parameters);
    }

    /**
     * 执行 Groovy 脚本，返回执行结果，这个方法用于执行非 {@code class} 形式的脚本，比如只是单纯的 Groovy 函数片段。
     *
     * @param scriptText Groovy 脚本片段
     * @param function   想要在脚本中执行的函数名称
     * @param parameters 对应函数的参数
     * @return 脚本执行结果
     * @since 0.2.0
     */
    public Object evaluate(final String scriptText, final String function, final Object... parameters) {
        // Parse the script and put it into cache instantly if it is not in cache
        GroovyObject groovyObjectCache = this.getGroovyObjectCacheFromScript(scriptText);
        if (groovyObjectCache == null) {
            final String trimmedScript = scriptText.trim();
            final String scriptCacheKey = MessageDigestUtils.md5Hex(trimmedScript);
            groovyObjectCache = parseScriptSnippet(trimmedScript);
            this.localCacheManager.put(scriptCacheKey, groovyObjectCache);
        }

        // Script is parsed successfully
        return invokeMethod(groovyObjectCache, function, parameters);
    }

    /**
     * 从缓存中获取 {@link GroovyObject} 对象，如果缓存中不存在，则解析脚本内容并重新放入缓存。
     *
     * @param scriptText 脚本内容
     * @return {@link GroovyObject} 对象
     */
    @Nullable
    private GroovyObject getGroovyObjectCacheFromScript(final String scriptText) {
        if (scriptText == null) {
            throw new InvalidGroovyScriptException("Groovy script is null");
        }

        final String trimmedScript = scriptText.trim();
        if (trimmedScript.isEmpty()) {
            throw new InvalidGroovyScriptException("Groovy script is empty");
        }

        final String scriptCacheKey = MessageDigestUtils.md5Hex(trimmedScript);
        return this.localCacheManager.getIfPresent(scriptCacheKey);
    }

    /**
     * 解析 Groovy Class 脚本内容，返回 {@link GroovyObject} 对象。
     *
     * @param classScript Groovy Class 脚本内容
     * @return {@link GroovyObject} 对象
     */
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

    /**
     * 解析非 {@code class} 形式的 Groovy 脚本片段，返回 {@link GroovyObject} 对象。
     *
     * @param scriptText Groovy 脚本片段
     * @return {@link GroovyObject} 对象
     */
    private GroovyObject parseScriptSnippet(final String scriptText) {
        CompilerConfiguration configuration = this.groovyScriptCompiler.getConfiguration();
        GroovyShell groovyShell = new GroovyShell(configuration);
        return groovyShell.parse(scriptText);
    }

    /**
     * 调用 Groovy 脚本中的函数，返回执行结果。
     *
     * @param groovyObject Groovy 脚本对象
     * @param function     想要在脚本中执行的函数名称
     * @param parameters   对应函数的参数
     * @return 脚本执行结果
     */
    private Object invokeMethod(GroovyObject groovyObject, String function, Object... parameters) {
        try {
            return groovyObject.invokeMethod(function, parameters);
        } catch (Exception e) {
            final String errorMessage = String.format("Failed to invoke groovy method '%s', nested exception is %s", function, e);
            throw new GroovyObjectInvokeMethodException(errorMessage, e);
        }
    }

}
