package com.basicarch.base.utils;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

public class CryptoUtils {
    private static final String ALLOWED_CHARACTERS;
    private static final int DEFAULT_LENGTH = 8;
    private static final SecureRandom secureRandom = new SecureRandom();

    static {
        StringBuilder sb = new StringBuilder();
        // 0 ~ 9
        for (char c = 48; c <= 57; c++) {
            sb.append(c);
        }
        // A ~ Z
        for (char c = 65; c <= 90; c++) {
            sb.append(c);
        }
        // a ~ z
        /*for (char c = 97; c <= 122; c++) {
            sb.append(c);
        }*/
        ALLOWED_CHARACTERS = sb.toString();
    }

    public static String generateRandomString() {
        return generateRandomString(DEFAULT_LENGTH);
    }

    public static String generateRandomString(int length) {
        if (length <= 0) length = DEFAULT_LENGTH;

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int randomIndex = secureRandom.nextInt(ALLOWED_CHARACTERS.length());
            sb.append(ALLOWED_CHARACTERS.charAt(randomIndex));
        }

        return sb.toString();
    }


    /**
     * SecretKey 생성 메서드(256)
     *
     * @return Base64로 인코딩된 SecretKey
     * @throws NoSuchAlgorithmException
     */
    public static String createSecretKey() throws NoSuchAlgorithmException {
        return createSecretKey(256);
    }

    /**
     * SecretKey 생성 메서드
     *
     * @param keySize keytGenerator 키사이즈
     * @return Base64로 인코딩된 SecretKey
     * @throws NoSuchAlgorithmException
     */
    public static String createSecretKey(int keySize) throws NoSuchAlgorithmException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("HmacSHA256");
        keyGenerator.init(keySize);
        SecretKey secretKey = keyGenerator.generateKey();
        return Base64.getEncoder().encodeToString(secretKey.getEncoded());
    }
}
