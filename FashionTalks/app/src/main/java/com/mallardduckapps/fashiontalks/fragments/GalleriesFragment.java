package com.mallardduckapps.fashiontalks.fragments;


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
import android.widget.ListView;

import com.mallardduckapps.fashiontalks.BaseActivity;
import com.mallardduckapps.fashiontalks.GalleryActivity;
import com.mallardduckapps.fashiontalks.R;
import com.mallardduckapps.fashiontalks.adapters.GalleryGridAdapter;
import com.mallardduckapps.fashiontalks.components.BounceListView;
import com.mallardduckapps.fashiontalks.loaders.GalleriesLoader;
import com.mallardduckapps.fashiontalks.objects.Gallery;
import com.mallardduckapps.fashiontalks.objects.GalleryItem;
import com.mallardduckapps.fashiontalks.utils.Constants;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GalleriesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GalleriesFragment extends BasicFragment implements
        LoaderManager.LoaderCallbacks<ArrayList<Gallery>>, GalleryGridAdapter.GalleryItemClicked {

    GalleriesLoader loader;
    boolean loading;
    //private final String TAG = "GalleriesFragment";
//    private ListView listView;
    private BounceListView listView;
    private ArrayList<GalleryItem> dataList;
    private GalleryGridAdapter listAdapter;
    private ArrayList<Gallery> items;
    //private MainActivity activity;

    private final int MAX_CARDS = 2;

    public static GalleriesFragment newInstance() {
        GalleriesFragment fragment = new GalleriesFragment();
        return fragment;
    }

    public GalleriesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        listAdapter = new GalleryGridAdapter(getActivity(), this, MAX_CARDS, true);
        useLoader();
    }

    @Override
    public void setTag() {
        TAG = "GalleriesFragment";
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_gallery, container, false);
        listView = (BounceListView) rootView.findViewById(R.id.galleryList);
        if(dataList != null){
            listAdapter.addItemsInGrid(dataList);
            listView.setAdapter(listAdapter);
        }

        return rootView;
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
       // outState.putInt(STATE_SELECTED_POSITION, mCurrentSelectedPosition);
    }

    @Override
    public Loader<ArrayList<Gallery>> onCreateLoader(int id, Bundle args) {
        loader = new GalleriesLoader(getActivity().getApplicationContext(), id);
        return loader;
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<Gallery>> loader, ArrayList<Gallery> data) {

        if(data == null){
            Log.d(TAG, "DATA IS NULL");
            Handler handler = new Handler();
            handler.post(new Runnable() {
                @Override
                public void run() {
                    app.openOKDialog(GalleriesFragment.this.getActivity(), GalleriesFragment.this, "no_connection");
                }
            });

            return;
        }
        Log.d(TAG, "ON LOAD FINISHED");
        items = data;
        int index = 0;
        if(dataList == null){
            dataList = new ArrayList<GalleryItem>(items.size());
            //StringBuilder builder = ;
            for (Gallery gallery : items){
                //Log.d(TAG, "COVER PATH: " + gallery.getCover());
                //ImagePathTask task = new ImagePathTask("galleries/1419693538.203064jpg");
                //String path = new StringBuilder(Constants.CLOUD_FRONT_URL).append("/300x300/").append(gallery.getCover()).toString();
                GalleryItem galleryItem = new GalleryItem(index, gallery.getId(), gallery.getTitle(), gallery.getCover());
                //Log.d(TAG, "GALLERY IDs: " + galleryItem.getId() + " - GalleryName: " + galleryItem.getTitle());
                index ++;
                dataList.add(galleryItem);
            }
            listAdapter.addItemsInGrid(dataList);
            listView.setAdapter(listAdapter);
        }
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<Gallery>> loader) {

    }
    public void useLoader() {
        //Log.d(TAG, "USE LOADER FRAGMENT GALLERIES");
        loading = true;
        loader = (GalleriesLoader) getActivity().getLoaderManager()
                .initLoader(Constants.GALLERIES_LOADER_ID, null, this);
        loader.forceLoad();
    }

    @Override
    public void galleryOnItemClicked(int galleryId, String galleryName, int galleryItemPosition) {
        Log.d(TAG, "SEND GALLERY ID TO ACTIVITY: " + galleryId);
        //Intent intent = new Intent(getActivity(), PostsActivity.class);
        Intent intent = new Intent(getActivity(), GalleryActivity.class);
        intent.putExtra("GALLERY_ID", galleryId);
        intent.putExtra("GALLERY_NAME", galleryName);
        //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        this.startActivity(intent);
        BaseActivity.setTranslateAnimation(getActivity());
        //getActivity().overridePendingTransition(R.anim.activity_open_scale, R.anim.activity_close_translate);
/*        PopularPostsFragment galleryFragment = new PopularPostsFragment();
        galleryFragment.setActivity(activity);
        Bundle bundle = new Bundle();
        bundle.putInt("LOADER_ID", Constants.GALLERY_POSTS_LOADER_ID);
        bundle.putInt("GALLERY_ID", cardData.getId());
        galleryFragment.setArguments(bundle);
        android.app.FragmentTransaction transaction = activity.getChildFragmentManager().beginTransaction();
        transaction.addToBackStack(null);
        transaction.replace(R.id.products_list_linear, productDetailFragment).commit();
        replaceFragment(true, null, galleryFragment);*/

    }
}
