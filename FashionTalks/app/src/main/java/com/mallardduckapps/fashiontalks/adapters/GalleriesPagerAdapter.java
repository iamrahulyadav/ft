package com.mallardduckapps.fashiontalks.adapters;

/**
 * Created by oguzemreozcan on 11/01/15.
 */

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.mallardduckapps.fashiontalks.BaseActivity;
import com.mallardduckapps.fashiontalks.R;
import com.mallardduckapps.fashiontalks.fragments.GalleriesFragment;
import com.mallardduckapps.fashiontalks.fragments.PopularPostsFragment;
import com.mallardduckapps.fashiontalks.utils.Constants;

import java.util.Locale;

/**
 * A {@link android.support.v4.app.FragmentPagerAdapter} that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class GalleriesPagerAdapter extends FragmentPagerAdapter {

    BaseActivity context;
    public GalleriesPagerAdapter(FragmentManager fm, BaseActivity context) {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        Bundle bundle = new Bundle();
        // getItem is called to instantiate the fragment for the given page.
        switch(position){
            case 0:
                PopularPostsFragment feedFragment = new PopularPostsFragment();
                bundle.putInt("LOADER_ID", Constants.FEED_POSTS_LOADER_ID);
                feedFragment.setArguments(bundle);
                //feedFragment.setActivity(context);
                return feedFragment;
            case 1:
                PopularPostsFragment popularFragment = new PopularPostsFragment();
                bundle.putInt("LOADER_ID", Constants.POPULAR_POSTS_LOADER_ID);
                popularFragment.setArguments(bundle);
               // popularFragment.setActivity(context);
                return popularFragment;
            case 2:
                GalleriesFragment gFragment = new GalleriesFragment();
                //gFragment.setActivity(context);
                return gFragment;
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
                return context.getString(R.string.main_tab1);
            case 1:
                return context.getString(R.string.main_tab2);
            case 2:
                return context.getString(R.string.main_tab3);
        }
        return null;
    }
}

