package com.mallardduckapps.fashiontalks.fragments;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.Loader;
import android.os.Bundle;

import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mallardduckapps.fashiontalks.R;
import com.mallardduckapps.fashiontalks.adapters.PopularUserListAdapter;
import com.mallardduckapps.fashiontalks.loaders.PopularUsersLoader;
import com.mallardduckapps.fashiontalks.objects.PopularUser;
import com.mallardduckapps.fashiontalks.utils.Constants;

import java.util.ArrayList;


public class PopularUsersFragment extends ListFragment implements LoaderManager.LoaderCallbacks<ArrayList<PopularUser>> {

    private BasicFragment.OnFragmentInteractionListener mListener;
    private PopularUsersLoader loader;
    private ProgressBar progressBar;
    private TextView emptyTv;
    private final String TAG = "PopularUserFragment";

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public PopularUsersFragment() {
    }

    public static PopularUsersFragment newInstance(String param1) {
        PopularUsersFragment fragment = new PopularUsersFragment();
        Bundle args = new Bundle();
        //args.putString(POST_ID, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        useLoader();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(
                R.layout.list_layout, container, false);
        emptyTv = (TextView) view.findViewById(R.id.noDataTv);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (BasicFragment.OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        if (null != mListener) {
            // Notify the active callbacks interface (the activity, if the
            // fragment is attached to one) that an item has been selected.
            //mListener.onFragmentInteraction(DummyContent.ITEMS.get(position).id);
        }
    }

    @Override
    public Loader<ArrayList<PopularUser>> onCreateLoader(int id, Bundle args) {
        loader = new PopularUsersLoader(getActivity().getApplicationContext(), Constants.POPULAR_USERS_LOADER_ID);
        return loader;
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<PopularUser>> loader, ArrayList<PopularUser> data) {
        if(data == null || data.size() == 0){
            progressBar.setVisibility(View.GONE);
            emptyTv.setVisibility(View.VISIBLE);
        }

        Log.d(TAG, "SET ADAPTER: " + data.size());

        // TODO: Change Adapter to display your content
        setListAdapter(new PopularUserListAdapter(getActivity(), data));
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<PopularUser>> loader) {

    }

    private void useLoader(){
        if(loader == null ) {
            loader = (PopularUsersLoader) getActivity().getLoaderManager()
                    .initLoader(Constants.POPULAR_USERS_LOADER_ID, null, this);
            loader.forceLoad();
        }
    }




}
