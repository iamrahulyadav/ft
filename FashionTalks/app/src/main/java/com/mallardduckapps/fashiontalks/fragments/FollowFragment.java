package com.mallardduckapps.fashiontalks.fragments;

/**
 * Created by oguzemreozcan on 16/02/15.
 */

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mallardduckapps.fashiontalks.FashionTalksApp;
import com.mallardduckapps.fashiontalks.ProfileActivity;
import com.mallardduckapps.fashiontalks.R;
import com.mallardduckapps.fashiontalks.adapters.GlammerListAdapter;
import com.mallardduckapps.fashiontalks.loaders.FollowListLoader;
import com.mallardduckapps.fashiontalks.objects.User;
import com.mallardduckapps.fashiontalks.utils.Constants;

import java.util.ArrayList;


public class FollowFragment extends ListFragment implements LoaderManager.LoaderCallbacks<ArrayList<User>>, AbsListView.OnScrollListener {

    private static final String PROFILE_ID = "PROFILE_ID";
    private static final String FOLLOWER_LIST = "FOLLOWER_LIST";
    private int paramProfileId;
    FollowListLoader loader;
    protected View loadMoreFooterView;
    int itemCountPerLoad = 0;
    boolean loading;
    ArrayList<User> dataList;
    GlammerListAdapter adapter;
    FashionTalksApp app;
    private final String TAG = "Followers_Fragment";
    boolean followers;
    int loaderId;
    ProgressBar progressBar;
    TextView noDataTv;

    private BasicFragment.OnFragmentInteractionListener mListener;

    // TODO: Rename and change types of parameters
    public static FollowFragment newInstance(int profileId, boolean followers) {
        FollowFragment fragment = new FollowFragment();
        Bundle args = new Bundle();
        args.putInt(PROFILE_ID, profileId);
        args.putBoolean(FOLLOWER_LIST, followers);
        fragment.setArguments(args);
        return fragment;
    }

    public FollowFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new GlammerListAdapter(getActivity(),dataList);
        app = (FashionTalksApp)getActivity().getApplication();
        if (getArguments() != null) {
            paramProfileId = getArguments().getInt(PROFILE_ID);
            followers = getArguments().getBoolean(FOLLOWER_LIST);
        }
        if(followers){
            loaderId = Constants.FOLLOWERS_LOADER_ID;
        }else{
            loaderId = Constants.FOLLOWING_LOADER_ID;
        }
        useLoader();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(
                R.layout.follow_list_layout, container, false);
        if(dataList != null){
            adapter.addData(dataList);
            setListAdapter(adapter);
        }
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        noDataTv = (TextView) view.findViewById(R.id.noDataTv);
        loadMoreFooterView = getLoadMoreView(inflater);
        return view;
    }

    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);
        try {
            //mListener = (BasicFragment.OnFragmentInteractionListener) activity;
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
        //if (null != mListener) {
            // Notify the active callbacks interface (the activity, if the
            // fragment is attached to one) that an item has been selected.
            //mListener.onFragmentInteraction(Uri.EMPTY);
            Log.d(TAG, "ITEM CLICKED " + position);
            Intent intent = new Intent(getActivity(), ProfileActivity.class);
            User user = dataList.get(position);
            app.setOther(user);
            intent.putExtra("PROFILE_ID", user.getId());
            //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
       // }
    }

    @Override
    public Loader<ArrayList<User>> onCreateLoader(int id, Bundle args) {
        loader = new FollowListLoader(app, getActivity().getApplicationContext(), loaderId, paramProfileId, followers);
        return loader;
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<User>> loader, ArrayList<User> data) {

        if(data == null){
            //Log.d(TAG, "DATA IS NULL");
            Handler handler = new Handler();
            handler.post(new Runnable() {
                @Override
                public void run() {
                    app.openOKDialog(FollowFragment.this.getActivity(), FollowFragment.this, "no_connection");
                }
            });
            return;
        }

        getListView().setOnScrollListener(this);
        itemCountPerLoad = data.size();
        Log.d(TAG, "LOAD SIZE: " + itemCountPerLoad);
        ListView listView = getListView();
        if(dataList == null){
            adapter.addData(data);
            dataList = data;
            if(listView != null)
                setListAdapter(adapter);
            //listView.setAdapter(adapter);
            if(canLoadMoreData()){
                listView.addFooterView(loadMoreFooterView);
                loadMoreFooterView.setVisibility(View.VISIBLE);
            }
        }else{
            //Log.d(TAG, "LOAD MORE DATA TO THE ADAPTER: ");
            dataList.addAll(data);
            //Log.d(TAG, "dataList size: " + dataList.size());
            adapter.addData(data);
        }

        if(!canLoadMoreData()){
            if(listView != null) {
                try{
                    listView.removeFooterView(loadMoreFooterView);
                }catch(Exception e){
                    e.printStackTrace();
                }
                loadMoreFooterView.setVisibility(View.INVISIBLE);
            }
        }

        if(dataList.size() == 0){
            progressBar.setVisibility(View.GONE);
            noDataTv.setVisibility(View.VISIBLE);
            if(followers){
                noDataTv.setText(getString(R.string.no_followers));
            }else{
                noDataTv.setText(getString(R.string.no_following));
            }
        }
        loading = false;
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<User>> loader) {
        dataList = null;
        loading = false;
    }

    private void useLoader(){
        if (this.canLoadMoreData() && !loading) {
            Log.d(TAG, "USE LOADER FRAGMENT FOLLOWER ");
            loading = true;
            if (loader == null) {
                loader = (FollowListLoader) getActivity().getLoaderManager()
                        .restartLoader(Constants.GLAMMERS_LOADER_ID, null, this);
                loader.forceLoad();
            } else {
                Log.d(TAG, "USE LOADER FRAGMENT FOLLOWER - ON CONTENT CHANGEDD");
                loader.forceLoad();
            }
            calculateLoadValues();
        }else{
            getListView().removeFooterView(loadMoreFooterView);
        }
    }

    @SuppressLint("InflateParams")
    protected View getLoadMoreView(LayoutInflater inflater) {
        ProgressBar loadMoreProgress = (ProgressBar) inflater.inflate(
                R.layout.auto_load_more_view, null);
        //loadMoreProgress.setBackgroundColor(Color.LTGRAY);
        return loadMoreProgress;
    }

    public boolean canLoadMoreData() {
        if(loader == null)
            return true;
        return loader.perPage > itemCountPerLoad  ? false : true;
    }

    public void calculateLoadValues(){
        if(dataList == null){
            loader.startIndex = 0;
        }else{
            loader.startIndex += loader.perPage;
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

        if(firstVisibleItem + visibleItemCount == totalItemCount && !loading && canLoadMoreData()){
            loadMoreFooterView.setVisibility(View.VISIBLE);
            Log.d(TAG, "ON REACHED TO END " + firstVisibleItem +" - visibleItemCount: " + visibleItemCount + " - totalItemCount: " + totalItemCount );
            useLoader();
        }
    }
}

