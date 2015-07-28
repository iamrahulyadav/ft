package com.mallardduckapps.fashiontalks.fragments;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.mallardduckapps.fashiontalks.BaseActivity;
import com.mallardduckapps.fashiontalks.PostsActivity;
import com.mallardduckapps.fashiontalks.ProfileActivity;
import com.mallardduckapps.fashiontalks.R;
import com.mallardduckapps.fashiontalks.adapters.GalleryGridAdapter;
import com.mallardduckapps.fashiontalks.components.BounceListView;
import com.mallardduckapps.fashiontalks.components.GridListOnScrollListener;
import com.mallardduckapps.fashiontalks.loaders.PopularPostsLoader;
import com.mallardduckapps.fashiontalks.objects.GalleryItem;
import com.mallardduckapps.fashiontalks.objects.Post;
import com.mallardduckapps.fashiontalks.utils.Constants;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class PopularPostsFragment extends BasicFragment implements LoaderManager.LoaderCallbacks<ArrayList<Post>>
        ,GridListOnScrollListener.OnScrolledToBottom, GalleryGridAdapter.PostItemClicked, BounceListView.RefreshListener {

    PopularPostsLoader loader;
    boolean loading;
    private BounceListView listView;
    private ArrayList<GalleryItem> dataList;
    private GalleryGridAdapter listAdapter;
    //private ArrayList<Gallery> items;
    private final int MAX_CARDS = 2;
    protected View loadMoreFooterView;
    int index = 0;
    int itemCountPerLoad = 0;
    int loaderId;
    int galleryId = 0; //Gallery ID if there is one
    //MainActivity activity;
    //String galleryName;
    boolean resetLoader;
    //GalleryGridAdapter.RefreshPagerCallback pagerCallback;

    public PopularPostsFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loaderId = getArguments().getInt("LOADER_ID");
        galleryId = getArguments().getInt("GALLERY_ID");
        //galleryName = getArguments().getString("GALLERY_NAME");
        if(loaderId == Constants.GALLERY_POSTS_BY_TAG_LOADER_ID){
            dataList = null;
        }
        //resetGlobalLists();
        listAdapter = new GalleryGridAdapter(getActivity(),this,MAX_CARDS,  galleryId == 0 ? false : true ); // galleryName == null
        //Log.d(TAG, "POPULAR POSTS FRAGMENT-LoaderId: " + loaderId + " - GalleryId: " + galleryId);
        useLoader();
    }


    @Override
    public void setTag() {
        TAG = new StringBuilder("POPULAR_POSTS_Fragment").append(loaderId).append(galleryId).toString();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try{
            //pagerCallback = (GalleryGridAdapter.RefreshPagerCallback) activity;
        }catch(ClassCastException e){
            e.printStackTrace();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_gallery, container, false);
        listView = (BounceListView) rootView.findViewById(R.id.galleryList);
        listView.setOnScrollListener(new GridListOnScrollListener(this));
        listView.setRefreshListener(this);
        if(dataList != null){
            listAdapter.clearList();
            listAdapter.addItemsInGrid(dataList);
            listView.setAdapter(listAdapter);
        }

        loadMoreFooterView = getLoadMoreView(inflater);
        sendEventToGoogleAnalytics();
        return rootView;
    }

    private void sendEventToGoogleAnalytics(){
        if(loaderId == Constants.FEED_POSTS_LOADER_ID){
            app.sendAnalyticsEvent("Followed Looks View", "UX", "look","");
            app.sendAnalyticsEvent("Look List View", "UX", "look", "");
        }else if(loaderId == Constants.POPULAR_POSTS_LOADER_ID){
            app.sendAnalyticsEvent("Popular Styles View", "UX", "look","");
            app.sendAnalyticsEvent("Look List View", "UX", "look","");
        }else{
            if(galleryId != 0){
                app.sendAnalyticsEvent("Gallery Detail View", "UX", "look",galleryId + "");
            }
        }
    }

    public void useLoader() {
        if (this.canLoadMoreData() && !loading) {
           // Log.d(TAG, "USE LOADER FRAGMENT POPULAR POSTS");
            loading = true;
            if(loader == null && !resetLoader){
                loader = (PopularPostsLoader) getActivity().getLoaderManager()
                        .initLoader(loaderId, null, this);
                loader.forceLoad();

            }else if(loader == null && resetLoader){
                loader = (PopularPostsLoader) getActivity().getLoaderManager()
                        .restartLoader(loaderId, null, this);
                loader.forceLoad();
            }
            else{
                Log.d(TAG, "**LOADER NOT NULL: startIndex" + loader.startIndex);
                //Log.d(TAG, "USE LOADER FRAGMENT POPULAR POSTS - ON CONTENT CHANGEDD");
                //loader.startLoading(); //= (PopularPostsLoader) getActivity().getLoaderManager()
                       // .restartLoader(Constants.POPULAR_POSTS_LOADER_ID, null, this);
               // loader.onContentChanged();
                loader.forceLoad();
            }
            calculateLoadValues();
        }else{
            try{
                listView.removeFooterView(loadMoreFooterView);
            }catch(Exception e){

            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        if(ProfileActivity.imageGalleryChanged){
            Log.d(TAG, "ON RESUME - IMAGE GALLERY CHANGED");
            onRefreshList();
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    ProfileActivity.imageGalleryChanged = false;
                    Log.d(TAG, "IMAGE GALLERY CHANGED FALSE");
                }
            }, 500);
        }
    }

    @Override
    public Loader<ArrayList<Post>> onCreateLoader(int id, Bundle args) {
        loader = new PopularPostsLoader(getActivity().getApplicationContext(), id, galleryId);
        return loader;
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<Post>> loader, final ArrayList<Post> data) {
        if(data == null){
            //Log.d(TAG, "DATA IS NULL");
            Handler handler = new Handler();
            handler.post(new Runnable() {
                @Override
                public void run() {
                    app.openOKDialog(PopularPostsFragment.this.getActivity(), PopularPostsFragment.this, "no_connection");
                }
            });
            return;
        }

        Log.d(TAG, "ON LOAD FINISHED: " + data.size() + " - galleryId: " + galleryId);
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                itemCountPerLoad = 0;
                if(dataList == null){
                    index = 0;
                    loadData(data);
                    if(listView != null)
                        listView.setAdapter(listAdapter);
                    if(canLoadMoreData()){
                        listView.addFooterView(loadMoreFooterView);
                        loadMoreFooterView.setVisibility(View.VISIBLE);
                    }
                }else{
                    Log.d(TAG, "LOAD MORE DATA TO THE ADAPTER: ");
                    loadData(data);
                    listAdapter.notifyDataSetChanged();
                }
                if(resetLoader){
                    resetLoader = false;
                }
                if(!canLoadMoreData()){
                    if(listView != null) {
                        listView.removeFooterView(loadMoreFooterView);
                        loadMoreFooterView.setVisibility(View.INVISIBLE);
                    }
                }

                loading = false;
            }
        });

    }

    private void loadData(ArrayList<Post> data){
        dataList = new ArrayList<GalleryItem>();
        for (Post post : data){
            //ImagePathTask task = new ImagePathTask("galleries/1419693538.203064jpg");
            //String path = new StringBuilder(Constants.CLOUD_FRONT_URL).append("/300x300/").append(post.getPhoto()).toString();
            if(post!=null){
                GalleryItem galleryItem = new GalleryItem(index, post.getId(), "", post.getPhoto());
                //Log.d(TAG, "DATA PHOTO URL: " + post.getPhoto());
                index ++;
                dataList.add(galleryItem);
            }
        }
        itemCountPerLoad = data.size();
        listAdapter.addItemsInGrid(dataList);
        addToGlobalLists(data);
    }

    private void resetGlobalLists(){
        Log.d(TAG, "ON RESET GLOBAL LISTS");
        switch (loaderId){
            case Constants.FEED_POSTS_LOADER_ID:
                app.setFeedPostArrayList(null);
                break;
            case Constants.POPULAR_POSTS_LOADER_ID:
                app.setPopularPostArrayList(null);
                break;
            case Constants.GALLERY_POSTS_LOADER_ID:
                app.setGalleryPostArrayList(null);
                break;
            case Constants.GALLERY_POSTS_BY_TAG_LOADER_ID:
                app.setBrandGalleryPostList(null);
                break;
        }
    }

    private void addToGlobalLists(ArrayList<Post> data){
        switch (loaderId){
            case Constants.FEED_POSTS_LOADER_ID:
                app.addFeedPostArrayList(data);
                break;
            case Constants.POPULAR_POSTS_LOADER_ID:
                app.addPopularPostArrayList(data);
                break;
            case Constants.GALLERY_POSTS_LOADER_ID:
                //Log.d(TAG, "FILL GALLERY POSTS LIST: " +loaderId + " - " + data.size() + " - data 0 name : " + data.get(0).getUser().getUserName());
                if(data != null ){ // && galleryName != null
                    app.addGalleryPostArrayList(data, galleryId);
                    app.lastGalleryId = galleryId;
                }else{
                    app.setGalleryPostArrayList(null);
                }
                break;
            case Constants.GALLERY_POSTS_BY_TAG_LOADER_ID:
                if(data != null){
                    app.addBrandGalleryPostList(data, galleryId);
                    app.lastBrandId = galleryId;
                }else{
                    app.setBrandGalleryPostList(null);
                }
                break;
        }
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<Post>> loader) {
        dataList = null;
        calculateLoadValues();
        resetGlobalLists();
        Log.d(TAG, "ON LOADER RESET");
        index = 0;
        loading = false;
    }

    @Override
    public void reachedToEnd() {
        //Load More Data
        loadMoreFooterView.setVisibility(View.VISIBLE);
        Log.d(TAG, "ON REACHED TO END");
        useLoader();
    }

    public boolean canLoadMoreData() {
        if(loader == null)
            return true;
        Log.d(TAG, "CAN LOAD MORE DATA ** " + loader.perPage + "- itemCountPerLoad : " + itemCountPerLoad);
        return loader.perPage > itemCountPerLoad  ? false : true;//listData.size() < getMaxAllowedItems();
    }

    public void calculateLoadValues(){

        if(dataList == null){
            loader.startIndex = 0;
        }else{
            loader.startIndex += loader.perPage;
        }
        Log.d(TAG, "CALCULATE LOAD MORE VALUES ** : " + loader.startIndex);
    }

    @SuppressLint("InflateParams")
    protected View getLoadMoreView(LayoutInflater inflater) {
        ProgressBar loadMoreProgress = (ProgressBar) inflater.inflate(
                R.layout.auto_load_more_view, null);
        //loadMoreProgress.setBackgroundColor(Color.LTGRAY);
        return loadMoreProgress;
    }

    @Override
    public void postOnItemClicked(int postId, int postItemPosition) {
        //if(pagerCallback != null){
         //   Log.e(TAG, "REFRESH PAGER !!!");
         //   pagerCallback.refreshPager(true);
        //}
        if(getActivity() instanceof PostsActivity){
            PostsActivity activity = (PostsActivity)getActivity();
            activity.openPostFragment(postId,postItemPosition,loaderId, true);
        } else {
            Log.d(TAG, "POST ACTIVITY OPENED BY GALLERY: " + loaderId + " -postId: " + postId + " - position: " + postItemPosition);
            Intent intent = new Intent(getActivity(), PostsActivity.class);
            intent.putExtra("LOADER_ID", loaderId);
            intent.putExtra("POST_ID", postId);
            intent.putExtra("POST_INDEX", postItemPosition);
            //TODO COntrol
            //intent.putExtra("GALLERY_ID", galleryId);
            app.setGalleryId(galleryId);
            //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            this.startActivity(intent);
            BaseActivity.setTranslateAnimation(getActivity());
            //getActivity().overridePendingTransition(setTranslateAnimation(BaseActivity.this);, R.anim.activity_close_translate);
        }
    }

    @Override
    public void onRefreshList() {
        if(resetLoader && loading){
            return;
        }
        Log.d(TAG, "ON REFRESH LIST");
        dataList = null;
        //calculateLoadValues();
        resetGlobalLists();
        Log.d(TAG, "ON LOADER RESET");
        index = 0;
        loading = false;

        loader = null;
        listAdapter = new GalleryGridAdapter(getActivity(),this,MAX_CARDS, galleryId == 0 ? false : true );
        resetLoader = true;
        useLoader();
    }
}
