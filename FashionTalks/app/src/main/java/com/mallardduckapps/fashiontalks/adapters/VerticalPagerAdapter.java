package com.mallardduckapps.fashiontalks.adapters;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import com.mallardduckapps.fashiontalks.fragments.PostFragment;
import com.mallardduckapps.fashiontalks.objects.Post;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by oguzemreozcan on 01/02/15.
 */
public class VerticalPagerAdapter extends FragmentPagerAdapter {

    ArrayList<Post> postArrayList;
    //int positionIndex;
    int loaderId;
    private final static String TAG = "VERTICAL_PAGER_ADAPTER";

    public VerticalPagerAdapter(FragmentManager fm, ArrayList<Post> postArrayList, int loaderId) {
        super(fm);
        this.postArrayList = postArrayList;
        //this.positionIndex = positionIndex;
        this.loaderId = loaderId;
    }

    @Override
    public Fragment getItem(int position) {
        PostFragment postFragment = new PostFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("LOADER_ID", loaderId);
        bundle.putInt("POST_ID", postArrayList.get(position).getId());
        bundle.putInt("POST_INDEX", position);
        postFragment.setArguments(bundle);
        Log.d(TAG, "Pager data 0 name: " + ( postArrayList.get(0)).getUser().getUserName());
        return postFragment;
    }

    @Override
    public int getCount() {
        return postArrayList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        Locale l = Locale.getDefault();
        switch (position) {
            case 0:
                return "PAGE 1";
            case 1:
                return "PAGE 2";
            case 2:
                return "PAGE 3";
        }
        return null;
    }

}


