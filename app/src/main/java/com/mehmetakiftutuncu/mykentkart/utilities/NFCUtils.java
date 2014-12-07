package com.mehmetakiftutuncu.mykentkart.utilities;

import android.content.Context;
import android.nfc.NfcAdapter;

public class NFCUtils {
    private static NFCUtils instance;
    private static NfcAdapter adapter;

    private NFCUtils(Context context) {
        adapter = NfcAdapter.getDefaultAdapter(context);
    }

    public static NFCUtils get(Context context) {
        if (instance == null) {
            instance = new NFCUtils(context);
        }

        return instance;
    }

    public NfcAdapter getAdapter() {
        return adapter;
    }

    public boolean hasNfc() {
        return adapter != null;
    }

    public boolean isNfcOn() {
        return hasNfc() && adapter.isEnabled();
    }
}
