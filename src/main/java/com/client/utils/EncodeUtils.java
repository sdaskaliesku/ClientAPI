package com.client.utils;

import com.client.domain.db.ActivateRequest;
import com.client.domain.db.CryptoKey;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
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
    private static final String UNICODE_FORMAT = "UTF8";
    private static final String DESEDE_ENCRYPTION_SCHEME = "DESede";
    private Cipher cipher;
    private SecretKey key;
    private String encryptKey;

    public EncodeUtils(CryptoKey cryptoKey) {
        this(cryptoKey.getCryptoKey());
    }

    public EncodeUtils(String encryptKey) {
        try {
            this.encryptKey = encryptKey;
            String myEncryptionScheme = DESEDE_ENCRYPTION_SCHEME;
            byte[] arrayBytes = encryptKey.getBytes(UNICODE_FORMAT);
            KeySpec ks = new DESedeKeySpec(arrayBytes);
            SecretKeyFactory skf = SecretKeyFactory.getInstance(myEncryptionScheme);
            cipher = Cipher.getInstance(myEncryptionScheme);
            key = skf.generateSecret(ks);
        } catch (Exception e) {
            log.error("Error initializing encode utils", e);
        }
    }

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

    private static EncodeUtils getNewInstance(String encryptKey) {
        return new EncodeUtils(encryptKey);
    }

    private static String action(String encryptKey, String input, boolean encrypt) {
        if (StringUtils.isEmpty(input)) {
            return null;
        }
        String encoded = input;
        EncodeUtils encodeUtils = getNewInstance(encryptKey);
        for (int i = 0; i < 5; i++) {
            if (encrypt) {
                encoded = encodeUtils.encrypt(encoded);
            } else {
                encoded = encodeUtils.decrypt(encoded);
            }
        }
        System.out.println("Input string: " + input);
        System.out.println("Decoded string: " + encoded);
        return encoded;
    }

    public String encode(Object object) {
        String value = objectToString(object);
        return action(encryptKey, value, true);
    }

    public <T> T decode(String encoded, Class<T> clazz) {
        if (StringUtils.isEmpty(encoded)) {
            return null;
        }
        try {
            String decodedString = action(encryptKey, encoded, false);
            return stringToObject(decodedString, clazz);
        } catch (Exception e) {
            log.error("Error decoding", e);
        }
        return null;
    }

    private String encrypt(String unencryptedString) {
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


    private String decrypt(String encryptedString) {
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
    private static String getDefaultCharSet() {
        OutputStreamWriter writer = new OutputStreamWriter(new ByteArrayOutputStream());
        String enc = writer.getEncoding();
        return enc;
    }

    public static void main(String[] args) {
        System.out.println("Default Charset=" + Charset.defaultCharset());
        System.out.println("file.encoding=" + System.getProperty("file.encoding"));
        System.out.println("Default Charset in Use=" + getDefaultCharSet());
//        String key = "jaL-#afnPdR$THwsYKGt*ud8*wvCB2v?rbn?cKCEuyecyxwYb3pmYL59hsk_+L$P";
//        String input = "Qxn4AsRFo2M2KXQRohaHQoT+j9ML4YCnCoZzZQKNK8tv9C6hpPCW/NbkJkv4nHOEDqwhCG5l+1hvXS3dWhLoymhjVWYAkPAS6ZD1G71gR0SFSRpfQN6lu+TO1ZPebOoBdLGsm2hCKX0MyzQYJY6TQso9CMRm8LOw0eZjP6ggdlUF8ezQDSY401HB41o+9j2kkatPn3rt73YYxLyZzkiTRwaGtZrbVnD7Z9oAuF0qUo7HKTO5v1tS2V3k5goquYxo4FLkEgfTURHzPa84YtXg5+QdMiSHO14RbW5EhiUMEWJGedj36ry400xmQB62zg3JqPVgXMg9dj2s6e4foSqEvlAFZcQHbIizsWjI99ook3AOh4Rr3VlZbDAGmILXoUrm4bbi2obAkH6lMRJQhaLfCoGUVBXryCxzzkT1Rt7RsJ3w7pJZW1cAHPGy99t3kEiKNIpbTufZ+HDkzBVv2U6/S4b8eCGmqAPdidLXazvHJcwrMOo303/z9Wf6GOY67elPcGzxSFUYrcqBLTLs71uVxz0qOA8bPIXqeOJPl54K92MqGc3om8ptnOaqDSBbBtci5nNKbI/SVZvNXXwS4mrwATmBaT3dYBVTZT0yOj9mEr24a+5WVn9VtSyQUyKKqt3IUWdz8wihsemiknAQcNOqSUv03rKetgn6PJ1zuiOZgP9C4yBJdZVjUlK4pF9IPg5RzdSwJjhFi0cD3tNg1+noSadN43g/IxJacp/RdIJNzpZyBeKLzD8Po/VCqwHEm4T8K9+T3DQ6dWg2OVcLGh8d+VKM9T8HE0uzg4kM84q8aHWYbiPLKJQcfvys5+MeKPprphKZ9EuyjN84qf7LN3Rc+21kXaKzn2GNR5c+uzr0nOfzuLEbgNGVtn+OCmY2H8o7EgoaJNC7l7nuMp89HWgnKk94oywrNXJTqwaKb4yBv4/MDfpXViDo9+k1pSGgBnLBqwLv0wAPrHj6WJybPquqk3foaHcczZ9XmZWQW8mc80s=";
//        EncodeUtils encodeUtils = EncodeUtils.getNewInstance(key);
//        System.out.println(encodeUtils.decode(input, ActivateRequest.class));
    }

}
