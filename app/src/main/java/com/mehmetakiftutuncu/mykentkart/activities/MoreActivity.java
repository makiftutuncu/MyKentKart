package com.mehmetakiftutuncu.mykentkart.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.mehmetakiftutuncu.mykentkart.R;
import com.mehmetakiftutuncu.mykentkart.utilities.Constants;

public class MoreActivity extends PreferenceActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initialize();
        initializeData();
    }

    private void initialize() {
        /* Since AppCompat library doesn't have compat version of PreferenceFragment,
           I had to use PreferenceActivity and therefore these stupid workaround stuff.

           It basically gets the root of an empty layout of a default Activity (not compat stuff),
           adds a Toolbar manually and sets its content (title, navigation etc.). */
        ViewGroup root = (ViewGroup) findViewById(android.R.id.content);
        View content = root.getChildAt(0);
        LinearLayout toolbarContainer = (LinearLayout) View.inflate(this, R.layout.activity_more, null);

        root.removeAllViews();
        toolbarContainer.addView(content);
        root.addView(toolbarContainer);

        Toolbar toolbar = (Toolbar) toolbarContainer.findViewById(R.id.toolbar);
        toolbar.setTitle(getTitle());
        toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToKentKartList();
            }
        });

        addPreferencesFromResource(R.xml.more);
    }

    private void initializeData() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);

        ListPreference listPreference = (ListPreference) findPreference(Constants.PREFERENCE_CONNECTED_TRANSPORT_DURATION);
        listPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                updateConnectedTransportDurationSummary(preference, (String) newValue);
                return false;
            }
        });
        String duration = preferences.getString(Constants.PREFERENCE_CONNECTED_TRANSPORT_DURATION, getString(R.string.moreActivity_connectedTransport_duration_default));
        updateConnectedTransportDurationSummary(listPreference, duration);
    }

    private void updateConnectedTransportDurationSummary(Preference preference, String newValue) {
        preference.setSummary(getString(R.string.moreActivity_connectedTransport_duration_summary, newValue));
    }

    private void goToKentKartList() {
        finish();
    }
}
