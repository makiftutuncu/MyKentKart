package com.mehmetakiftutuncu.mykentkart.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.mehmetakiftutuncu.mykentkart.fragments.HelpPageFragment;
import com.mehmetakiftutuncu.mykentkart.models.HelpPage;

public class HelpPageAdapter extends FragmentStatePagerAdapter {
    private HelpPageFragment[] helpPageFragments;

    public HelpPageAdapter(FragmentManager fragmentManager, HelpPage[] helpPages) {
        super(fragmentManager);

        this.helpPageFragments = new HelpPageFragment[helpPages.length];
        for (int i = 0; i < helpPageFragments.length; i++) {
            HelpPage helpPage = helpPages[i];

            helpPageFragments[i] = HelpPageFragment.with(helpPage.titleResourceId, helpPage.imageResourceId, helpPage.messageResourceId);
        }
    }

    @Override
    public Fragment getItem(int position) {
        return helpPageFragments != null ? helpPageFragments[position] : null;
    }

    @Override
    public int getCount() {
        return helpPageFragments != null ? helpPageFragments.length : -1;
    }
}
