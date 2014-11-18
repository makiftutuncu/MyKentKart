package com.mehmetakiftutuncu.mykentkart.tasks;

import android.os.AsyncTask;

import com.mehmetakiftutuncu.mykentkart.models.KentKartInformation;
import com.mehmetakiftutuncu.mykentkart.utilities.Log;
import com.mehmetakiftutuncu.mykentkart.utilities.StringUtils;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Random;

public class GetKentKartInformationTask extends AsyncTask<Void, Void, KentKartInformation> {
    public interface OnKentKartInformationReadyListener {
        public void onKentKartInformationReady(String kentKartNumber, KentKartInformation kentKartInformation);
    }

    private static final String URL = "http://m.kentkart.com/kws.php";

    private static final String[] USER_AGENTS = new String[] {
        "Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/31.0.1650.63 Safari/537.36",
        "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/31.0.1650.63 Safari/537.36",
        "Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:24.0) Gecko/20100101 Firefox/24.0 Waterfox/24.0",
        "Mozilla/5.0 (Linux; U; Android 4.0.3; de-ch; HTC Sensation Build/IML74K) AppleWebKit/534.30 (KHTML, like Gecko) Version/4.0 Mobile Safari/534.30"
    };

    public static final String KENT_KART_RESPONSE_DATE_TIME_FORMAT = "yyyyMMddHHmmss";

    private OnKentKartInformationReadyListener listener;
    private String kentKartNumber;

    public GetKentKartInformationTask(String kentKartNumber, OnKentKartInformationReadyListener listener) {
        this.kentKartNumber = kentKartNumber;
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
        } else {
            try {
                DefaultHttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(URL);
                ArrayList<BasicNameValuePair> parameters = new ArrayList<BasicNameValuePair>();

                httpPost.setHeader("User-Agent", USER_AGENTS[new Random().nextInt(USER_AGENTS.length)]);
                parameters.add(new BasicNameValuePair("func", "bs"));
                parameters.add(new BasicNameValuePair("myregion", "006"));
                parameters.add(new BasicNameValuePair("myregiontitle", "IZMIR"));
                parameters.add(new BasicNameValuePair("val", kentKartNumber.substring(0, 10)));
                httpPost.setEntity(new UrlEncodedFormEntity(parameters));

                HttpResponse httpResponse = httpClient.execute(httpPost);

                int statusCode = httpResponse.getStatusLine().getStatusCode();
                if(statusCode != 200) {
                    Log.error(this, "Failed to get KentKart information, invalid status code! kentKartNumber: " + kentKartNumber + ", statusCode: " + statusCode);
                    return null;
                } else {
                    HttpEntity httpEntity = httpResponse.getEntity();
                    String result = EntityUtils.toString(httpEntity, "UTF-8");

                    JSONObject json = new JSONObject(result);

                    try {
                        String balanceString = json.getString("balanceresult");
                        double balance = Double.parseDouble(balanceString.replaceAll(",", "\\."));

                        double lastUseAmount = -1;
                        try {
                            String lastUseAmountString = json.getString("usageAmt");
                            lastUseAmount = Double.parseDouble(lastUseAmountString.replaceAll(",", "\\."));
                        } catch (Exception e) {}

                        long lastUseTime = -1;
                        try {
                            String lastUseTimeString = json.getString("usageresult");
                            lastUseTime = new SimpleDateFormat(KENT_KART_RESPONSE_DATE_TIME_FORMAT).parse(lastUseTimeString).getTime();
                        } catch (Exception e) {}

                        double lastLoadAmount = -1;
                        try {
                            String lastLoadAmountString = json.getString("chargeAmt");
                            lastLoadAmount = Double.parseDouble(lastLoadAmountString.replaceAll(",", "\\."));
                        } catch (Exception e) {}

                        long lastLoadTime = -1;
                        try {
                            String lastLoadTimeString = json.getString("chargeresult");
                            lastLoadTime = new SimpleDateFormat(KENT_KART_RESPONSE_DATE_TIME_FORMAT).parse(lastLoadTimeString).getTime();
                        } catch (Exception e) {}

                        return new KentKartInformation(balance, lastUseAmount, lastUseTime, lastLoadAmount, lastLoadTime);
                    } catch (Exception e) {
                        Log.error(this, "Failed to get KentKart information, balance not found! kentKartNumber: " + kentKartNumber + ", response: " + json, e);
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
