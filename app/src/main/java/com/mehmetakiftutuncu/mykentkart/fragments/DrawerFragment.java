package com.mehmetakiftutuncu.mykentkart.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.mehmetakiftutuncu.mykentkart.R;
import com.mehmetakiftutuncu.mykentkart.activities.PreferencesActivity;


public class DrawerFragment extends Fragment implements View.OnClickListener {
    private Button rate;
    private Button feedback;
    private Button about;
    private Button preferences;
    private Button help;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_drawer, container, false);

        rate = (Button) layout.findViewById(R.id.button_drawer_rate);
        feedback = (Button) layout.findViewById(R.id.button_drawer_feedback);
        about = (Button) layout.findViewById(R.id.button_drawer_about);
        preferences = (Button) layout.findViewById(R.id.button_drawer_preferences);
        help = (Button) layout.findViewById(R.id.button_drawer_help);

        rate.setOnClickListener(this);
        feedback.setOnClickListener(this);
        about.setOnClickListener(this);
        preferences.setOnClickListener(this);
        help.setOnClickListener(this);

        return layout;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_drawer_rate:
                break;

            case R.id.button_drawer_feedback:
                break;

            case R.id.button_drawer_about:
                break;

            case R.id.button_drawer_preferences:
                Intent intent = new Intent(getActivity(), PreferencesActivity.class);
                startActivity(intent);
                break;

            case R.id.button_drawer_help:
                break;
        }
    }
}
