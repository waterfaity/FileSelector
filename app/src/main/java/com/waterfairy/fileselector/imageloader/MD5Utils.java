package com.waterfairy.fileselector.imageloader;

import java.security.MessageDigest;

/**
 * Created by water_fairy on 2017/5/8.
 * 995637517@qq.com
 */

public class MD5Utils {


    public static String getMD5Code(String s) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] bytes = md.digest(s.getBytes("utf-8"));
            return toHex(bytes);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static String toHex(byte[] bytes) {

        final char[] HEX_DIGITS = "0123456789abcdef".toCharArray();
        StringBuilder ret = new StringBuilder(bytes.length * 2);
        for (int i = 0; i < bytes.length; i++) {
            ret.append(HEX_DIGITS[(bytes[i] >> 4) & 0x0f]);
            ret.append(HEX_DIGITS[bytes[i] & 0x0f]);
        }
        return ret.toString();
    }

    public static void main(String[] args) {
        String s = "123456";
        String md5Code = getMD5Code(s);
        String s1 = toHex(s.getBytes());
        System.out.println(md5Code);
        System.out.println(s1);
    }
}
