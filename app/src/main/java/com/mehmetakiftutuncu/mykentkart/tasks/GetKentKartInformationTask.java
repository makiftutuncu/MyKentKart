package com.mehmetakiftutuncu.mykentkart.tasks;

import android.os.AsyncTask;

import com.mehmetakiftutuncu.mykentkart.models.KentKartInformation;
import com.mehmetakiftutuncu.mykentkart.utilities.Constants;
import com.mehmetakiftutuncu.mykentkart.utilities.Log;
import com.mehmetakiftutuncu.mykentkart.utilities.StringUtils;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Random;

public class GetKentKartInformationTask extends AsyncTask<Void, Void, KentKartInformation> {
    public interface OnKentKartInformationReadyListener {
        public void onKentKartInformationReady(String kentKartNumber, KentKartInformation kentKartInformation);
    }

    private OnKentKartInformationReadyListener listener;
    private String kentKartNumber;
    private String kentKartRegionCode;

    private String getUrl(String region, String kentKartNumber) {
        return String.format(
            "http://m.kentkart.com/new/services/?cmd=getBalance&region=%s&alias=%s",
            region,
            kentKartNumber
        );
    }

    public GetKentKartInformationTask(String kentKartNumber, String kentKartRegionCode, OnKentKartInformationReadyListener listener) {
        this.kentKartNumber = kentKartNumber;
        this.kentKartRegionCode = kentKartRegionCode;
        this.listener = listener;
    }

    @Override
    protected KentKartInformation doInBackground(Void... params) {
        if (StringUtils.isEmpty(kentKartNumber)) {
            Log.error(this, "Failed to get KentKart information, kentKartNumber is empty!");
            return null;
        } else if (kentKartNumber.length() != 11) {
            Log.error(this, "Failed to get KentKart information, kentKartNumber is invalid! kentKartNumber: " + kentKartNumber);
            return null;
        } else if (StringUtils.isEmpty(kentKartRegionCode)) {
            Log.error(this, "Failed to get KentKart information, kentKartRegionCode is empty!");
            return null;
        } else {
            try {
                DefaultHttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(getUrl(kentKartRegionCode, kentKartNumber));

                HttpResponse httpResponse = httpClient.execute(httpPost);

                int statusCode = httpResponse.getStatusLine().getStatusCode();
                if (statusCode != 200) {
                    Log.error(this, "Failed to get KentKart information, invalid status code! kentKartNumber: " + kentKartNumber + ", statusCode: " + statusCode);
                    return null;
                } else {
                    HttpEntity httpEntity = httpResponse.getEntity();
                    String result = EntityUtils.toString(httpEntity, "UTF-8");

                    Log.info(this, "Result from KentKart");
                    Log.info(this, result);

                    JSONObject resultJson = new JSONObject(result);
                    JSONArray cardsJson = resultJson.getJSONArray("cardlist");
                    JSONObject kentKartJson = cardsJson.getJSONObject(0);

                    try {
                        String balanceString = kentKartJson.getString("balance");
                        double balance = Double.parseDouble(balanceString);

                        JSONArray usagesJson = kentKartJson.getJSONArray("usage");

                        JSONObject lastUseJson = usagesJson.getJSONObject(0);

                        double lastUseAmount = -1;
                        try {
                            String lastUseAmountString = lastUseJson.getString("amt");
                            lastUseAmount = Double.parseDouble(lastUseAmountString);
                        } catch (Exception e) {
                            Log.info(this, "Failed to get KentKart last use amount! kentKartNumber: " + kentKartNumber + ", result: " + resultJson);
                        }

                        long lastUseTime = -1;
                        try {
                            String lastUseTimeString = lastUseJson.getString("date");
                            lastUseTime = new SimpleDateFormat(Constants.KENT_KART_RESPONSE_DATE_TIME_FORMAT).parse(lastUseTimeString).getTime();
                        } catch (Exception e) {
                            Log.info(this, "Failed to get KentKart last use date! kentKartNumber: " + kentKartNumber + ", result: " + resultJson);
                        }

                        JSONObject lastLoadJson = usagesJson.getJSONObject(1);

                        double lastLoadAmount = -1;
                        try {
                            String lastLoadAmountString = lastLoadJson.getString("amt");
                            lastLoadAmount = Double.parseDouble(lastLoadAmountString);
                        } catch (Exception e) {
                            Log.info(this, "Failed to get KentKart last load amount! kentKartNumber: " + kentKartNumber + ", result: " + resultJson);
                        }

                        long lastLoadTime = -1;
                        try {
                            String lastLoadTimeString = lastLoadJson.getString("date");
                            lastLoadTime = new SimpleDateFormat(Constants.KENT_KART_RESPONSE_DATE_TIME_FORMAT).parse(lastLoadTimeString).getTime();
                        } catch (Exception e) {
                            Log.info(this, "Failed to get KentKart last load date! kentKartNumber: " + kentKartNumber + ", result: " + resultJson);
                        }

                        return new KentKartInformation(balance, lastUseAmount, lastUseTime, lastLoadAmount, lastLoadTime);
                    } catch (Exception e) {
                        Log.error(this, "Failed to get KentKart information, balance not found! kentKartNumber: " + kentKartNumber + ", result: " + resultJson, e);
                        return null;
                    }
                }
            } catch (Exception e) {
                Log.error(this, "Failed to get KentKart information! kentKartNumber: " + kentKartNumber, e);
                return null;
            }
        }
    }

    @Override
    protected void onPostExecute(KentKartInformation kentKartInformation) {
        super.onPostExecute(kentKartInformation);

        listener.onKentKartInformationReady(kentKartNumber, kentKartInformation);
    }
}
