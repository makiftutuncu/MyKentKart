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

import android.content.Context;
import android.nfc.NfcAdapter;

/**
 * A utility class for basic NFC operations, implemented as singleton
 *
 * @author mehmetakiftutuncu
 */
public class NFCUtils {
    /** Instance of this singleton class */
    private static NFCUtils instance;
    /** Instance of {@link android.nfc.NfcAdapter} to handle NFC operations */
    private static NfcAdapter adapter;

    /**
     * Private constructor for the singleton
     *
     * @param context {@link android.content.Context} to access {@link android.nfc.NfcAdapter}
     */
    private NFCUtils(Context context) {
        adapter = NfcAdapter.getDefaultAdapter(context);
    }

    /**
     * Access method to the singleton, gives the instance of this class
     *
     * @param context {@link android.content.Context} to access {@link android.nfc.NfcAdapter}
     *
     * @return The instance of this class
     */
    public static NFCUtils get(Context context) {
        if (instance == null) {
            instance = new NFCUtils(context);
        }

        return instance;
    }

    /**
     * Gets {@link com.mehmetakiftutuncu.mykentkart.utilities.NFCUtils#adapter} instance
     *
     * @return {@link com.mehmetakiftutuncu.mykentkart.utilities.NFCUtils#adapter} instance
     */
    public NfcAdapter getAdapter() {
        return adapter;
    }

    /**
     * Checks whether or not the device has NFC support
     *
     * @return true if the device has NFC support or false otherwise
     */
    public boolean hasNfc() {
        return adapter != null;
    }

    /**
     * Checks whether or not NFC is turned on on the device
     *
     * @return true if NFC is turned on on the device or false otherwise
     */
    public boolean isNfcOn() {
        return hasNfc() && adapter.isEnabled();
    }
}
