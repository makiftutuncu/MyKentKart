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
package com.mehmetakiftutuncu.mykentkart.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;

import com.mehmetakiftutuncu.mykentkart.R;
import com.mehmetakiftutuncu.mykentkart.adapters.HelpPageAdapter;
import com.mehmetakiftutuncu.mykentkart.models.HelpPage;
import com.mehmetakiftutuncu.mykentkart.utilities.Constants;
import com.mehmetakiftutuncu.mykentkart.utilities.DepthPageTransformer;
import com.mehmetakiftutuncu.mykentkart.utilities.NFCUtils;

public class HelpActivity extends ActionBarActivity {
    private HelpPage[] helpPages = new HelpPage[] {
        new HelpPage(R.string.help_page1_title, R.drawable.help_logo, R.string.help_page1_message),
        new HelpPage(R.string.help_page2_title, R.drawable.help_kentkart, R.string.help_page2_message),
        new HelpPage(R.string.help_page3_title, R.drawable.nfc_big, R.string.help_page3_message),
        new HelpPage(R.string.help_page4_title, R.drawable.help_add, R.string.help_page4_message),
        new HelpPage(R.string.help_page5_title, R.drawable.help_information, R.string.help_page5_message),
        new HelpPage(R.string.help_page6_title, R.drawable.help_connectedtransport, R.string.help_page6_message),
        new HelpPage(R.string.help_page7_title, R.drawable.help_edit, R.string.help_page7_message),
        new HelpPage(R.string.help_page8_title, R.drawable.help_more, R.string.help_page8_message),
        new HelpPage(-1, -1, -1)
    };

    private HelpPage[] helpPagesWithoutNfc = new HelpPage[] {
        new HelpPage(R.string.help_page1_title, R.drawable.help_logo, R.string.help_page1_message),
        new HelpPage(R.string.help_page2_title, R.drawable.help_kentkart, R.string.help_page2_message),
        new HelpPage(R.string.help_page4_title, R.drawable.help_add, R.string.help_page4_message),
        new HelpPage(R.string.help_page5_title, R.drawable.help_information, R.string.help_page5_message),
        new HelpPage(R.string.help_page6_title, R.drawable.help_connectedtransport, R.string.help_page6_message),
        new HelpPage(R.string.help_page7_title, R.drawable.help_edit, R.string.help_page7_message),
        new HelpPage(R.string.help_page8_title, R.drawable.help_more, R.string.help_page8_message),
        new HelpPage(-1, -1, -1)
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_help);

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager_help);
        final HelpPage[] pages = NFCUtils.get(this).hasNfc() ? helpPages : helpPagesWithoutNfc;
        HelpPageAdapter helpPageAdapter = new HelpPageAdapter(getSupportFragmentManager(), pages);
        viewPager.setAdapter(helpPageAdapter);
        viewPager.setPageTransformer(true, new DepthPageTransformer());
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (position == pages.length - 1) {
                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(HelpActivity.this);
                    preferences.edit().putBoolean(Constants.PREFERENCE_IS_HELP_COMPLETED, true).apply();
                    Intent intent = new Intent(HelpActivity.this, KentKartListActivity.class);
                    startActivity(intent);
                    finish();
                }
            }

            @Override
            public void onPageSelected(int position) {}

            @Override
            public void onPageScrollStateChanged(int state) {}
        });
    }
}
