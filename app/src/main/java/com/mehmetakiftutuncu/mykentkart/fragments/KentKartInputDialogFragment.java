package com.mehmetakiftutuncu.mykentkart.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.mehmetakiftutuncu.mykentkart.R;
import com.mehmetakiftutuncu.mykentkart.models.KentKart;
import com.mehmetakiftutuncu.mykentkart.utilities.Log;
import com.mehmetakiftutuncu.mykentkart.utilities.StringUtils;

public class KentKartInputDialogFragment extends DialogFragment implements View.OnClickListener {
    public static final String EXTRA_NFC_ID = "nfcId";

    private EditText nameEditText;
    private TextView numberTextView;
    private KentKart kentKart = new KentKart("", "", "");
    private OnKentKartInputListener onKentKartInputListener;

    public static interface OnKentKartInputListener {
        public void onKentKartInput(KentKart kentKart);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        View header = layoutInflater.inflate(R.layout.kentkart_input_header, null, false);
        View layout = layoutInflater.inflate(R.layout.kentkart_input, null, false);

        nameEditText = (EditText) layout.findViewById(R.id.kentKartInput_name);
        numberTextView = (TextView) layout.findViewById(R.id.kentKartInput_number);
        ImageButton acceptButton = (ImageButton) header.findViewById(R.id.kentKartInput_accept);
        ImageButton deleteButton = (ImageButton) layout.findViewById(R.id.kentKartInput_delete);
        Button button0 = (Button) layout.findViewById(R.id.kentKartInput_button0);
        Button button1 = (Button) layout.findViewById(R.id.kentKartInput_button1);
        Button button2 = (Button) layout.findViewById(R.id.kentKartInput_button2);
        Button button3 = (Button) layout.findViewById(R.id.kentKartInput_button3);
        Button button4 = (Button) layout.findViewById(R.id.kentKartInput_button4);
        Button button5 = (Button) layout.findViewById(R.id.kentKartInput_button5);
        Button button6 = (Button) layout.findViewById(R.id.kentKartInput_button6);
        Button button7 = (Button) layout.findViewById(R.id.kentKartInput_button7);
        Button button8 = (Button) layout.findViewById(R.id.kentKartInput_button8);
        Button button9 = (Button) layout.findViewById(R.id.kentKartInput_button9);

        acceptButton.setOnClickListener(this);
        deleteButton.setOnClickListener(this);
        deleteButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                kentKart.number = "";
                updateNumber();
                return true;
            }
        });
        button0.setOnClickListener(this);
        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
        button3.setOnClickListener(this);
        button4.setOnClickListener(this);
        button5.setOnClickListener(this);
        button6.setOnClickListener(this);
        button7.setOnClickListener(this);
        button8.setOnClickListener(this);
        button9.setOnClickListener(this);

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        dialogBuilder.setCustomTitle(header);
        dialogBuilder.setView(layout);

        AlertDialog dialog = dialogBuilder.create();

        Bundle args = getArguments();
        if (args != null) {
            kentKart.nfcId = args.getString(EXTRA_NFC_ID);
        }

        updateNumber();

        return dialog;
    }

    private void addDigit(int digit) {
        if(digit < 0 || digit > 9) return;

        if(kentKart.number.length() < 11) {
            kentKart.number += digit;
            updateNumber();
        }
    }

    private void deleteDigit() {
        if(kentKart.number.length() > 0) {
            kentKart.number = kentKart.number.substring(0, kentKart.number.length() - 1);
            updateNumber();
        }
    }

    private void updateNumber() {
        int length = kentKart.number.length();

        if (length >= 0 && length < 6) {
            // Until first 5 digits
            numberTextView.setText(kentKart.number);
        } else if (length >= 6 && length < 11) {
            // After first 5 digits
            String first5 = kentKart.number.substring(0, 5);
            String rest = kentKart.number.substring(5, length);

            numberTextView.setText(first5 + "-" + rest);
        } else if (length >= 11) {
            // After first 10 digits
            String first5 = kentKart.number.substring(0, 5);
            String second5 = kentKart.number.substring(5, 10);
            String rest = kentKart.number.substring(10, length);

            numberTextView.setText(first5 + "-" + second5 + "-" + rest);
        }
    }

    public void setOnKentKartInputListener(OnKentKartInputListener onKentKartInputListener) {
        this.onKentKartInputListener = onKentKartInputListener;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.kentKartInput_accept:
                kentKart.name = nameEditText.getText().toString();

                if(onKentKartInputListener != null) {
                    if (kentKart.isValid()) {
                        onKentKartInputListener.onKentKartInput(kentKart);
                        dismiss();
                    } else {
                        boolean isNameEmpty = StringUtils.isEmpty(kentKart.name);
                        boolean isNumberEmpty = StringUtils.isEmpty(kentKart.number);
                        boolean isNumberInvalid = kentKart.number.length() != 11;

                        if (isNameEmpty) {
                            String message = "KentKart is invalid, name is empty!";
                            Log.error(this, message);
                            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                        } else if (isNumberEmpty) {
                            String message = "KentKart is invalid, number is empty!";
                            Log.error(this, message);
                            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                        } else if (isNumberInvalid) {
                            String message = "KentKart is invalid, number must be 11 digits!";
                            Log.error(this, message +  " number: " + kentKart.number);
                            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                break;
            case R.id.kentKartInput_delete:    deleteDigit();   break;
            case R.id.kentKartInput_button0:   addDigit(0);     break;
            case R.id.kentKartInput_button1:   addDigit(1);     break;
            case R.id.kentKartInput_button2:   addDigit(2);     break;
            case R.id.kentKartInput_button3:   addDigit(3);     break;
            case R.id.kentKartInput_button4:   addDigit(4);     break;
            case R.id.kentKartInput_button5:   addDigit(5);     break;
            case R.id.kentKartInput_button6:   addDigit(6);     break;
            case R.id.kentKartInput_button7:   addDigit(7);     break;
            case R.id.kentKartInput_button8:   addDigit(8);     break;
            case R.id.kentKartInput_button9:   addDigit(9);     break;
        }
    }
}
