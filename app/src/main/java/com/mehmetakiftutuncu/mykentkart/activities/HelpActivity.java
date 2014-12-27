package com.mehmetakiftutuncu.mykentkart.activities;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;

import com.mehmetakiftutuncu.mykentkart.R;
import com.mehmetakiftutuncu.mykentkart.adapters.HelpPageAdapter;
import com.mehmetakiftutuncu.mykentkart.models.HelpPage;
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
