package com.github.awesome.scripting.groovy.security;

import org.kohsuke.groovy.sandbox.GroovyInterceptor;

/**
 * Groovy 脚本执行拦截器，提供沙箱环境，禁止调用 {@link Runtime} 类的方法，保证脚本执行的安全性。
 *
 * @author codeboyzhou
 * @since 0.2.0
 */
public class RuntimeClassInterceptor extends GroovyInterceptor {

    @Override
    public Object onStaticCall(Invoker invoker, Class receiver, String method, Object... args) throws Throwable {
        if (receiver == Runtime.class) {
            throw new SecurityException("Runtime." + method + "() is not allowed in your groovy code.");
        }
        return super.onStaticCall(invoker, receiver, method, args);
    }

}
