package io.github.awesomejavaweb

import com.github.benmanes.caffeine.cache.Caffeine
import io.github.awesomejavaweb.core.GroovyObjectCacheManager
import spock.lang.Specification

class GroovyObjectCacheManagerTest extends Specification {

    def "test new constructor"() {
        when:
        new GroovyObjectCacheManager()

        then:
        Exception exception = thrown(UnsupportedOperationException)
        exception instanceof UnsupportedOperationException && exception.message == "Utility class should not be instantiated"
    }

    def "test useCustomCacheBuilder"() {
        given:
        Caffeine<String, GroovyObject> cacheBuilder = Caffeine.newBuilder() as Caffeine<String, GroovyObject>

        when:
        GroovyObjectCacheManager.useCustomCacheBuilder(cacheBuilder)

        then:
        GroovyObjectCacheManager.getCustomCacheBuilder().hashCode() == cacheBuilder.hashCode()
    }

}
