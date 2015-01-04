/*
 * Copyright (C) 2015 Mehmet Akif Tütüncü
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.mehmetakiftutuncu.mykentkart.utilities;

import java.security.MessageDigest;
import java.util.Arrays;

/**
 * A utility class for basic String operations
 *
 * @author mehmetakiftutuncu
 */
public class StringUtils {
    /**
     * Checks whether or not given String is empty
     *
     * @param s String to check
     *
     * @return true if given String is not null and not empty or false otherwise
     */
    public static boolean isEmpty(String s) {
        return s == null || s.isEmpty();
    }

    /**
     * Generates an NFC id represented as String from identifying bytes of KentKart read via NFC,
     * basically takes a SHA-1 hash of the bytes and converts it to a String
     *
     * @param input Identifying bytes of KentKart read via NFC
     *
     * @return Generated NFC id of KentKart represented as a String or null if any error occurs
     */
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
