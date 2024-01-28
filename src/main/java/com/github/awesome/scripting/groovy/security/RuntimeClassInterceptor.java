package com.github.awesome.scripting.groovy.security;

import org.kohsuke.groovy.sandbox.GroovyInterceptor;

public class RuntimeClassInterceptor extends GroovyInterceptor {

    @Override
    public Object onStaticCall(Invoker invoker, Class receiver, String method, Object... args) throws Throwable {
        if (receiver == Runtime.class) {
            throw new SecurityException("Runtime." + method + "() is not allowed in your groovy code.");
        }
        return super.onStaticCall(invoker, receiver, method, args);
    }

}
