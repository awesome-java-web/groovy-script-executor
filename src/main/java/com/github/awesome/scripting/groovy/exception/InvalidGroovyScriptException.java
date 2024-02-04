package com.github.awesome.scripting.groovy.exception;

import com.github.awesome.scripting.groovy.GroovyScriptExecutor;

/**
 * 当 Groovy 脚本无效时抛出的异常，比如脚本内容为 {@code null}，为空字符串。
 *
 * @author codeboyzhou
 * @see GroovyScriptExecutor#execute(String, String, Object...)
 * @see GroovyScriptExecutor#evaluate(String, String, Object...)
 * @since 0.1.0
 */
public class InvalidGroovyScriptException extends RuntimeException {

    public InvalidGroovyScriptException(String message) {
        super(message);
    }

}
