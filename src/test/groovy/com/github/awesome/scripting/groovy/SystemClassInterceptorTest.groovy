package com.github.awesome.scripting.groovy

import com.github.awesome.scripting.groovy.security.SystemClassInterceptor
import org.kohsuke.groovy.sandbox.GroovyInterceptor
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

class SystemClassInterceptorTest extends Specification {

    @Shared
    SystemClassInterceptor interceptor

    def setupSpec() {
        interceptor = new SystemClassInterceptor()
    }

    @Unroll
    def "test onStaticCall throw SecurityException, method = #method"() {
        when:
        interceptor.onStaticCall(null, System.class, method)

        then:
        SecurityException exception = thrown(SecurityException)
        exception.message == "System." + method + "() is not allowed in your groovy code."
        System.err.println(exception.message)

        where:
        method << ["gc", "exit", "runFinalization"]
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
        clazz << [System.class, Long.class]
        method << ["nanoTime", "currentTimeMillis"]
    }

}
