package com.mehmetakiftutuncu.mykentkart.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.Gson;
import com.mehmetakiftutuncu.mykentkart.utilities.Log;

public class KentKartInformation implements Parcelable {
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
            Gson gson = new Gson();
            return gson.fromJson(json, KentKartInformation.class);
        } catch (Exception e) {
            Log.error(KentKart.class, "Failed to generate KentKartInformation from json! json: " + json, e);
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
        dest.writeDouble(balance);
        dest.writeDouble(lastUseAmount);
        dest.writeLong(lastUseTime);
        dest.writeDouble(lastLoadAmount);
        dest.writeLong(lastLoadTime);
    }
}
