package com.mehmetakiftutuncu.mykentkart.tasks;

import android.os.AsyncTask;

import com.mehmetakiftutuncu.mykentkart.models.KentKart;
import com.mehmetakiftutuncu.mykentkart.utilities.Log;

public class GetKentKartInformationTask extends AsyncTask<KentKart, Void, String> {
    @Override
    protected String doInBackground(KentKart... params) {
        KentKart kentKart = params != null ? params[0] : null;

        if (kentKart == null) {
            Log.error(this, "Failed to get KentKart information, kentKart is null!");
            return null;
        } else if (kentKart.isNumberValid()) {
            Log.error(this, "Failed to get KentKart information, number is invalid! kentKart: " + kentKart);
            return null;
        } else {
            String trimmedNumber = kentKart.number.substring(0, 10);
            String url = "http://eshotroidplus.herokuapp.com/kentkart/" + trimmedNumber;
        }

        return null;
    }
}
