package com.mehmetakiftutuncu.mykentkart.activities;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.nfc.NfcAdapter;
import android.nfc.tech.NfcA;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.mehmetakiftutuncu.mykentkart.R;
import com.mehmetakiftutuncu.mykentkart.models.KentKart;
import com.mehmetakiftutuncu.mykentkart.models.KentKartInformation;
import com.mehmetakiftutuncu.mykentkart.tasks.GetKentKartInformationTask;
import com.mehmetakiftutuncu.mykentkart.utilities.Constants;
import com.mehmetakiftutuncu.mykentkart.utilities.Data;
import com.mehmetakiftutuncu.mykentkart.utilities.Log;
import com.mehmetakiftutuncu.mykentkart.utilities.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class KentKartInformationActivity extends ActionBarActivity implements GetKentKartInformationTask.OnKentKartInformationReadyListener {
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
    private TextView connectedTransportTextView;
    private TextView lastLoadAmountTextView;
    private TextView lastLoadTimeTextView;

    private String kentKartName;
    private String kentKartNumber;

    private NfcAdapter nfcAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_kentkart_information);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }

        Bundle args = getIntent().getExtras();
        if (args != null) {
            kentKartName = args.getString(Constants.KENT_KART_NAME);
            kentKartNumber = args.getString(Constants.KENT_KART_NUMBER);
        }

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        updateTitle(actionBar);

        loadingLayout = (LinearLayout) findViewById(R.id.linearLayout_loading);
        errorLayout = (LinearLayout) findViewById(R.id.linearLayout_kentKartInformationActivity_error);
        contentLayout = (ScrollView) findViewById(R.id.scrollView_kentKartInformationActivity_content);
        lastUseSectionLayout = (RelativeLayout) findViewById(R.id.relativeLayout_kentKartInformationActivity_section_lastUse);
        lastLoadSectionLayout = (RelativeLayout) findViewById(R.id.relativeLayout_kentKartInformationActivity_section_lastLoad);

        balanceTextView = (TextView) findViewById(R.id.textView_kentKartInformationActivity_balance);
        lastUseAmountTextView = (TextView) findViewById(R.id.textView_kentKartInformationActivity_lastUseAmount);
        lastUseTimeTextView = (TextView) findViewById(R.id.textView_kentKartInformationActivity_lastUseTime);
        connectedTransportTextView = (TextView) findViewById(R.id.textView_kentKartInformationActivity_connectedTransport);
        lastLoadAmountTextView = (TextView) findViewById(R.id.textView_kentKartInformationActivity_lastLoadAmount);
        lastLoadTimeTextView = (TextView) findViewById(R.id.textView_kentKartInformationActivity_lastLoadTime);

        initializeNFCAdapter();

        handleIntent(getIntent());
    }

    @Override
    public void onPause() {
        super.onPause();

        if (nfcAdapter != null) {
            nfcAdapter.disableForegroundDispatch(this);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        if (nfcAdapter != null) {
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, KentKartInformationActivity.class).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
            IntentFilter intentFilter = new IntentFilter(NfcAdapter.ACTION_TECH_DISCOVERED);

            nfcAdapter.enableForegroundDispatch(this, pendingIntent, new IntentFilter[]{intentFilter}, new String[][]{new String[]{NfcA.class.getName()}});
        }
    }

    @Override
    public void onNewIntent(Intent intent) {
        handleIntent(intent);
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

    @Override
    public void onKentKartInformationReady(String kentKartNumber, KentKartInformation kentKartInformation) {
        if (StringUtils.isEmpty(kentKartNumber)) {
            Log.error(this, "Failed to get KentKart information, kentKartNumber is empty!");
            changeState(States.ERROR);
        } else if (kentKartNumber.length() != 11) {
            Log.error(this, "Failed to get KentKart information, kentKartNumber is invalid! kentKartNumber: " + kentKartNumber);
            changeState(States.ERROR);
        } else if (kentKartInformation == null) {
            Log.error(this, "Failed to get KentKart information, kentKartInformation is empty!");
            changeState(States.ERROR);
        } else {
            balanceTextView.setText(kentKartInformation.balance + " TL");

            boolean isLastUseAmountFound = kentKartInformation.lastUseAmount != -1;
            boolean isLastUseTimeFound = kentKartInformation.lastUseTime != -1;
            boolean isLastLoadAmountFound = kentKartInformation.lastLoadAmount != -1;
            boolean isLastLoadTimeFound = kentKartInformation.lastLoadTime != -1;

            if (!isLastUseAmountFound && !isLastUseTimeFound) {
                lastUseSectionLayout.setVisibility(View.GONE);
            } else {
                if (isLastUseAmountFound) {
                    lastUseAmountTextView.setText(kentKartInformation.lastUseAmount + " TL");
                } else {
                    lastUseAmountTextView.setVisibility(View.GONE);
                }

                if (isLastUseTimeFound) {
                    lastUseTimeTextView.setText(new SimpleDateFormat(DATE_TIME_FORMAT).format(new Date(kentKartInformation.lastUseTime)));

                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
                    if (preferences.getBoolean(Constants.PREFERENCE_CONNECTED_TRANSPORT_ENABLED, false)) {
                        String durationPreference = preferences.getString(Constants.PREFERENCE_CONNECTED_TRANSPORT_DURATION, "");
                        long connectedTransportDuration = !durationPreference.isEmpty() ? Long.parseLong(durationPreference) * 60 * 1000 : -1;
                        long difference = System.currentTimeMillis() - kentKartInformation.lastUseTime;

                        if (difference >= 0 && difference < connectedTransportDuration) {
                            String differenceMinutes = "" + (difference / (60 * 1000));
                            connectedTransportTextView.setText(getString(R.string.kentKartInformationActivity_connectedTransport_duration, differenceMinutes));
                        } else {
                            connectedTransportTextView.setVisibility(View.GONE);
                        }
                    }
                } else {
                    lastUseTimeTextView.setVisibility(View.GONE);
                }
            }

            if (!isLastLoadAmountFound && !isLastLoadTimeFound) {
                lastLoadSectionLayout.setVisibility(View.GONE);
            } else {
                if (isLastLoadAmountFound) {
                    lastLoadAmountTextView.setText(kentKartInformation.lastLoadAmount + " TL");
                } else {
                    lastLoadAmountTextView.setVisibility(View.GONE);
                }

                if (isLastLoadTimeFound) {
                    lastLoadTimeTextView.setText(new SimpleDateFormat(DATE_TIME_FORMAT).format(new Date(kentKartInformation.lastLoadTime)));
                } else {
                    lastLoadTimeTextView.setVisibility(View.GONE);
                }
            }

            changeState(States.SUCCESS);
        }
    }

    private void goToKentKartList() {
        Intent intent = new Intent(this, KentKartListActivity.class);
        startActivity(intent);
        finish();
    }

    private void initializeNFCAdapter() {
        if (nfcAdapter == null) {
            NfcAdapter adapter = NfcAdapter.getDefaultAdapter(this);

            if (adapter == null) {
                String message = "Sorry, your device doesn't support NFC!";
                Log.error(this, message);
            } else {
                nfcAdapter = adapter;
                boolean isNfcOn = nfcAdapter.isEnabled();

                if (!isNfcOn) {
                    String message = "NFC is disabled! Enable it and try again.";
                    Log.error(this, message);
                }
            }
        }
    }

    private void handleIntent(Intent intent) {
        if (intent != null) {
            String action = intent.getAction();

            if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)) {
                // An NFC tag has been read
                byte[] tagId = intent.getByteArrayExtra(NfcAdapter.EXTRA_ID);

                String id = StringUtils.generateNfcId(tagId);

                Log.info(this, "KentKart NFC id: " + id);

                KentKart loadedKentKart = Data.loadKentKart(id);

                if (loadedKentKart != null) {
                    // Found a saved KentKart
                    String message = "Going to get info about KentKart... kentKart: " + loadedKentKart;
                    Log.info(this, message);

                    kentKartName = loadedKentKart.name;
                    kentKartNumber = loadedKentKart.number;
                    getKentKartInformation();
                } else {
                    // This is a new KentKart
                    Log.info(this, "New KentKart with id: " + id);

                    Intent i = new Intent(this, KentKartDetailsActivity.class);
                    i.putExtra(Constants.KENT_KART_NFC_ID, id);
                    i.putExtra(Constants.HAS_NFC, true);
                    startActivity(i);
                    finish();
                }
            } else {
                // User came to this activity from UI
                getKentKartInformation();
            }
        }
    }

    private void getKentKartInformation() {
        updateTitle(getSupportActionBar());
        changeState(States.LOADING);

        new GetKentKartInformationTask(kentKartNumber, this).execute();
    }

    private void updateTitle(ActionBar actionBar) {
        if (!StringUtils.isEmpty(kentKartName) && !StringUtils.isEmpty(kentKartNumber)) {
            actionBar.setTitle(kentKartName);
            actionBar.setSubtitle(KentKart.getFormattedNumber(kentKartNumber));
        }
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
