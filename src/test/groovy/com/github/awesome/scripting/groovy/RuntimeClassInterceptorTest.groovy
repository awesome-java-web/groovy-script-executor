package com.github.awesome.scripting.groovy

import com.github.awesome.scripting.groovy.security.RuntimeClassInterceptor
import org.kohsuke.groovy.sandbox.GroovyInterceptor
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

class RuntimeClassInterceptorTest extends Specification {

    @Shared
    RuntimeClassInterceptor interceptor

    def setupSpec() {
        interceptor = new RuntimeClassInterceptor()
    }

    @Unroll
    def "test onStaticCall throw SecurityException, method = #method"() {
        when:
        interceptor.onStaticCall(null, Runtime.class, method)

        then:
        SecurityException exception = thrown(SecurityException)
        exception.message == "Runtime." + method + "() is not allowed in your groovy code."

        where:
        method << ["getRuntime", "exit", "addShutdownHook"]
    }

    @Unroll
    def "test onStaticCall success, method = #method"() {
        given:
        GroovyInterceptor.Invoker invoker = Mock(GroovyInterceptor.Invoker)

        when:
        def result = interceptor.onStaticCall(invoker, clazz, method)

        then:
        result == null

        where:
        clazz << [Long.class, Integer.class, String.class]
        method << ["parseLong", "parseInt", "valueOf"]
    }

}
