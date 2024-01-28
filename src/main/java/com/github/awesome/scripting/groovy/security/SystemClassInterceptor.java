package com.github.awesome.scripting.groovy.security;

import org.kohsuke.groovy.sandbox.GroovyInterceptor;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class SystemClassInterceptor extends GroovyInterceptor {

    private static final List<String> NOT_ALLOWED_METHODS = Collections.unmodifiableList(
            Arrays.asList("gc", "exit", "runFinalization")
    );

    @Override
    public Object onStaticCall(Invoker invoker, Class receiver, String method, Object... args) throws Throwable {
        if (receiver == System.class && NOT_ALLOWED_METHODS.contains(method)) {
            throw new SecurityException("System." + method + "() is not allowed in your groovy code.");
        }
        return super.onStaticCall(invoker, receiver, method, args);
    }

}
