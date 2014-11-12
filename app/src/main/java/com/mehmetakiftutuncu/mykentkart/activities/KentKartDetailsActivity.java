package com.mehmetakiftutuncu.mykentkart.activities;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.nfc.tech.NfcA;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.mehmetakiftutuncu.mykentkart.R;
import com.mehmetakiftutuncu.mykentkart.fragments.KentKartDetailsFragment;
import com.mehmetakiftutuncu.mykentkart.models.KentKart;
import com.mehmetakiftutuncu.mykentkart.utilities.Data;
import com.mehmetakiftutuncu.mykentkart.utilities.Log;
import com.mehmetakiftutuncu.mykentkart.utilities.StringUtils;

public class KentKartDetailsActivity extends ActionBarActivity implements KentKartDetailsFragment.KentKartDetailsListener {
    private Toolbar toolbar;
    private NfcAdapter nfcAdapter;

    private boolean hasNfc = false;
    private boolean isNfcOn = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_kentkart_details);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }

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
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, KentKartDetailsActivity.class).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
            IntentFilter intentFilter = new IntentFilter(NfcAdapter.ACTION_TECH_DISCOVERED);

            nfcAdapter.enableForegroundDispatch(this, pendingIntent, new IntentFilter[]{intentFilter}, new String[][]{new String[]{NfcA.class.getName()}});
        }
    }

    @Override
    public void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    private void initializeNFCAdapter() {
        if (nfcAdapter == null) {
            NfcAdapter adapter = NfcAdapter.getDefaultAdapter(this);

            if (adapter == null) {
                hasNfc = false;

                String message = "Sorry, your device doesn't support NFC!";
                Log.error(this, message);
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            } else {
                nfcAdapter = adapter;
                hasNfc = true;

                if (!nfcAdapter.isEnabled()) {
                    isNfcOn = false;

                    String message = "NFC is disabled! Enable it and try again.";
                    Log.error(this, message);
                    Toast.makeText(this, message, Toast.LENGTH_LONG).show();
                } else {
                    isNfcOn = true;
                }
            }
        }
    }

    private void handleIntent(Intent intent) {
        if (intent != null) {
            String action = intent.getAction();

            if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)) {
                byte[] tagId = intent.getByteArrayExtra(NfcAdapter.EXTRA_ID);

                String id = StringUtils.generateNfcId(tagId);

                Log.info(this, "KentKart NFC id: " + id);

                KentKart loadedKentKart = Data.loadKentKart(id);

                if (loadedKentKart != null) {
                    // Found a saved KentKart
                    String message = "Going to get info about KentKart... kentKart: " + loadedKentKart;
                    Log.info(this, message);
                    Toast.makeText(this, message, Toast.LENGTH_LONG).show();
                } else {
                    // This is a new KentKart
                    Log.info(this, "New KentKart with id: " + id);
                    KentKartDetailsFragment kentKartDetailsFragment = KentKartDetailsFragment.newInstance(hasNfc, isNfcOn, id);
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.container, kentKartDetailsFragment);
                    fragmentTransaction.commit();
                }
            } else {
                KentKartDetailsFragment kentKartDetailsFragment = KentKartDetailsFragment.newInstance(hasNfc, isNfcOn);
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container, kentKartDetailsFragment);
                fragmentTransaction.commit();
            }
        }
    }

    @Override
    public void onKentKartSave(KentKart kentKart) {
        Log.info(this, "Saving KentKart: " + kentKart);
        Data.saveKentKart(kentKart);
    }

    @Override
    public void onKentKartDelete(KentKart kentKart) {
        Log.info(this, "Deleting KentKart: " + kentKart);
    }
}
