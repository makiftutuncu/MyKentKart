package com.mehmetakiftutuncu.mykentkart.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.mehmetakiftutuncu.mykentkart.R;
import com.mehmetakiftutuncu.mykentkart.activities.KentKartListActivity;
import com.mehmetakiftutuncu.mykentkart.utilities.Log;
import com.mehmetakiftutuncu.mykentkart.utilities.StringUtils;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

public class KentKartInformationFragment extends Fragment {
    public static final String KENT_KART_NAME   = "kentKartName";
    public static final String KENT_KART_NUMBER = "kentKartNumber";

    public static final String DATE_TIME_FORMAT = "dd MMMM yyyy, HH:mm";

    private enum States {LOADING, ERROR, SUCCESS}

    private LinearLayout loadingLayout;
    private LinearLayout errorLayout;
    private ScrollView contentLayout;
    private RelativeLayout lastUseSectionLayout;
    private RelativeLayout lastLoadSectionLayout;

    private TextView balanceTextView;
    private TextView lastUseAmountTextView;
    private TextView lastUseTimeTextView;
    private TextView lastLoadAmountTextView;
    private TextView lastLoadTimeTextView;

    private String kentKartName;
    private String kentKartNumber;

    public static KentKartInformationFragment newInstance(String kentKartName, String kentKartNumber) {
        KentKartInformationFragment kentKartInformationFragment = new KentKartInformationFragment();
        Bundle args = new Bundle();

        args.putString(KENT_KART_NAME, kentKartName);
        args.putString(KENT_KART_NUMBER, kentKartNumber);

        kentKartInformationFragment.setArguments(args);
        return kentKartInformationFragment;
    }

    public KentKartInformationFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        if (args != null) {
            kentKartName = args.getString(KENT_KART_NAME);
            kentKartNumber = args.getString(KENT_KART_NUMBER);
        }

        setHasOptionsMenu(true);
        if (getActivity() != null && getActivity() instanceof ActionBarActivity) {
            ActionBar actionBar = ((ActionBarActivity) getActivity()).getSupportActionBar();
            if (actionBar != null) {
                if (!StringUtils.isEmpty(kentKartName) && !StringUtils.isEmpty(kentKartNumber)) {
                    actionBar.setTitle(kentKartName);
                    actionBar.setSubtitle(kentKartNumber);
                }

                actionBar.setHomeButtonEnabled(true);
                actionBar.setDisplayHomeAsUpEnabled(true);
            }
        }

