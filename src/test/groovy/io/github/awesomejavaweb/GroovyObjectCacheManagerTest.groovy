package io.github.awesomejavaweb

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

}
