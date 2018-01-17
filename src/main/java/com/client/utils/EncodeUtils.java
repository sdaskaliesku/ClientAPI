package com.client.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import java.security.spec.KeySpec;
import java.util.Base64;

/**
 * @author sdaskaliesku
 */
public class EncodeUtils {


    protected static final Logger log = LoggerFactory.getLogger(EncodeUtils.class);

    private static final ObjectMapper JSON_MAPPER = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .setSerializationInclusion(JsonInclude.Include.NON_EMPTY);

    private static final EncodeUtils INSTANCE = new EncodeUtils();

    private static String objectToString(Object o) {
        try {
            return JSON_MAPPER.writeValueAsString(o);
        } catch (Exception e) {
            log.error("Error serializing object {}", o, e);
        }
        return "";
    }

    private static <T> T stringToObject(String in, Class<T> clazz) {
        try {
            return JSON_MAPPER.readValue(in, clazz);
        } catch (Exception e) {
            log.error("Error deserializing object {}", in, e);
        }
        return null;
    }

    private static String action(String input, boolean encrypt) {
        String encoded = input;
        for (int i = 0; i < 5; i++) {
            if (encrypt) {
                encoded = INSTANCE.encrypt(encoded);
            } else {
                encoded = INSTANCE.decrypt(encoded);
            }
        }
        return encoded;
    }

    public static String encode(Object object) {
        String value = objectToString(object);
        return action(value, true);
    }

    public static <T> T decode(String encoded, Class<T> clazz) {
        try {
            return stringToObject(action(encoded, false), clazz);
        } catch (Exception e) {
            log.error("Error decoding", e);
        }
        return null;
    }

    private static final String UNICODE_FORMAT = "UTF8";
    private static final String DESEDE_ENCRYPTION_SCHEME = "DESede";
    private Cipher cipher;
    private SecretKey key;

    public EncodeUtils() {
        try {
            String myEncryptionKey = "@^-^xn#zXgB42wryP7=kCnrm28GpUT9z$_M*HHX=E?+u-FK4+TfadTgvp-B%wXWT";
            String myEncryptionScheme = DESEDE_ENCRYPTION_SCHEME;
            byte[] arrayBytes = myEncryptionKey.getBytes(UNICODE_FORMAT);
            KeySpec ks = new DESedeKeySpec(arrayBytes);
            SecretKeyFactory skf = SecretKeyFactory.getInstance(myEncryptionScheme);
            cipher = Cipher.getInstance(myEncryptionScheme);
            key = skf.generateSecret(ks);
        } catch (Exception e) {
            log.error("Error initializing encode utils", e);
        }
    }


    public String encrypt(String unencryptedString) {
        String encryptedString = null;
        try {
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] plainText = unencryptedString.getBytes(UNICODE_FORMAT);
            byte[] encryptedText = cipher.doFinal(plainText);
            encryptedString = new String(Base64.getEncoder().encode(encryptedText));
        } catch (Exception e) {
            log.error("Error encrypting", e);
        }
        return encryptedString;
    }


    public String decrypt(String encryptedString) {
        String decryptedText = null;
        try {
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] encryptedText = Base64.getDecoder().decode(encryptedString);
            byte[] plainText = cipher.doFinal(encryptedText);
            decryptedText = new String(plainText);
        } catch (Exception e) {
            log.error("Error decrypting", e);
        }
        return decryptedText;
    }

}
