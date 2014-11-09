package com.mehmetakiftutuncu.mykentkart.activities;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.nfc.tech.NfcA;
import android.os.Bundle;
import android.widget.Toast;

import com.mehmetakiftutuncu.mykentkart.fragments.KentKartInputDialogFragment;
import com.mehmetakiftutuncu.mykentkart.models.KentKart;
import com.mehmetakiftutuncu.mykentkart.utilities.Data;
import com.mehmetakiftutuncu.mykentkart.utilities.Log;
import com.mehmetakiftutuncu.mykentkart.utilities.StringUtils;


public class NFCActivity extends Activity {
    private NfcAdapter nfcAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
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
                String message = "Sorry, your device doesn't support NFC!";
                Log.error(this, message);
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            } else {
                nfcAdapter = adapter;

                if (!nfcAdapter.isEnabled()) {
                    String message = "NFC is disabled! Enable it and try again.";
                    Log.error(this, message);
                    Toast.makeText(this, message, Toast.LENGTH_LONG).show();
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

                    Log.info(this, "Going to get info about KentKart... kentKart: " + loadedKentKart);
                } else {
                    // This is a new KentKart

                    KentKartInputDialogFragment kentKartInputDialogFragment = new KentKartInputDialogFragment();
                    Bundle args = new Bundle();
                    args.putString(KentKartInputDialogFragment.EXTRA_NFC_ID, id);
                    kentKartInputDialogFragment.setArguments(args);
                    kentKartInputDialogFragment.setOnKentKartInputListener(new KentKartInputDialogFragment.OnKentKartInputListener() {
                        @Override
                        public void onKentKartInput(KentKart kentKart) {
                            Log.info(this, "KentKart: " + kentKart);

                            boolean saveResult = Data.saveKentKart(kentKart);

                            if (saveResult) {
                                Log.info(this, "KentKart is saved! kentKart: " + kentKart);
                            }
                        }
                    });
                    kentKartInputDialogFragment.show(getFragmentManager(), "kentKartNumberInputDialogFragment");
                }
            }
        }
    }
}
