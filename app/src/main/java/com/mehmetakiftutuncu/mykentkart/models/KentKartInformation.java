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

/**
 * A model of information to show about a {@link com.mehmetakiftutuncu.mykentkart.models.KentKart},
 * it implements {@link android.os.Parcelable} so it can be moved around in a {@link android.os.Bundle}
 *
 * @author mehmetakiftutuncu
 */
public class KentKartInformation implements Parcelable {
    /** Balance amount of the KentKart */
    public double balance;
    /** Amount used when the KentKart is last used */
    public double lastUseAmount;
    /** Timestamp value of the time when the KentKart is last used */
    public long lastUseTime;
    /** Amount used when the KentKart is last loaded */
    public double lastLoadAmount;
    /** Timestamp value of the time when the KentKart is last loaded */
    public long lastLoadTime;

    /**
     * Constructor initializing all values
     *
     * @param balance        Value to set as {@link com.mehmetakiftutuncu.mykentkart.models.KentKartInformation#balance}
     * @param lastUseAmount  Value to set as {@link com.mehmetakiftutuncu.mykentkart.models.KentKartInformation#lastUseAmount}
     * @param lastUseTime    Value to set as {@link com.mehmetakiftutuncu.mykentkart.models.KentKartInformation#lastUseTime}
     * @param lastLoadAmount Value to set as {@link com.mehmetakiftutuncu.mykentkart.models.KentKartInformation#lastLoadAmount}
     * @param lastLoadTime   Value to set as {@link com.mehmetakiftutuncu.mykentkart.models.KentKartInformation#lastLoadTime}
     */
    public KentKartInformation(double balance, double lastUseAmount, long lastUseTime, double lastLoadAmount, long lastLoadTime) {
        this.balance = balance;
        this.lastUseAmount = lastUseAmount;
        this.lastUseTime = lastUseTime;
        this.lastLoadAmount = lastLoadAmount;
        this.lastLoadTime = lastLoadTime;
    }

    /**
     * Converts this KentKartInformation to a String representation as JSON using {@link com.google.gson.Gson}
     *
     * @return A String representation of this KentKartInformation as JSON
     */
    public String toJson() {
        try {
            Gson gson = new Gson();
            return gson.toJson(this, KentKartInformation.class);
        } catch (Exception e) {
            Log.error(this, "Failed to convert KentKartInformation to json! balance: " + balance + ", lastUseAmount: " + lastUseAmount + ", lastUseTime: " + lastUseTime + ", lastLoadAmount: " + lastLoadAmount + ", lastLoadTime: " + lastLoadTime, e);
            return null;
        }
    }

    /* Represents this KentKart as a String, internally calls toJson() */
    @Override
    public String toString() {
        return toJson();
    }

    /* Needed for making KentKartInformation a {@link android.os.Parcelable} */
    @Override
    public int describeContents() {
        return 0;
    }

    /* Needed for making KentKartInformation a {@link android.os.Parcelable}
     *
     * Writes each field of this KentKartInformation to a {@link android.os.Parcel}
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(balance);
        dest.writeDouble(lastUseAmount);
        dest.writeLong(lastUseTime);
        dest.writeDouble(lastLoadAmount);
        dest.writeLong(lastLoadTime);
    }
}
