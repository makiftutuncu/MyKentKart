package com.mehmetakiftutuncu.mykentkart.models;

import com.google.gson.Gson;
import com.mehmetakiftutuncu.mykentkart.utilities.Log;

public class KentKartInformation {
    public double balance;
    public double lastUseAmount;
    public long lastUseTime;
    public double lastLoadAmount;
    public long lastLoadTime;

    public KentKartInformation() {
        this(-1, -1, -1, -1, -1);
    }

    public KentKartInformation(double balance, double lastUseAmount, long lastUseTime, double lastLoadAmount, long lastLoadTime) {
        this.balance = balance;
        this.lastUseAmount = lastUseAmount;
        this.lastUseTime = lastUseTime;
        this.lastLoadAmount = lastLoadAmount;
        this.lastLoadTime = lastLoadTime;
    }

    public String toJson() {
        try {
            Gson gson = new Gson();
            return gson.toJson(this, KentKartInformation.class);
        } catch (Exception e) {
            Log.error(this, "Failed to convert KentKartInformation to json! balance: " + balance + ", lastUseAmount: " + lastUseAmount + ", lastUseTime: " + lastUseTime + ", lastLoadAmount: " + lastLoadAmount + ", lastLoadTime: " + lastLoadTime, e);
            return null;
        }
    }

    public static KentKartInformation fromJson(String json) {
        try {
            return new KentKartInformation();
        } catch (Exception e) {
            Log.error(KentKart.class, "Failed to generate KentKartInformation from json! json: " + json, e);
            return null;
        }
    }

    @Override
    public String toString() {
        return toJson();
    }
}
