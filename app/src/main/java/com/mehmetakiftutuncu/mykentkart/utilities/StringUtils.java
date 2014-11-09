package com.mehmetakiftutuncu.mykentkart.utilities;

import java.security.MessageDigest;
import java.util.Arrays;

public class StringUtils {
    public static boolean isEmpty(String s) {
        return s == null || s.isEmpty();
    }

    public static String generateNfcId(byte[] input) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            byte[] resultBytes = md.digest(input);
            StringBuilder sb = new StringBuilder();
            for (byte b : resultBytes) {
                sb.append(Integer.toString((b & 0xFF) + 0x100, 16).substring(1));
            }
            return sb.toString().toUpperCase();
        } catch (Exception e) {
            Log.error(StringUtils.class, "Failed to generate nfc id! input: " + Arrays.toString(input), e);
            return null;
        }
    }
}
