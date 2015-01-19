package com.mallardduckapps.fashiontalks.adapters;

/**
 * Created by oguzemreozcan on 11/01/15.
 */

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.mallardduckapps.fashiontalks.R;
import com.mallardduckapps.fashiontalks.fragments.GalleriesFragment;
import com.mallardduckapps.fashiontalks.fragments.PhotoShowFragment;
import com.mallardduckapps.fashiontalks.fragments.PlaceHolderFragment;
import com.mallardduckapps.fashiontalks.fragments.PopularPostsFragment;

import java.util.Locale;

/**
 * A {@link android.support.v4.app.FragmentPagerAdapter} that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

    Context context;
    public SectionsPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        switch(position){
            case 0:
                return PhotoShowFragment.newInstance(position + 1);
            case 1:
                return new PopularPostsFragment();
            case 2:
                return new GalleriesFragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        // Show 3 total pages.
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        Locale l = Locale.getDefault();
        switch (position) {
            case 0:
                return context.getString(R.string.tab1);
            case 1:
                return context.getString(R.string.tab2);
            case 2:
                return context.getString(R.string.tab3);
        }
        return null;
    }
}

