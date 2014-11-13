package com.mehmetakiftutuncu.mykentkart.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mehmetakiftutuncu.mykentkart.R;
import com.mehmetakiftutuncu.mykentkart.models.KentKart;
import com.mehmetakiftutuncu.mykentkart.utilities.Log;
import com.mehmetakiftutuncu.mykentkart.utilities.StringUtils;

public class KentKartDetailsFragment extends Fragment {
    public static final String IS_EDIT_MODE     = "isEditMode";
    public static final String HAS_NFC          = "hasNfc";
    public static final String IS_NFC_ON        = "isNfcOn";
    public static final String KENT_KART_NAME   = "kentKartName";
    public static final String KENT_KART_NUMBER = "kentKartNumber";
    public static final String KENT_KART_NFC_ID = "kentKartNfcId";

    public interface KentKartDetailsListener {
        public void onKentKartSave(KentKart kentKart);
        public void onKentKartDelete(KentKart kentKart);
    }

    private KentKartDetailsListener kentKartDetailsListener;

    private EditText nameEditText;
    private EditText numberEditText;
    private LinearLayout nfcLayout;
    private TextView nfcIdTextView;

    private boolean isEditMode;
    private boolean hasNfc;
    private boolean isNfcOn;
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

    public static KentKartDetailsFragment newInstance(boolean hasNfc, boolean isNfcOn) {
        return newInstance(false, hasNfc, isNfcOn, null, null, null);
    }

    public static KentKartDetailsFragment newInstance(boolean hasNfc, boolean isNfcOn, String nfcId) {
        return newInstance(false, hasNfc, isNfcOn, null, null, nfcId);
    }

    public static KentKartDetailsFragment newInstance(boolean isEditMode, boolean hasNfc, boolean isNfcOn, String name, String number, String nfcId) {
        KentKartDetailsFragment fragment = new KentKartDetailsFragment();
        Bundle args = new Bundle();

        args.putBoolean(IS_EDIT_MODE, isEditMode);
        args.putBoolean(HAS_NFC, hasNfc);
        args.putBoolean(IS_NFC_ON, isNfcOn);
        if (!StringUtils.isEmpty(name)) {
            args.putString(KENT_KART_NAME, name);
        }
        if (!StringUtils.isEmpty(number)) {
            args.putString(KENT_KART_NUMBER, number);
        }
        if (!StringUtils.isEmpty(nfcId)) {
            args.putString(KENT_KART_NFC_ID, nfcId);
        }

        fragment.setArguments(args);
        return fragment;
    }

    public KentKartDetailsFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        kentKart = new KentKart(null, null, null);
        Bundle args = getArguments();
        if (args != null) {
            isEditMode      = args.getBoolean(IS_EDIT_MODE, false);
            hasNfc          = args.getBoolean(HAS_NFC, false);
            isNfcOn         = args.getBoolean(IS_NFC_ON, false);
            kentKart.name   = args.getString(KENT_KART_NAME);
            kentKart.number = args.getString(KENT_KART_NUMBER);
            kentKart.nfcId  = args.getString(KENT_KART_NFC_ID);
        }

        setHasOptionsMenu(true);
        if (getActivity() != null && getActivity() instanceof ActionBarActivity) {
            ActionBar actionBar = ((ActionBarActivity) getActivity()).getSupportActionBar();
            if (actionBar != null) {
                if (isEditMode) {
                    actionBar.setTitle(R.string.title_kentKartDetailsFragment_edit);
                } else {
                    actionBar.setTitle(R.string.title_kentKartDetailsFragment_add);
                }

                actionBar.setHomeButtonEnabled(true);
                actionBar.setDisplayHomeAsUpEnabled(true);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_kentkart_details, container, false);

        nameEditText = (EditText) layout.findViewById(R.id.editText_kentKartDetailsFragment_name);
        numberEditText = (EditText) layout.findViewById(R.id.editText_kentKartDetailsFragment_number);
        nfcLayout = (LinearLayout) layout.findViewById(R.id.linearLayout_kentKartDetailsFragment_nfc);
        nfcIdTextView = (TextView) layout.findViewById(R.id.textView_kentKartDetailsFragment_nfc_id);

        numberEditText.addTextChangedListener(numberTextWatcher);

        if (!hasNfc) {
            nfcLayout.setVisibility(View.GONE);
        } else {
            if (StringUtils.isEmpty(kentKart.nfcId)) {
                nfcIdTextView.setText(R.string.notSet);
            } else {
                nfcIdTextView.setText(kentKart.nfcId);
            }

            if (!isNfcOn) {
                String message = "NFC is off!";
                Log.info(this, message);
            }
        }

        return layout;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_kentkart_details, menu);

        MenuItem item = menu.findItem(R.id.menu_kentKartDetailsFragment_delete);
        if (item != null && !isEditMode) {
            item.setVisible(false);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                break;

            case R.id.menu_kentKartDetailsFragment_save:
                if (kentKartDetailsListener != null) {
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
                        kentKartDetailsListener.onKentKartSave(kentKart);
                    }
                }
                break;

            case R.id.menu_kentKartDetailsFragment_delete:
                if (kentKartDetailsListener != null) {
                    kentKartDetailsListener.onKentKartDelete(kentKart);
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            kentKartDetailsListener = (KentKartDetailsListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement KentKartDetailsListener!");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();

        kentKartDetailsListener = null;
    }

    private void updateNumberText() {
        numberEditText.removeTextChangedListener(numberTextWatcher);

        int length = kentKart.number.length();
        String newNumberText = kentKart.number;
        int newLastIndex = length;
        if (length >= 6 && length < 11) {
            // After first 5 digits
            String first5 = kentKart.number.substring(0, 5);
            String rest = kentKart.number.substring(5, length);

            newNumberText = first5 + "-" + rest;
            newLastIndex = newNumberText.length();
        } else if (length >= 11) {
            // After first 10 digits
            String first5 = kentKart.number.substring(0, 5);
            String second5 = kentKart.number.substring(5, 10);
            String rest = kentKart.number.substring(10, 11);

            newNumberText = first5 + "-" + second5 + "-" + rest;
            newLastIndex = newNumberText.length();
        }
        numberEditText.setText(newNumberText);
        numberEditText.setSelection(newLastIndex);

        numberEditText.addTextChangedListener(numberTextWatcher);
    }
}
