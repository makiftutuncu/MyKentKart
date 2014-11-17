package com.mehmetakiftutuncu.mykentkart.models;

import com.google.gson.Gson;
import com.mehmetakiftutuncu.mykentkart.utilities.Log;
import com.mehmetakiftutuncu.mykentkart.utilities.StringUtils;

public class KentKart {
    public String name;
    public String number;
    public String nfcId;

    public KentKart(String name, String number, String nfcId) {
        this.name = name;
        this.number = number;
        this.nfcId = nfcId;
    }

    public boolean isNameValid() {
        return !StringUtils.isEmpty(name);
    }

    public boolean isNumberValid() {
        return !StringUtils.isEmpty(number) && number.length() == 11;
    }

    public String getFormattedNumber() {
        return KentKart.getFormattedNumber(number);
    }

    public static String getFormattedNumber(String number) {
        if (number == null) {
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
            Log.error(this, "Failed to convert KentKart to json! name: " + name + ", number: " + number + ", nfcId: " + nfcId, e);
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
}
