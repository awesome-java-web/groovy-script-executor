package io.github.awesomejavaweb

import io.github.awesomejavaweb.common.Strings
import io.github.awesomejavaweb.core.GroovyScriptExecutor
import io.github.awesomejavaweb.exception.InvalidGroovyScriptException
import spock.lang.Specification
import spock.lang.Unroll

import java.nio.file.Files
import java.nio.file.Paths

class GroovyScriptExecutorTest extends Specification {

    String testScriptFilePath = "src/test/resources"

    @Unroll
    def "test execute empty script: #expectedMessage"() {
        given:
        GroovyScriptExecutor groovyScriptExecutor = new GroovyScriptExecutor()

        when:
        groovyScriptExecutor.execute(script, Strings.EMPTY, Strings.EMPTY)

        then:
        Exception exception = thrown(expectedException)
        exception instanceof InvalidGroovyScriptException && exception.message == expectedMessage

        where:
        script        | expectedException            | expectedMessage
        null          | InvalidGroovyScriptException | "Groovy script is null or empty"
        Strings.EMPTY | InvalidGroovyScriptException | "Groovy script is null or empty"
        Strings.SPACE | InvalidGroovyScriptException | "Groovy script is null or empty"
        Strings.TAB   | InvalidGroovyScriptException | "Groovy script is null or empty"
        Strings.LF    | InvalidGroovyScriptException | "Groovy script is null or empty"
        Strings.CRLF  | InvalidGroovyScriptException | "Groovy script is null or empty"
    }

    @Unroll
    def "test execute success, script = #scriptFileName, function = #function, parameters = #parameters, result = #result"() {
        given:
        GroovyScriptExecutor groovyScriptExecutor = new GroovyScriptExecutor()
        String script = new String(Files.readAllBytes(Paths.get(testScriptFilePath, scriptFileName)))
        def executeReturn = groovyScriptExecutor.execute(script, function, parameters)

        expect:
        executeReturn == result

        where:
        scriptFileName                    | function                      | parameters | result
        "TestGroovyScriptExecutor.groovy" | "testInvokeMethodNoArgs"      | null       | 2147483647
        "TestGroovyScriptExecutor.groovy" | "testInvokeMethodWithArgs"    | 10240      | 1048576000
        "TestGroovyScriptExecutor.groovy" | "testInvokeMethodWithTwoArgs" | [2, 31]    | 2147483647
    }

}
