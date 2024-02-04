package com.github.awesome.scripting.groovy.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 消息摘要计算工具类，内部实现，减少一些外部依赖。
 *
 * @author codeboyzhou
 * @since 0.1.0
 */
public final class MessageDigestUtils {

    private static final String MESSAGE_DIGEST_ALGORITHM_MD5 = "MD5";

    private MessageDigestUtils() {
        throw new UnsupportedOperationException("Utility class should not be instantiated");
    }

    public static byte[] md5(String input) {
        try {
            MessageDigest md5 = MessageDigest.getInstance(MESSAGE_DIGEST_ALGORITHM_MD5);
            return md5.digest(input.getBytes(StandardCharsets.UTF_8));
        } catch (NoSuchAlgorithmException e) {
            return new byte[0];
        }
    }

    public static String md5Hex(String input) {
        StringBuilder sb = new StringBuilder();
        byte[] digest = md5(input);
        for (byte b : digest) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

}
