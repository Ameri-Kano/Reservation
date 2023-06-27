package com.amerikano.reservation.encryption.util;

import org.apache.tomcat.util.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

/**
 * 암호화/복호화를 위한 Util 클래스(AES-256)
 */
public class CryptoUtil {

    // constant (secretKey, initializeVector, algorithm)
    private static final String SECRET_KEY = "SecretKey_Must_Be_32byte_String!";
    private static final String IV = SECRET_KEY.substring(0, 16);
    public static String alg = "AES/CBC/PKCS5Padding";

    /**
     * 암호화 수행
     */
    public static String encrypt(String text) {
        try {
            Cipher cipher = Cipher.getInstance(alg);
            SecretKeySpec keySpec = new SecretKeySpec(SECRET_KEY.getBytes(), "AES");
            IvParameterSpec ivParameterSpec = new IvParameterSpec(
                IV.getBytes(StandardCharsets.UTF_8)
            );
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivParameterSpec);

            byte[] encrypted = cipher.doFinal(text.getBytes(StandardCharsets.UTF_8));

            return Base64.encodeBase64String(encrypted);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 복호화 수행
     */
    public static String decrypt(String encryptedText) {
        try {
            Cipher cipher = Cipher.getInstance(alg);
            SecretKeySpec keySpec = new SecretKeySpec(SECRET_KEY.getBytes(), "AES");
            IvParameterSpec ivParameterSpec = new IvParameterSpec(
                IV.getBytes(StandardCharsets.UTF_8)
            );
            cipher.init(Cipher.DECRYPT_MODE, keySpec, ivParameterSpec);

            byte[] decodedBytes = Base64.decodeBase64(encryptedText);
            byte[] decrypted = cipher.doFinal(decodedBytes);

            return new String(decrypted, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
