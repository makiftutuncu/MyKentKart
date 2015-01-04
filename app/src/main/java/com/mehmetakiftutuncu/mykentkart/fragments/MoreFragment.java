/*
 * Copyright (C) 2015 Mehmet Akif Tütüncü
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
import com.mehmetakiftutuncu.mykentkart.activities.HelpActivity;
import com.mehmetakiftutuncu.mykentkart.utilities.Constants;
import com.mehmetakiftutuncu.mykentkart.utilities.Log;

import de.psdev.licensesdialog.LicensesDialog;
import de.psdev.licensesdialog.licenses.ApacheSoftwareLicense20;
import de.psdev.licensesdialog.licenses.MITLicense;
import de.psdev.licensesdialog.model.Notice;
import de.psdev.licensesdialog.model.Notices;

/**
 * A {@link android.preference.PreferenceFragment} to show contents of
 * {@link com.mehmetakiftutuncu.mykentkart.activities.MoreActivity}
 *
 * @author mehmetakiftutuncu
 */
public class MoreFragment extends PreferenceFragment {
    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.more);

        initializeData();
    }

    /**
     * A utility method to initialize preferences and about data
     */
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
                startActivity(Intent.createChooser(intent, getString(R.string.moreActivity_about_rate)));
                return true;
            }
        });

        Preference help = findPreference(Constants.PREFERENCE_ABOUT_HELP);
        help.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Intent intent = new Intent(getActivity(), HelpActivity.class);
                intent.putExtra(Constants.HELP_STARTED_MANUALLY, true);
                startActivity(intent);
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
                startActivity(Intent.createChooser(intent, getString(R.string.moreActivity_about_feedback)));
                return true;
            }
        });

        Preference version = findPreference(Constants.PREFERENCE_ABOUT_VERSION);

        Preference licenses = findPreference(Constants.PREFERENCE_ABOUT_LICENSES);
        licenses.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Notices notices = new Notices();
                notices.addNotice(new Notice("Android Form EditText", "https://github.com/vekexasia/android-edittext-validator", "", new MITLicense()));
                notices.addNotice(new Notice("Google Gson", "https://code.google.com/p/google-gson", "", new ApacheSoftwareLicense20()));
                notices.addNotice(new Notice("Android-ProgressSwitcher", "https://github.com/Drnkn/Android-ProgressSwitcher", "", new ApacheSoftwareLicense20()));
                notices.addNotice(new Notice("FloatingActionButton", "https://github.com/makovkastar/FloatingActionButton", "", new MITLicense()));
                notices.addNotice(new Notice("Material-ish Progress", "https://github.com/pnikosis/materialish-progress", "", new ApacheSoftwareLicense20()));
                notices.addNotice(new Notice("Discreet App Rate", "https://github.com/PomepuyN/discreet-app-rate", "", new ApacheSoftwareLicense20()));

                new LicensesDialog.Builder(getActivity())
                    .setNotices(notices)
                    .setIncludeOwnLicense(true)
                    .build()
                    .show();
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

    /**
     * A utility method update summary text of connected transport preference to show current value
     *
     * @param preference A reference to {@link android.preference.ListPreference} whose summary will be updated
     * @param newValue   New value of connected transport duration to set
     */
    private void updateConnectedTransportDurationSummary(Preference preference, String newValue) {
        preference.setSummary(getString(R.string.moreActivity_connectedTransport_duration_summary, newValue));
    }
}