        getKentKartInformation();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_kentkart_information, container, false);

        loadingLayout = (LinearLayout) layout.findViewById(R.id.linearLayout_loading);
        errorLayout = (LinearLayout) layout.findViewById(R.id.linearLayout_kentKartInformationFragment_error);
        contentLayout = (ScrollView) layout.findViewById(R.id.scrollView_kentKartInformationFragment_content);
        lastUseSectionLayout = (RelativeLayout) layout.findViewById(R.id.relativeLayout_kentKartInformationFragment_section_lastUse);
        lastLoadSectionLayout = (RelativeLayout) layout.findViewById(R.id.relativeLayout_kentKartInformationFragment_section_lastLoad);

        balanceTextView = (TextView) layout.findViewById(R.id.textView_kentKartInformationFragment_balance);
        lastUseAmountTextView = (TextView) layout.findViewById(R.id.textView_kentKartInformationFragment_lastUseAmount);
        lastUseTimeTextView = (TextView) layout.findViewById(R.id.textView_kentKartInformationFragment_lastUseTime);
        lastLoadAmountTextView = (TextView) layout.findViewById(R.id.textView_kentKartInformationFragment_lastLoadAmount);
        lastLoadTimeTextView = (TextView) layout.findViewById(R.id.textView_kentKartInformationFragment_lastLoadTime);

        return layout;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                goToKentKartList();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void goToKentKartList() {
        if (getActivity() != null) {
            Activity activity = getActivity();
            Intent intent = new Intent(activity, KentKartListActivity.class);
            startActivity(intent);
            activity.finish();
        }
    }

    private void getKentKartInformation() {
        changeState(States.LOADING);

        RequestQueue queue = Volley.newRequestQueue(getActivity());
        String url ="http://eshotroidplus.herokuapp.com/kentkart/" + kentKartNumber.substring(0, 10);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject info = response.getJSONObject("info");

                    try {
                        double balance = info.getDouble("balance");
                        balanceTextView.setText(balance + " TL");
                    } catch (Exception e) {
                        Log.error(KentKartInformationFragment.this, "Failed to get KentKart information, balance not found! kentKartNumber: " + kentKartNumber + ", response: " + response, e);
                        changeState(States.ERROR);

                        return;
                    }

                    boolean isLastUseAmountFound = true;
                    try {
                        double lastUseAmount = info.getDouble("lastUseAmount");
                        lastUseAmountTextView.setText(lastUseAmount + " TL");
                    } catch (Exception e) {
                        isLastUseAmountFound = false;
                    }

                    boolean isLastUseTimeFound = true;
                    try {
                        long lastUseTime = info.getLong("lastUseTime");
                        lastUseTimeTextView.setText(new SimpleDateFormat(DATE_TIME_FORMAT).format(new Date(lastUseTime)));
                    } catch (Exception e) {
                        isLastUseTimeFound = false;
                    }

                    boolean isLastLoadAmountFound = true;
                    try {
                        double lastLoadAmount = info.getDouble("lastLoadAmount");
                        lastLoadAmountTextView.setText(lastLoadAmount + " TL");
                    } catch (Exception e) {
                        isLastLoadAmountFound = false;
                    }

                    boolean isLastLoadTimeFound = true;
                    try {
                        long lastLoadTime = info.getLong("lastLoadTime");
                        lastLoadTimeTextView.setText(new SimpleDateFormat(DATE_TIME_FORMAT).format(new Date(lastLoadTime)));
                    } catch (Exception e) {
                        isLastLoadTimeFound = false;
                    }

                    if (!isLastUseAmountFound && !isLastUseTimeFound) {
                        lastUseSectionLayout.setVisibility(View.GONE);
                    } else if (!isLastUseAmountFound) {
                        lastUseAmountTextView.setVisibility(View.GONE);
                    } else if (!isLastUseTimeFound) {
                        lastUseTimeTextView.setVisibility(View.GONE);
                    }

                    if (!isLastLoadAmountFound && !isLastLoadTimeFound) {
                        lastLoadSectionLayout.setVisibility(View.GONE);
                    } else if (!isLastLoadAmountFound) {
                        lastLoadAmountTextView.setVisibility(View.GONE);
                    } else if (!isLastLoadTimeFound) {
                        lastLoadTimeTextView.setVisibility(View.GONE);
                    }

                    changeState(States.SUCCESS);
                } catch (Exception e) {
                    Log.error(KentKartInformationFragment.this, "Failed to get KentKart information! kentKartNumber: " + kentKartNumber + ", response: " + response, e);
                    changeState(States.ERROR);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.error(KentKartInformationFragment.this, "Failed to get KentKart information! kentKartNumber: " + kentKartNumber + ", response: " + error.networkResponse.statusCode + ", error: " + error.getMessage());
                changeState(States.ERROR);
            }
        });
        queue.add(jsonObjectRequest);
    }

    private void setLoadingLayoutVisibitility(int visibility) {
        if (loadingLayout != null) {
            loadingLayout.setVisibility(visibility);
        }
    }

    private void setErrorLayoutVisibitility(int visibility) {
        if (errorLayout != null) {
            errorLayout.setVisibility(visibility);
        }
    }

    private void setContentLayoutVisibitility(int visibility) {
        if (contentLayout != null) {
            contentLayout.setVisibility(visibility);
        }
    }

    private void changeState(States state) {
        switch (state) {
            case LOADING:
                setLoadingLayoutVisibitility(View.VISIBLE);
                setErrorLayoutVisibitility(View.GONE);
                setContentLayoutVisibitility(View.GONE);
                break;
            case ERROR:
                setLoadingLayoutVisibitility(View.GONE);
                setErrorLayoutVisibitility(View.VISIBLE);
                setContentLayoutVisibitility(View.GONE);
                break;
            case SUCCESS:
                setLoadingLayoutVisibitility(View.GONE);
                setErrorLayoutVisibitility(View.GONE);
                setContentLayoutVisibitility(View.VISIBLE);
                break;
        }
    }
}
