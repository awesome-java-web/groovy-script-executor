package com.github.awesome.scripting.groovy

import com.github.awesome.scripting.groovy.cache.CaffeineLocalCache
import com.github.awesome.scripting.groovy.cache.GuavaLocalCache
import com.github.awesome.scripting.groovy.cache.LocalCacheManager
import com.github.awesome.scripting.groovy.exception.GroovyObjectInvokeMethodException
import com.github.awesome.scripting.groovy.exception.GroovyScriptParseException
import com.github.awesome.scripting.groovy.exception.InvalidGroovyScriptException
import com.github.benmanes.caffeine.cache.Caffeine
import com.google.common.cache.CacheBuilder
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

import java.nio.file.Files
import java.nio.file.Paths

class GroovyScriptExecutorTest extends Specification {

    @Shared
    GuavaLocalCache guava

    @Shared
    CaffeineLocalCache caffeine

    @Shared
    LocalCacheManager localCacheManager

    @Shared
    GroovyScriptExecutor groovyScriptExecutor

    String testScriptFilePath = "src/test/resources"

    def setupSpec() {
        localCacheManager = LocalCacheManager.newBuilder()
        guava = new GuavaLocalCache(CacheBuilder.newBuilder().recordStats().build())
        caffeine = new CaffeineLocalCache(Caffeine.newBuilder().recordStats().build())
        groovyScriptExecutor = GroovyScriptExecutor.newBuilder().with(GroovyScriptCompiler.asDefault())
    }

    @Unroll
    def "test execute/evaluate empty or error script: #expectedMessage"() {
        given:
        localCacheManager.useDefaultCache()
        groovyScriptExecutor.with(localCacheManager)

        when:
        groovyScriptExecutor.execute(script, _ as String, _ as String)

        then:
        Exception executeException = thrown(expectedException)
        executeException.message == expectedMessage || executeException.message.contains(expectedMessage)
        System.err.println(executeException.message)

        when:
        groovyScriptExecutor.evaluate(script, _ as String, _ as String)

        then:
        Exception evaluateException = thrown(expectedException)
        evaluateException.message == expectedMessage || evaluateException.message.contains(expectedMessage)
        System.err.println(evaluateException.message)

        where:
        script                    | expectedException                 | expectedMessage
        null                      | InvalidGroovyScriptException      | "Groovy script is null or empty"
        Strings.EMPTY             | InvalidGroovyScriptException      | "Groovy script is null or empty"
        Strings.SPACE             | InvalidGroovyScriptException      | "Groovy script is null or empty"
        Strings.TAB               | InvalidGroovyScriptException      | "Groovy script is null or empty"
        Strings.LF                | InvalidGroovyScriptException      | "Groovy script is null or empty"
        Strings.CRLF              | InvalidGroovyScriptException      | "Groovy script is null or empty"
        "This is not groovy code" | GroovyObjectInvokeMethodException | "Failed to invoke groovy method"
    }

    @Unroll
    def "test execute success, script = #scriptFileName, function = #function, parameters = #parameters, result = #expected"() {
        given:
        localCacheManager.use(cacheFramework)
        groovyScriptExecutor.with(localCacheManager)
        String script = new String(Files.readAllBytes(Paths.get(testScriptFilePath, scriptFileName)))

        when:
        def result = groovyScriptExecutor.execute(script, function, parameters)

        then:
        result == expected
        String stats = groovyScriptExecutor.getLocalCacheManager().stats()
        println(stats)

        where:
        cacheFramework | scriptFileName                    | function                      | parameters   | expected
        guava          | "TestGroovyScriptExecutor.groovy" | "testInvokeMethodNoArgs"      | null         | 1024
        guava          | "TestGroovyScriptExecutor.groovy" | "testInvokeMethodWithArgs"    | 1024         | 1025
        caffeine       | "TestGroovyScriptExecutor.groovy" | "testInvokeMethodWithTwoArgs" | [1024, 1024] | 2048
    }

    @Unroll
    def "test evaluate, script = #scriptText, function = #function, parameters = #parameters, result = #expected"() {
        given:
        localCacheManager.useDefaultCache()
        groovyScriptExecutor.with(localCacheManager)

        when:
        def result = groovyScriptExecutor.evaluate(scriptText, function, parameters)

        then:
        result == expected

        where:
        scriptText                                                   | function                  | parameters   | expected
        "def testEvaluateNoArgs() { return 1024 }"                   | "testEvaluateNoArgs"      | null         | 1024
        "def testEvaluateWithArgs(int a) { return a + 1 }"           | "testEvaluateWithArgs"    | 1024         | 1025
        "def testEvaluateWithTwoArgs(int a, int b) { return a + b }" | "testEvaluateWithTwoArgs" | [1024, 1024] | 2048
    }

    def "test parseClassScript catch InstantiationException | IllegalAccessException"() {
        given:
        localCacheManager.useDefaultCache()
        groovyScriptExecutor.with(localCacheManager)
        final String scriptFileName = "TestInstantiationException.groovy"
        String script = new String(Files.readAllBytes(Paths.get(testScriptFilePath, scriptFileName)))

        when:
        groovyScriptExecutor.execute(script, _ as String, _ as String)

        then:
        Exception exception = thrown(GroovyScriptParseException)
        exception.message.contains("Failed to parse groovy class script")
        System.err.println(exception.message)
    }

    @Unroll
    def "test sandbox interceptor, method = #method"() {
        given:
        localCacheManager.useDefaultCache()
        groovyScriptExecutor.with(localCacheManager)
        final String scriptFileName = "TestSandboxInterceptor.groovy"
        String script = new String(Files.readAllBytes(Paths.get(testScriptFilePath, scriptFileName)))

        when:
        groovyScriptExecutor.execute(script, method, null)

        then:
        Exception exception = thrown(GroovyObjectInvokeMethodException)
        exception.message.contains("Failed to invoke groovy method")
        exception.message.contains("not allowed in your groovy code")
        System.err.println(exception.message)

        where:
        method << ["testSystemGC", "testSystemExit", "testSystemRunFinalization", "testRuntimeClass"]
    }

}
