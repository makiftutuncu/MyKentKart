package com.mehmetakiftutuncu.mykentkart.activities;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

import com.mehmetakiftutuncu.mykentkart.R;

public class MyKentKartActivity extends ActionBarActivity {
    protected Toolbar mToolbar;

    protected void initLayout(LayoutInflater layoutInflater, FrameLayout container) {}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.mykentkart_base);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
        }
        FrameLayout container = (FrameLayout) findViewById(R.id.container);
        initLayout(getLayoutInflater(), container);
    }
}
