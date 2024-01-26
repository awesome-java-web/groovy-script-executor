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
        groovyScriptExecutor = GroovyScriptExecutor.newBuilder()
        guava = new GuavaLocalCache(CacheBuilder.newBuilder().build())
        caffeine = new CaffeineLocalCache(Caffeine.newBuilder().build())
    }

    @Unroll
    def "test execute empty script: #expectedMessage"() {
        given:
        localCacheManager.useDefaultCache()
        groovyScriptExecutor.withCacheManager(localCacheManager)

        when:
        groovyScriptExecutor.execute(script, _ as String, _ as String)

        then:
        Exception exception = thrown(expectedException)
        exception.message == expectedMessage || exception.message.contains(expectedMessage)

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
    def "test execute success, script = #scriptFileName, function = #function, parameters = #parameters, result = #result"() {
        given:
        localCacheManager.use(cacheFramework)
        groovyScriptExecutor.withCacheManager(localCacheManager)
        String script = new String(Files.readAllBytes(Paths.get(testScriptFilePath, scriptFileName)))

        when:
        def executeReturn = groovyScriptExecutor.execute(script, function, parameters)

        then:
        executeReturn == result
        groovyScriptExecutor.getCacheManager().stats()

        where:
        cacheFramework | scriptFileName                    | function                      | parameters | result
        guava          | "TestGroovyScriptExecutor.groovy" | "testInvokeMethodNoArgs"      | null       | 2147483647
        guava          | "TestGroovyScriptExecutor.groovy" | "testInvokeMethodWithArgs"    | 10240      | 1048576000
        caffeine       | "TestGroovyScriptExecutor.groovy" | "testInvokeMethodWithTwoArgs" | [2, 31]    | 2147483647
    }

    def "test parseScript catch InstantiationException | IllegalAccessException"() {
        given:
        localCacheManager.useDefaultCache()
        groovyScriptExecutor.withCacheManager(localCacheManager)
        final String scriptFileName = "TestInstantiationException.groovy"
        String script = new String(Files.readAllBytes(Paths.get(testScriptFilePath, scriptFileName)))

        when:
        groovyScriptExecutor.execute(script, _ as String, _ as String)

        then:
        Exception exception = thrown(GroovyScriptParseException)
        exception.message.contains("Failed to parse groovy script")
    }

}
