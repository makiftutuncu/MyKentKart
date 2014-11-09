package com.mehmetakiftutuncu.mykentkart.activities;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.nfc.tech.NfcA;
import android.os.Bundle;
import android.widget.Toast;

import com.mehmetakiftutuncu.mykentkart.fragments.KentKartNumberInputDialogFragment;
import com.mehmetakiftutuncu.mykentkart.utilities.Log;

import java.security.MessageDigest;
import java.util.Arrays;


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

                String id = generateKentKartId(tagId);

                String message = String.format("Kent Kart Id: %s", id);
                Log.info(this, message);
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();

                KentKartNumberInputDialogFragment kentKartNumberInputDialogFragment = new KentKartNumberInputDialogFragment();
                kentKartNumberInputDialogFragment.setOnKentKartNumberInputListener(new KentKartNumberInputDialogFragment.OnKentKartNumberInputListener() {
                    @Override
                    public void onKentKartNumberInput(String value) {
                        String message = String.format("Kent Kart Number: %s", value);
                        Log.info(this, message);
                        Toast.makeText(NFCActivity.this, message, Toast.LENGTH_LONG).show();
                    }
                });
                kentKartNumberInputDialogFragment.show(getFragmentManager(), "kentKartNumberInputDialogFragment");
            }
        }
    }

    private String generateKentKartId(byte[] input) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            byte[] resultBytes = md.digest(input);
            StringBuilder sb = new StringBuilder();
            for (byte b : resultBytes) {
                sb.append(Integer.toString((b & 0xFF) + 0x100, 16).substring(1));
            }
            return sb.toString().toUpperCase();
        } catch (Exception e) {
            Log.error(this, "An error occurred while generating Kent Kart id from input " + Arrays.toString(input), e);
            return null;
        }
    }
}
