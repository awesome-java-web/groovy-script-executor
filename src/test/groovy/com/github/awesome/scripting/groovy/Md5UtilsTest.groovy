package com.github.awesome.scripting.groovy

import com.github.awesome.scripting.groovy.util.Md5Utils
import org.mockito.MockedStatic
import org.mockito.Mockito
import spock.lang.Specification
import spock.lang.Unroll

import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

class Md5UtilsTest extends Specification {

    def "test new constructor"() {
        when:
        new Md5Utils()

        then:
        Exception exception = thrown(UnsupportedOperationException)
        exception instanceof UnsupportedOperationException && exception.message == "Utility class should not be instantiated"
        System.err.println(exception.message)
    }

    def "test md5 catch NoSuchAlgorithmException"() {
        given:
        MockedStatic<MessageDigest> mdMock = Mockito.mockStatic(MessageDigest.class)
        mdMock.when(() -> MessageDigest.getInstance(Mockito.anyString())).thenThrow(NoSuchAlgorithmException)

        when:
        byte[] md5 = Md5Utils.md5("hello world")

        then:
        md5.length == 0
        mdMock.close()
    }

    @Unroll
    def "test md5Hex, input = #input, result = #result"() {
        when:
        String md5Hex = Md5Utils.md5Hex(input)

        then:
        md5Hex == result

        where:
        input         | result
        "hello world" | "5eb63bbbe01eeed093cb22bb8f5acdc3"
        "12345678910" | "432f45b44c432414d2f97df0e5743818"
        "test md5Hex" | "23d03bcab51446d6b9a50aaf26ebe666"
    }

}
