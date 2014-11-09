package com.mehmetakiftutuncu.mykentkart.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.mehmetakiftutuncu.mykentkart.R;

public class KentKartNumberInputDialogFragment extends DialogFragment implements View.OnClickListener {
    private TextView valueTextView;
    private String valueString = "";
    private OnKentKartNumberInputListener onKentKartNumberInputListener;

    public static interface OnKentKartNumberInputListener {
        public void onKentKartNumberInput(String value);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        View header = layoutInflater.inflate(R.layout.kentkart_number_input_header, null, false);
        View layout = layoutInflater.inflate(R.layout.kentkart_number_input, null, false);

        valueTextView = (TextView) layout.findViewById(R.id.kentKartNumberInput_value);
        ImageButton acceptButton = (ImageButton) header.findViewById(R.id.kentKartNumberInput_accept);
        ImageButton deleteButton = (ImageButton) layout.findViewById(R.id.kentKartNumberInput_delete);
        Button button0 = (Button) layout.findViewById(R.id.kentKartNumberInput_button0);
        Button button1 = (Button) layout.findViewById(R.id.kentKartNumberInput_button1);
        Button button2 = (Button) layout.findViewById(R.id.kentKartNumberInput_button2);
        Button button3 = (Button) layout.findViewById(R.id.kentKartNumberInput_button3);
        Button button4 = (Button) layout.findViewById(R.id.kentKartNumberInput_button4);
        Button button5 = (Button) layout.findViewById(R.id.kentKartNumberInput_button5);
        Button button6 = (Button) layout.findViewById(R.id.kentKartNumberInput_button6);
        Button button7 = (Button) layout.findViewById(R.id.kentKartNumberInput_button7);
        Button button8 = (Button) layout.findViewById(R.id.kentKartNumberInput_button8);
        Button button9 = (Button) layout.findViewById(R.id.kentKartNumberInput_button9);

        acceptButton.setOnClickListener(this);
        deleteButton.setOnClickListener(this);
        deleteButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                valueString = "";
                updateValue();
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

        updateValue();

        return dialog;
    }

    private void addDigit(int digit) {
        if(digit < 0 || digit > 9) return;

        if(valueString.length() < 11) {
            valueString += digit;
            updateValue();
        }
    }

    private void deleteDigit() {
        if(valueString.length() > 0) {
            valueString = valueString.substring(0, valueString.length() - 1);
            updateValue();
        }
    }

    private void updateValue() {
        int length = valueString.length();

        if (length >= 0 && length < 6) {
            // Until first 5 digits
            valueTextView.setText(valueString);
        } else if (length >= 6 && length < 11) {
            // After first 5 digits
            String first5 = valueString.substring(0, 5);
            String rest = valueString.substring(5, length);

            valueTextView.setText(first5 + "-" + rest);
        } else if (length >= 11) {
            // After first 10 digits
            String first5 = valueString.substring(0, 5);
            String second5 = valueString.substring(5, 10);
            String rest = valueString.substring(10, length);

            valueTextView.setText(first5 + "-" + second5 + "-" + rest);
        }
    }

    public void setOnKentKartNumberInputListener(OnKentKartNumberInputListener onKentKartNumberInputListener) {
        this.onKentKartNumberInputListener = onKentKartNumberInputListener;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.kentKartNumberInput_accept:
                if(onKentKartNumberInputListener != null) {
                    if (valueString.length() == 11) {
                        onKentKartNumberInputListener.onKentKartNumberInput(valueString);
                        dismiss();
                    }
                }
                break;
            case R.id.kentKartNumberInput_delete:    deleteDigit();   break;
            case R.id.kentKartNumberInput_button0:   addDigit(0);     break;
            case R.id.kentKartNumberInput_button1:   addDigit(1);     break;
            case R.id.kentKartNumberInput_button2:   addDigit(2);     break;
            case R.id.kentKartNumberInput_button3:   addDigit(3);     break;
            case R.id.kentKartNumberInput_button4:   addDigit(4);     break;
            case R.id.kentKartNumberInput_button5:   addDigit(5);     break;
            case R.id.kentKartNumberInput_button6:   addDigit(6);     break;
            case R.id.kentKartNumberInput_button7:   addDigit(7);     break;
            case R.id.kentKartNumberInput_button8:   addDigit(8);     break;
            case R.id.kentKartNumberInput_button9:   addDigit(9);     break;
        }
    }
}
