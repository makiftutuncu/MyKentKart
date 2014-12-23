package com.mehmetakiftutuncu.mykentkart.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;

import com.mehmetakiftutuncu.mykentkart.R;
import com.mehmetakiftutuncu.mykentkart.utilities.Constants;
import com.mehmetakiftutuncu.mykentkart.utilities.Log;

public class MoreFragment extends PreferenceFragment {
    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.more);

        initializeData();
    }

    private void initializeData() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());

        ListPreference connectedTransportDurations = (ListPreference) findPreference(Constants.PREFERENCE_CONNECTED_TRANSPORT_DURATION);
        connectedTransportDurations.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                updateConnectedTransportDurationSummary(preference, (String) newValue);
                return true;
            }
        });

        Preference rate = findPreference(Constants.PREFERENCE_ABOUT_RATE);
        rate.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(Constants.RATE_URI));
                startActivity(intent);
                return true;
            }
        });

        Preference help = findPreference(Constants.PREFERENCE_ABOUT_HELP);
        help.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                return true;
            }
        });

        Preference feedback = findPreference(Constants.PREFERENCE_ABOUT_FEEDBACK);
        feedback.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("message/rfc822");
                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{Constants.FEEDBACK_CONTACT});
                intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.feedback_subject));
                startActivity(intent);
                return true;
            }
        });

        Preference version = findPreference(Constants.PREFERENCE_ABOUT_VERSION);

        Preference licenses = findPreference(Constants.PREFERENCE_ABOUT_LICENSES);
        licenses.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                return true;
            }
        });

        String duration = preferences.getString(Constants.PREFERENCE_CONNECTED_TRANSPORT_DURATION, getString(R.string.moreActivity_connectedTransport_duration_default));
        updateConnectedTransportDurationSummary(connectedTransportDurations, duration);

        try {
            String versionName = getActivity().getPackageManager().getPackageInfo(Constants.PACKAGE_NAME, 0).versionName;
            version.setSummary(versionName);
        } catch (PackageManager.NameNotFoundException e) {
            Log.error(this, "Failed to get package info!", e);
        }
    }

    private void updateConnectedTransportDurationSummary(Preference preference, String newValue) {
        preference.setSummary(getString(R.string.moreActivity_connectedTransport_duration_summary, newValue));
    }
}
