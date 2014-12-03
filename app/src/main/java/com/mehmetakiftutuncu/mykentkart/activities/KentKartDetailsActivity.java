package com.mehmetakiftutuncu.mykentkart.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mehmetakiftutuncu.mykentkart.R;
import com.mehmetakiftutuncu.mykentkart.models.KentKart;
import com.mehmetakiftutuncu.mykentkart.utilities.Constants;
import com.mehmetakiftutuncu.mykentkart.utilities.Data;
import com.mehmetakiftutuncu.mykentkart.utilities.Log;
import com.mehmetakiftutuncu.mykentkart.utilities.StringUtils;

public class KentKartDetailsActivity extends ActionBarActivity {
    private EditText nameEditText;
    private EditText numberEditText;

    private boolean isEditMode;
    private boolean hasNfc;

    private KentKart kentKart;

    private TextWatcher numberTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {}

        @Override
        public void afterTextChanged(Editable s) {
            kentKart.number = s.toString().replaceAll("-", "");
            updateNumberText();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_kentkart_details);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }

        kentKart = new KentKart(null, null, null);
        Bundle args = getIntent().getExtras();
        if (args != null) {
            kentKart.name   = args.getString(Constants.KENT_KART_NAME);
            kentKart.number = args.getString(Constants.KENT_KART_NUMBER);
            kentKart.nfcId  = args.getString(Constants.KENT_KART_NFC_ID);
            isEditMode      = args.getBoolean(Constants.EDIT_MODE, false);
            hasNfc          = args.getBoolean(Constants.HAS_NFC, false);
        }

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        if (isEditMode) {
            actionBar.setTitle(R.string.kentKartDetailsActivity_title_edit);
        } else {
            actionBar.setTitle(R.string.kentKartDetailsActivity_title_add);
        }

        nameEditText = (EditText) findViewById(R.id.editText_kentKartDetailsActivity_name);
        numberEditText = (EditText) findViewById(R.id.editText_kentKartDetailsActivity_number);

        nameEditText.setText(kentKart.name);
        numberEditText.addTextChangedListener(numberTextWatcher);
        numberEditText.setText(kentKart.number);
        numberEditText.setEnabled(!isEditMode);

        LinearLayout nfcLayout = (LinearLayout) findViewById(R.id.linearLayout_kentKartDetailsActivity_nfc);
        TextView nfcIdTextView = (TextView) findViewById(R.id.textView_kentKartDetailsActivity_nfc_id);

        if (hasNfc && !isEditMode) {
            // Read a new card
            nfcIdTextView.setText(kentKart.nfcId);
        } else if (!hasNfc && isEditMode && !StringUtils.isEmpty(kentKart.nfcId)) {
            // Clicked edit button, card saved with NFC
            nfcIdTextView.setText(kentKart.nfcId);
        } else if (!hasNfc && isEditMode && StringUtils.isEmpty(kentKart.nfcId)) {
            // Clicked edit button, card saved without NFC
            nfcLayout.setVisibility(View.GONE);
        } else {
            // Clicked add button, no NFC
            nfcLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_kentkart_details, menu);

        MenuItem item = menu.findItem(R.id.menu_kentKartDetailsActivity_delete);
        if (item != null && !isEditMode) {
            item.setVisible(false);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                goToKentKartList(false);
                break;

            case R.id.menu_kentKartDetailsActivity_save:
                kentKart.name = nameEditText.getText().toString();
                boolean isValid = true;

                if (!kentKart.isNameValid()) {
                    String message = "Name is invalid, it cannot be empty!";
                    Log.error(this, message);
                    isValid = false;
                }
                if (!kentKart.isNumberValid()) {
                    String message = "Number is invalid, it should be 11 digits!";
                    Log.error(this, message);
                    isValid = false;
                }

                if (isValid) {
                    saveKentKart(kentKart);

                    if (!isEditMode) {
                        goToKentKartInformation(kentKart);
                    } else {
                        goToKentKartList(true);
                    }
                }
                break;

            case R.id.menu_kentKartDetailsActivity_delete:
                deleteKentKart(kentKart);
                goToKentKartList(true);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        goToKentKartList(false);
    }

    private void saveKentKart(KentKart kentKart) {
        String message = "Saving KentKart: " + kentKart;
        Log.info(this, message);
        Data.saveKentKart(kentKart);
    }

    private void deleteKentKart(KentKart kentKart) {
        String message = "Deleting KentKart: " + kentKart;
        Log.info(this, message);
        Data.deleteKentKart(kentKart);
    }

    private void goToKentKartInformation(KentKart kentKart) {
        Intent intent = new Intent(this, KentKartInformationActivity.class);
        intent.putExtra(Constants.KENT_KART_NAME, kentKart.name);
        intent.putExtra(Constants.KENT_KART_NUMBER, kentKart.number);
        startActivity(intent);
        finish();
    }

    private void goToKentKartList(boolean shouldReloadKentKartList) {
        Intent intent = new Intent(this, KentKartListActivity.class);
        if (shouldReloadKentKartList) {
            intent.putExtra(KentKartListActivity.EXTRA_RELOAD_KENTKARTS, true);
        }
        startActivity(intent);
        finish();
    }

    private void updateNumberText() {
        numberEditText.removeTextChangedListener(numberTextWatcher);

        String newNumberText = kentKart.getFormattedNumber();
        int newLastIndex = newNumberText.length();

        numberEditText.setText(newNumberText);
        numberEditText.setSelection(newLastIndex);

        numberEditText.addTextChangedListener(numberTextWatcher);
    }
}
