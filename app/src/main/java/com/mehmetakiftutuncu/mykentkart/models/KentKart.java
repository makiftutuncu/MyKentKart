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
package com.mehmetakiftutuncu.mykentkart.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.Gson;
import com.mehmetakiftutuncu.mykentkart.utilities.Log;
import com.mehmetakiftutuncu.mykentkart.utilities.StringUtils;

/**
 * A model of a KentKart, it implements {@link android.os.Parcelable}
 * so it can be moved around in a {@link android.os.Bundle}
 *
 * @author mehmetakiftutuncu
 */
public class KentKart implements Parcelable {
    /** Name of the KentKart */
    public String name;
    /** Number of the KentKart, must contain only 11 digits to be valid */
    public String number;
    /** NFC id of the KentKart */
    public String nfcId;
    /** Region code of the KentKart, must be code value of one of the values in {@link com.mehmetakiftutuncu.mykentkart.models.Regions} */
    public String regionCode;

    /**
     * Constructor initializing all values
     *
     * @param name       Value to set as {@link com.mehmetakiftutuncu.mykentkart.models.KentKart#name}
     * @param number     Value to set as {@link com.mehmetakiftutuncu.mykentkart.models.KentKart#number}
     * @param nfcId      Value to set as {@link com.mehmetakiftutuncu.mykentkart.models.KentKart#nfcId}
     * @param regionCode Value to set as {@link com.mehmetakiftutuncu.mykentkart.models.KentKart#regionCode}
     */
    public KentKart(String name, String number, String nfcId, String regionCode) {
        this.name = name;
        this.number = number;
        this.nfcId = nfcId;
        this.regionCode = regionCode;
    }

    /**
     * Generates a formatted String representation of {@link com.mehmetakiftutuncu.mykentkart.models.KentKart#number}
     *
     * @return A formatted String representation of {@link com.mehmetakiftutuncu.mykentkart.models.KentKart#number}
     *         or empty String if given number is empty
     */
    public String getFormattedNumber() {
        return KentKart.getFormattedNumber(number);
    }

    /**
     * Generates a formatted String representation of given KentKart number, example: 12345-67890-1
     *
     * @param number Number of KentKart from which to generate formatted value
     *
     * @return A formatted String representation of given KentKart number or empty String if given number is empty
     */
    public static String getFormattedNumber(String number) {
        if (StringUtils.isEmpty(number)) {
            return "";
        } else {
            String newNumberText = number;

            int length = number.length();
            if (length >= 6 && length < 11) {
                // After first 5 digits
                String first5 = number.substring(0, 5);
                String rest = number.substring(5, length);

                newNumberText = first5 + "-" + rest;
            } else if (length >= 11) {
                // After first 10 digits
                String first5 = number.substring(0, 5);
                String second5 = number.substring(5, 10);
                String rest = number.substring(10, 11);

                newNumberText = first5 + "-" + second5 + "-" + rest;
            }

            return newNumberText;
        }
    }

    /**
     * Converts this KentKart to a String representation as JSON using {@link com.google.gson.Gson}
     *
     * @return A String representation of this KentKart as JSON
     */
    public String toJson() {
        try {
            Gson gson = new Gson();
            return gson.toJson(this, KentKart.class);
        } catch (Exception e) {
            Log.error(this, "Failed to convert KentKart to json! name: " + name + ", number: " + number + ", nfcId: " + nfcId + ", regionCode: " + regionCode, e);
            return null;
        }
    }

    /**
     * Converts given JSON String to corresponding KentKart object using {@link com.google.gson.Gson}
     *
     * @param json JSON String of a KentKart from which a KentKart object will be generated
     *
     * @return Generated KentKart object or null if any error occurs
     */
    public static KentKart fromJson(String json) {
        try {
            Gson gson = new Gson();
            return gson.fromJson(json, KentKart.class);
        } catch (Exception e) {
            Log.error(KentKart.class, "Failed to generate KentKart from json! json: " + json, e);
            return null;
        }
    }

    /* Represents this KentKart as a String, internally calls toJson() */
    @Override
    public String toString() {
        return toJson();
    }

    /* Needed for making KentKart a {@link android.os.Parcelable} */
    @Override
    public int describeContents() {
        return 0;
    }

    /* Needed for making KentKart a {@link android.os.Parcelable}
     *
     * Writes each field of this KentKart to a {@link android.os.Parcel}
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(number);
        dest.writeString(nfcId);
        dest.writeString(regionCode);
    }
}
