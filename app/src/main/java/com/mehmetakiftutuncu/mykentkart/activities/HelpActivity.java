package com.mehmetakiftutuncu.mykentkart.activities;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;

import com.mehmetakiftutuncu.mykentkart.R;
import com.mehmetakiftutuncu.mykentkart.adapters.HelpPageAdapter;
import com.mehmetakiftutuncu.mykentkart.models.HelpPage;
import com.mehmetakiftutuncu.mykentkart.utilities.DepthPageTransformer;

public class HelpActivity extends ActionBarActivity {
    private ViewPager viewPager;
    private HelpPageAdapter helpPageAdapter;

    private HelpPage[] helpPages = new HelpPage[] {
        new HelpPage(R.string.help_page1_title, R.drawable.launcher_icon_big, R.string.help_page1_message),
        new HelpPage(-1, -1, -1)
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_help);

        viewPager = (ViewPager) findViewById(R.id.viewPager_help);
        helpPageAdapter = new HelpPageAdapter(getSupportFragmentManager(), helpPages);
        viewPager.setAdapter(helpPageAdapter);
        viewPager.setPageTransformer(true, new DepthPageTransformer());
    }
}
