package com.mehmetakiftutuncu.mykentkart.activities;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

import com.mehmetakiftutuncu.mykentkart.R;

public class MainActivity extends MyKentKartActivity {
    @Override
    protected void initLayout(LayoutInflater layoutInflater, FrameLayout container) {
        layoutInflater.inflate(R.layout.activity_main, container);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
