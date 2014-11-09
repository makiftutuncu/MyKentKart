package com.mehmetakiftutuncu.mykentkart.models;

import com.google.gson.Gson;
import com.mehmetakiftutuncu.mykentkart.utilities.Log;

public class KentKart {
    public String name;
    public String number;
    public String nfcId;

    public KentKart(String name, String number, String nfcId) {
        this.name = name;
        this.number = number;
        this.nfcId = nfcId;
    }

    public String toJson() {
        try {
            Gson gson = new Gson();
            return gson.toJson(this, KentKart.class);
        } catch (Exception e) {
            Log.error(this, String.format("Failed to convert Kent Kart to json! name: %s, number: %s, nfcId: %s", name, number, nfcId), e);
            return null;
        }
    }

    public static KentKart fromJson(String json) {
        try {
            Gson gson = new Gson();
            return gson.fromJson(json, KentKart.class);
        } catch (Exception e) {
            Log.error(KentKart.class, String.format("Failed to generate Kent Kart from json! json: %s!", json), e);
            return null;
        }
    }

    @Override
    public String toString() {
        return toJson();
    }
}
