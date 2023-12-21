package com.github.awesome.scripting.groovy

import com.github.awesome.scripting.groovy.security.DefaultShouldInterceptKeywords
import spock.lang.Specification
import spock.lang.Unroll

class DefaultShouldInterceptKeywordsTest extends Specification {

    @Unroll
    def "test fromKeyword, input = #keyword, result = #expectedResult"() {
        when:
        DefaultShouldInterceptKeywords result = DefaultShouldInterceptKeywords.fromKeyword(keyword)

        then:
        result == null || result.explanation == expectedResult

        where:
        keyword       | expectedResult
        "test1"       | null
        "test2"       | null
        "test3"       | null
        "System.gc()" | DefaultShouldInterceptKeywords.SYSTEM_GC.explanation
    }

}
