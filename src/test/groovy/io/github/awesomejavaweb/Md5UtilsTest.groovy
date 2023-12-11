package io.github.awesomejavaweb

import io.github.awesomejavaweb.util.Md5Utils
import spock.lang.Specification
import spock.lang.Unroll

class Md5UtilsTest extends Specification {

    @Unroll
    def "test md5Hex, input = #input, result = #result"() {
        given:
        String md5Hex = Md5Utils.md5Hex(input)

        expect:
        md5Hex == result

        where:
        input         | result
        "hello world" | "5eb63bbbe01eeed093cb22bb8f5acdc3"
        "12345678910" | "432f45b44c432414d2f97df0e5743818"
        "test md5Hex" | "23d03bcab51446d6b9a50aaf26ebe666"
    }

}
