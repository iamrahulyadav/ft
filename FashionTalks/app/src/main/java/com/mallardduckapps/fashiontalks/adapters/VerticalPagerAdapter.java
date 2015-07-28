package com.mallardduckapps.fashiontalks.adapters;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import com.mallardduckapps.fashiontalks.fragments.PostFragment;
import com.mallardduckapps.fashiontalks.objects.Post;
import com.mallardduckapps.fashiontalks.utils.Constants;

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
    LoadMorePostToPager callback;
    int initialSize = 0;
    boolean inLoadEnabled = true;

    public VerticalPagerAdapter(FragmentManager fm, ArrayList<Post> postArrayList, LoadMorePostToPager callback, int loaderId) {
        super(fm);
        //clear();
        this.postArrayList = postArrayList;
        initialSize = postArrayList.size();
        Log.d(TAG, "**POSTS  - VERTICAL PAGER ADAPTER: " + postArrayList.size());
        this.callback = callback;
        //this.positionIndex = positionIndex;
        this.loaderId = loaderId;
        if(loaderId == Constants.GALLERIES_LOADER_ID ){//|| loaderId == Constants.GALLERY_POSTS_LOADER_ID
               // || loaderId == Constants.GALLERY_POSTS_BY_TAG_LOADER_ID){
            inLoadEnabled = false;
        }
    }

    @Override
    public Fragment getItem(int position) {
        PostFragment postFragment = new PostFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("LOADER_ID", loaderId);
        bundle.putInt("POST_ID", postArrayList.get(position).getId());
        bundle.putInt("POST_INDEX", position);
        postFragment.setArguments(bundle);
        Log.d(TAG, "**POSITION : " + position + " - size: " + postArrayList.size());
        if(postArrayList.size() > 0 && inLoadEnabled){
            //TODO perpage 15 for now
            if(position == postArrayList.size() - 1 && initialSize % 15 == 0){
                callback.loadMorePost(position+1,1,loaderId);
            }
        }

        //Log.d(TAG, "Pager data 0 name: " + ( postArrayList.get(0)).getUser().getUserName());
        return postFragment;
    }

    public void addNewItem(Post post){
        Log.d(TAG, "**VERTICAL VIEW ADAPTER ADD NEW POST: " + post.getId());
        postArrayList.add(post);
        this.notifyDataSetChanged();
    }

    public void clear(){

        if(postArrayList != null){
            postArrayList.clear();
        }

    }

    @Override
    public int getCount() {
        if(postArrayList == null){
            return 0;
        }
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

    public interface LoadMorePostToPager{
        void loadMorePost(int position, int count, int loaderId);

    }

}


