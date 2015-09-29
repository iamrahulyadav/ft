package com.mallardduckapps.fashiontalks.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.LoaderManager;
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

import com.mallardduckapps.fashiontalks.BaseActivity;
import com.mallardduckapps.fashiontalks.FashionTalksApp;
import com.mallardduckapps.fashiontalks.ProfileActivity;
import com.mallardduckapps.fashiontalks.R;
import com.mallardduckapps.fashiontalks.adapters.PopularUserListAdapter;
import com.mallardduckapps.fashiontalks.components.BounceListView;
import com.mallardduckapps.fashiontalks.loaders.PopularUsersLoader;
import com.mallardduckapps.fashiontalks.objects.PopularUser;
import com.mallardduckapps.fashiontalks.objects.User;
import com.mallardduckapps.fashiontalks.utils.Constants;

import java.util.ArrayList;


public class PopularUsersFragment extends ListFragment implements LoaderManager.LoaderCallbacks<ArrayList<PopularUser>>,AbsListView.OnScrollListener, BounceListView.RefreshListener {

    private BasicFragment.OnFragmentInteractionListener mListener;
    private PopularUsersLoader loader;
    private ProgressBar progressBar;
    private TextView emptyTv;
    private final String TAG = "PopularUserFragment";
    protected View loadMoreFooterView;
    int itemCountPerLoad = 0;
    boolean loading;
    ArrayList<PopularUser> dataList;
    PopularUserListAdapter adapter;
    FashionTalksApp app;

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
        adapter = new PopularUserListAdapter(getActivity(), dataList);
        app = (FashionTalksApp)getActivity().getApplication();
        useLoader();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(
                R.layout.user_list_layout, container, false);
        emptyTv = (TextView) view.findViewById(R.id.noDataTv);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        if(dataList != null){
            adapter.addData(dataList);
            setListAdapter(adapter);
        }
        loadMoreFooterView = getLoadMoreView(inflater);

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
        Log.d(TAG, "ON ITEM CLICKED" + position);
        if (null != mListener) {
            // Notify the active callbacks interface (the activity, if the
            // fragment is attached to one) that an item has been selected.
            //mListener.onFragmentInteraction(DummyContent.ITEMS.get(position).id);
            Intent intent = new Intent(getActivity(), ProfileActivity.class);
            User user = dataList.get(position);
            app.setOther(user);
            intent.putExtra("PROFILE_ID", user.getId());
            //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            BaseActivity.setTranslateAnimation(getActivity());
            //getActivity().overridePendingTransition(R.anim.activity_open_scale, R.anim.activity_close_translate);
        }
    }

    @Override
    public Loader<ArrayList<PopularUser>> onCreateLoader(int id, Bundle args) {
        loader = new PopularUsersLoader(getActivity().getApplicationContext(),app, Constants.POPULAR_USERS_LOADER_ID);
        return loader;
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<PopularUser>> loader, ArrayList<PopularUser> data) {
        ListView listView = getListView();

        if(data == null){
            //Log.d(TAG, "DATA IS NULL");
            Handler handler = new Handler();
            handler.post(new Runnable() {
                @Override
                public void run() {
                    app.openOKDialog(PopularUsersFragment.this.getActivity(), PopularUsersFragment.this, "no_connection");
                }
            });

            return;
        }
        if( data.size() == 0){
            progressBar.setVisibility(View.GONE);
            emptyTv.setVisibility(View.VISIBLE);
        }
        getListView().setOnScrollListener(this);

        //Log.d(TAG, "set adapter: " + data.size());
        itemCountPerLoad = data.size();
        if(dataList == null){
            //index = 0;
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
                listView.removeFooterView(loadMoreFooterView);
                loadMoreFooterView.setVisibility(View.INVISIBLE);
            }
        }
        loading = false;
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<PopularUser>> loader) {
        dataList = null;
        loading = false;
    }

    private void useLoader(){
        if (this.canLoadMoreData() && !loading) {
            Log.d(TAG, "USE LOADER FRAGMENT POPULAR POSTS");
            loading = true;
            if (loader == null) {
                loader = (PopularUsersLoader) getActivity().getLoaderManager()
                        .initLoader(Constants.POPULAR_USERS_LOADER_ID, null, this);
                loader.forceLoad();
            } else {
                Log.d(TAG, "USE LOADER FRAGMENT POPULAR POSTS - ON CONTENT CHANGEDD");
                //loader.startLoading(); //= (PopularPostsLoader) getActivity().getLoaderManager()
                // .restartLoader(Constants.POPULAR_POSTS_LOADER_ID, null, this);
                // loader.onContentChanged();
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
        return loader.perPage > itemCountPerLoad  ? false : true;//listData.size() < getMaxAllowedItems();
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
            //Log.d(TAG, "ON REACHED TO END " + firstVisibleItem +" - visibleItemCount: " + visibleItemCount + " - totalItemCount: " + totalItemCount );
            useLoader();
        }
    }

    @Override
    public void onRefreshList() {
        Log.d(TAG, "LIST REFRESHED");
    }
}
