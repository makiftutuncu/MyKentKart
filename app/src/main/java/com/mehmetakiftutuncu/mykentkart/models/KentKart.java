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

public class KentKart implements Parcelable {
    public String name;
    public String number;
    public String nfcId;
    public String regionCode;

    public KentKart(String name, String number, String nfcId, String regionCode) {
        this.name = name;
        this.number = number;
        this.nfcId = nfcId;
        this.regionCode = regionCode;
    }

    public String getFormattedNumber() {
        return KentKart.getFormattedNumber(number);
    }

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

    public String toJson() {
        try {
            Gson gson = new Gson();
            return gson.toJson(this, KentKart.class);
        } catch (Exception e) {
            Log.error(this, "Failed to convert KentKart to json! name: " + name + ", number: " + number + ", nfcId: " + nfcId + ", regionCode: " + regionCode, e);
            return null;
        }
    }

    public static KentKart fromJson(String json) {
        try {
            Gson gson = new Gson();
            return gson.fromJson(json, KentKart.class);
        } catch (Exception e) {
            Log.error(KentKart.class, "Failed to generate KentKart from json! json: " + json, e);
            return null;
        }
    }

    @Override
    public String toString() {
        return toJson();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(number);
        dest.writeString(nfcId);
        dest.writeString(regionCode);
    }
}
