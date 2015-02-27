package com.mallardduckapps.fashiontalks.adapters;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import com.mallardduckapps.fashiontalks.R;
import com.mallardduckapps.fashiontalks.UsersActivity;
import com.mallardduckapps.fashiontalks.fragments.PopularUsersFragment;

import java.util.Locale;

/**
 * Created by oguzemreozcan on 03/02/15.
 */

    public class UsersPagerAdapter extends FragmentPagerAdapter {

        UsersActivity context;
        public UsersPagerAdapter(FragmentManager fm, UsersActivity context) {
            super(fm);
            this.context = context;
        }

        @Override
        public Fragment getItem(int position) {
            Bundle bundle = new Bundle();
            // getItem is called to instantiate the fragment for the given page.
            switch(position){
                case 0:
                    Log.d("USERS PAGER ADAPTER", "POPULER KULLANICILAR LOAD");
                    return PopularUsersFragment.newInstance("");
                case 1:
                    return new Fragment();
            }

            return new Fragment();
        }

        @Override
        public int getCount() {
            // Show 2 total pages.
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return context.getString(R.string.users_tab1);
                case 1:
                    return context.getString(R.string.users_tab2);
            }
            return null;
        }
    }



