package com.github.awesome.scripting.groovy.exception;

import com.github.awesome.scripting.groovy.GroovyScriptExecutor;

/**
 * 当 Groovy 脚本解析异常时可能会抛出的异常。
 *
 * @author codeboyzhou
 * @see GroovyScriptExecutor#execute(String, String, Object...)
 * @see GroovyScriptExecutor#evaluate(String, String, Object...)
 * @since 0.1.0
 */
public class GroovyScriptParseException extends RuntimeException {

    public GroovyScriptParseException(String message, Throwable cause) {
        super(message, cause);
    }

}
