package com.mallardduckapps.fashiontalks.fragments;


import android.app.LoaderManager;
import android.content.Loader;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.mallardduckapps.fashiontalks.R;
import com.mallardduckapps.fashiontalks.adapters.GalleryGridAdapter;
import com.mallardduckapps.fashiontalks.loaders.GalleriesLoader;
import com.mallardduckapps.fashiontalks.loaders.PopularPostsLoader;
import com.mallardduckapps.fashiontalks.objects.Gallery;
import com.mallardduckapps.fashiontalks.objects.GalleryItem;
import com.mallardduckapps.fashiontalks.utils.Constants;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class PopularPostsFragment extends Fragment implements
        LoaderManager.LoaderCallbacks<ArrayList<Gallery>> {
    PopularPostsLoader loader;
    boolean loading;
    private final String TAG = "POPULAR_POSTS_Fragment";
    private ListView listView;
    private ArrayList<GalleryItem> dataList;
    private GalleryGridAdapter listAdapter;
    //private ArrayList<Gallery> items;
    private final int MAX_CARDS = 2;

    public PopularPostsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        listAdapter = new GalleryGridAdapter(getActivity(),MAX_CARDS);
        useLoader();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_gallery, container, false);
        listView = (ListView) rootView.findViewById(R.id.galleryList);
        if(dataList != null){
            listAdapter.addItemsInGrid(dataList);
            listView.setAdapter(listAdapter);
        }

        return rootView;
    }

    public void useLoader() {
        Log.d(TAG, "USE LOADER FRAGMENT GALLERIES");
        loading = true;
        loader = (PopularPostsLoader) getActivity().getLoaderManager()
                .initLoader(Constants.POPULAR_POSTS_LOADER_ID, null, this);
        loader.forceLoad();
    }


    @Override
    public Loader<ArrayList<Gallery>> onCreateLoader(int id, Bundle args) {
        loader = new PopularPostsLoader(getActivity().getApplicationContext(), id);
        return loader;
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<Gallery>> loader, ArrayList<Gallery> data) {

    }

    @Override
    public void onLoaderReset(Loader<ArrayList<Gallery>> loader) {

    }
}
