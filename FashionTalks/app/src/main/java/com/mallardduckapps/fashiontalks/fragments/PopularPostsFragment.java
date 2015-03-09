package com.mallardduckapps.fashiontalks.fragments;


import android.annotation.SuppressLint;
import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.mallardduckapps.fashiontalks.PostsActivity;
import com.mallardduckapps.fashiontalks.R;
import com.mallardduckapps.fashiontalks.adapters.GalleryGridAdapter;
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
        ,GridListOnScrollListener.OnScrolledToBottom, GalleryGridAdapter.PostItemClicked {

    PopularPostsLoader loader;
    boolean loading;
    private ListView listView;
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

    public PopularPostsFragment() {
        // Required empty public constructor
        
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loaderId = getArguments().getInt("LOADER_ID");
        galleryId = getArguments().getInt("GALLERY_ID");
        listAdapter = new GalleryGridAdapter(getActivity(),this,MAX_CARDS, galleryId == 0 ? false : true );
        Log.d(TAG, "POPULAR POSTS FRAGMENT-LoaderId: " + loaderId + " - GalleryId: " + galleryId);
        useLoader();
    }

    @Override
    public void setTag() {
        TAG = new StringBuilder("POPULAR_POSTS_Fragment").append(loaderId).append(galleryId).toString();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_gallery, container, false);
        listView = (ListView) rootView.findViewById(R.id.galleryList);
        listView.setOnScrollListener(new GridListOnScrollListener(this));
        if(dataList != null){
            listAdapter.addItemsInGrid(dataList);
            listView.setAdapter(listAdapter);
        }
        loadMoreFooterView =getLoadMoreView(inflater);
        return rootView;
    }

    public void useLoader() {
        if (this.canLoadMoreData() && !loading) {
            Log.d(TAG, "USE LOADER FRAGMENT POPULAR POSTS");
            loading = true;
            if(loader == null ){
                loader = (PopularPostsLoader) getActivity().getLoaderManager()
                        .initLoader(loaderId, null, this);
                loader.forceLoad();

            }else{
                Log.d(TAG, "USE LOADER FRAGMENT POPULAR POSTS - ON CONTENT CHANGEDD");
                //loader.startLoading(); //= (PopularPostsLoader) getActivity().getLoaderManager()
                       // .restartLoader(Constants.POPULAR_POSTS_LOADER_ID, null, this);
               // loader.onContentChanged();
                loader.forceLoad();
            }
            calculateLoadValues();
        }else{
            listView.removeFooterView(loadMoreFooterView);
        }
    }

    @Override
    public Loader<ArrayList<Post>> onCreateLoader(int id, Bundle args) {
        loader = new PopularPostsLoader(getActivity().getApplicationContext(), id, galleryId);
        return loader;
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<Post>> loader, ArrayList<Post> data) {
        Log.d(TAG, "ON LOAD FINISHED: " + data.size() + " - galleryId: " + galleryId);
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
            //Log.d(TAG, "LOAD MORE DATA TO THE ADAPTER: ");
            loadData(data);
            listAdapter.notifyDataSetChanged();
        }

        if(!canLoadMoreData()){
            if(listView != null) {

                listView.removeFooterView(loadMoreFooterView);
                loadMoreFooterView.setVisibility(View.INVISIBLE);
            }
        }

        loading = false;
    }

    private void loadData(ArrayList<Post> data){
        dataList = new ArrayList<GalleryItem>();
        for (Post post : data){
            //Log.d(TAG, "COVER PATH: " + post.getPhoto());
            //ImagePathTask task = new ImagePathTask("galleries/1419693538.203064jpg");
            //String path = new StringBuilder(Constants.CLOUD_FRONT_URL).append("/300x300/").append(post.getPhoto()).toString();
            GalleryItem galleryItem = new GalleryItem(index, post.getId(), "", post.getPhoto());
           Log.d(TAG, "DATA PHOTO URL: " + post.getPhoto());
            index ++;
            dataList.add(galleryItem);
        }
        itemCountPerLoad = data.size();
        listAdapter.addItemsInGrid(dataList);
        addToGlobalLists(data);
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
                if(data != null){
                    app.addGalleryPostArrayList(data);
                }else{
                    app.setGalleryPostArrayList(null);
                }
                break;
        }
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<Post>> loader) {
        dataList = null;
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
        return loader.perPage > itemCountPerLoad  ? false : true;//listData.size() < getMaxAllowedItems();
    }

    public void calculateLoadValues(){
        if(dataList == null){
            loader.startIndex = 0;
        }else{
            loader.startIndex += loader.perPage;
        }
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
        Log.d(TAG, "SEND GALLERY ID TO ACTIVITY: " + galleryId);
        Log.d(TAG, "SEND POST ID TO ACTIVITY: " + postId);

        if(getActivity() instanceof PostsActivity){
            PostsActivity activity = (PostsActivity)getActivity();
            activity.openPostFragment(postId,postItemPosition,loaderId, true);
        }else{
            Intent intent = new Intent(getActivity(), PostsActivity.class);
            intent.putExtra("LOADER_ID", loaderId);
            intent.putExtra("POST_ID", postId);
            intent.putExtra("POST_INDEX", postItemPosition);
            //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            this.startActivity(intent);
            getActivity().overridePendingTransition(R.anim.activity_open_scale, R.anim.activity_close_translate);
        }

    }
}
