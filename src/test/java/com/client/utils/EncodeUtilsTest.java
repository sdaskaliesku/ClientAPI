package com.client.utils;

import org.junit.Test;

import static org.junit.Assert.assertEquals;


/**
 * @author sdaskaliesku
 */
public class EncodeUtilsTest {

    @Test
    public void test() {
        String in = "антон тоже";
        EncodeUtils encodeUtils = new EncodeUtils();
        String outEnc = encodeUtils.encrypt(in);
        String outDec = encodeUtils.decrypt(outEnc);
        System.out.println(in);
        System.out.println(outEnc);
        System.out.println(outDec);
        assertEquals(in, outDec);
    }

}