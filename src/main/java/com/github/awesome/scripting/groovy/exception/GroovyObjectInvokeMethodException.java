package com.github.awesome.scripting.groovy.exception;

import com.github.awesome.scripting.groovy.GroovyScriptExecutor;
import groovy.lang.GroovyObject;

/**
 * 当使用 {@link GroovyObject#invokeMethod(String, Object)} 方法时可能会抛出的异常。
 *
 * @author codeboyzhou
 * @see GroovyScriptExecutor#execute(String, String, Object...)
 * @see GroovyScriptExecutor#evaluate(String, String, Object...)
 * @since 0.1.0
 */
public class GroovyObjectInvokeMethodException extends RuntimeException {

    public GroovyObjectInvokeMethodException(String message, Throwable cause) {
        super(message, cause);
    }

}
