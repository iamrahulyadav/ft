package com.mallardduckapps.fashiontalks.fragments;


import android.app.LoaderManager;
import android.content.Loader;
import android.graphics.Point;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.mallardduckapps.fashiontalks.R;
import com.mallardduckapps.fashiontalks.adapters.GalleryGridAdapter;
import com.mallardduckapps.fashiontalks.loaders.GalleriesLoader;
import com.mallardduckapps.fashiontalks.objects.Gallery;
import com.mallardduckapps.fashiontalks.objects.GalleryItem;
import com.mallardduckapps.fashiontalks.utils.Constants;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public abstract class GalleryBaseFragment extends BasicFragment  implements
        LoaderManager.LoaderCallbacks<ArrayList<Gallery>>{


    GalleriesLoader loader;
    boolean loading;
    private ListView listView;
    private ArrayList<GalleryItem> dataList;
    private GalleryGridAdapter listAdapter;
    private ArrayList<Gallery> items;
    private final int MAX_CARDS = 2;
    public static String CARD_SIZE_TEXT = "/200x200/";

    public GalleryBaseFragment() {
        // Required empty public constructor
        super();
        setLoaderId();
    }

    public abstract void setLoaderId();


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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        listAdapter = new GalleryGridAdapter(getActivity(),MAX_CARDS);
        useLoader();
    }

    @Override
    public Loader<ArrayList<Gallery>> onCreateLoader(int id, Bundle args) {
        loader = new GalleriesLoader(getActivity().getApplicationContext(), id);
        return loader;
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<Gallery>> loader, ArrayList<Gallery> data) {
        Log.d(TAG, "ON LOAD FINISHED");
        items = data;
        int index = 0;
        if(dataList == null){
            dataList = new ArrayList<GalleryItem>(items.size());
            for (Gallery gallery : items){
                Log.d(TAG, "COVER PATH: " + gallery.getCover());
                String path = new StringBuilder(Constants.CLOUD_FRONT_URL).append(CARD_SIZE_TEXT).append(gallery.getCover()).toString();
                GalleryItem galleryItem = new GalleryItem(index, gallery.getId(), gallery.getTitle(), path);
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
        Log.d(TAG, "USE LOADER FRAGMENT GALLERIES");
        loading = true;
        loader = (GalleriesLoader) getActivity().getLoaderManager()
                .initLoader(Constants.GALLERIES_LOADER_ID, null, this);
        loader.forceLoad();
    }

}
