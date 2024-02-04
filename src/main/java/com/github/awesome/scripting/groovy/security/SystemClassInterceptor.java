package com.github.awesome.scripting.groovy.security;

import org.kohsuke.groovy.sandbox.GroovyInterceptor;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Groovy 脚本执行拦截器，提供沙箱环境，禁止调用 {@link System} 类的一些方法，保证脚本执行的安全性。
 *
 * @author codeboyzhou
 * @since 0.2.0
 */
public class SystemClassInterceptor extends GroovyInterceptor {

    /**
     * 不被允许调用的方法列表
     */
    private static final List<String> NOT_ALLOWED_METHODS = Collections
            .unmodifiableList(Arrays.asList("gc", "exit", "runFinalization"));

    @Override
    public Object onStaticCall(Invoker invoker, Class receiver, String method, Object... args) throws Throwable {
        if (receiver == System.class && NOT_ALLOWED_METHODS.contains(method)) {
            throw new SecurityException("System." + method + "() is not allowed in your groovy code.");
        }
        return super.onStaticCall(invoker, receiver, method, args);
    }

}
