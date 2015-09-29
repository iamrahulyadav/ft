package com.mallardduckapps.fashiontalks.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.LoaderManager;
import android.content.Context;
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
import com.mallardduckapps.fashiontalks.R;
import com.mallardduckapps.fashiontalks.adapters.GlammerListAdapter;
import com.mallardduckapps.fashiontalks.loaders.FacebookFriendsLoader;
import com.mallardduckapps.fashiontalks.objects.User;
import com.mallardduckapps.fashiontalks.utils.Constants;
import com.mallardduckapps.fashiontalks.utils.FTUtils;

import java.util.ArrayList;

/**
 * Created by oguzemreozcan on 25/08/15.
 */

public class FacebookFriendsLoginFragment extends ListFragment implements LoaderManager.LoaderCallbacks<ArrayList<User>>, AbsListView.OnScrollListener, GlammerListAdapter.FollowProcessListener {

    boolean loading;
    ArrayList<User> dataList;
    GlammerListAdapter adapter;
    private final String TAG = "FB_Friends_Fragment";
    ProgressBar progressBar;
    TextView noDataTv;
    TextView connectToFbTv;
    FashionTalksApp app;
    protected View loadMoreFooterView;
    int itemCountPerLoad = 0;
    FacebookFriendsLoader loader;
    private BasicFragment.OnLoginFragmentInteractionListener mListener;
    boolean followTaskIsOnTheRun = false;

    public static FacebookFriendsLoginFragment newInstance() {
        FacebookFriendsLoginFragment fragment = new FacebookFriendsLoginFragment();
        return fragment;
    }

    public FacebookFriendsLoginFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new GlammerListAdapter(getActivity(),this, dataList);
        app = (FashionTalksApp) getActivity().getApplication();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(
                R.layout.list_layout, container, false);
        if (dataList != null) {
            adapter.addData(dataList);
            setListAdapter(adapter);
        }
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        noDataTv = (TextView) view.findViewById(R.id.noDataTv);
        noDataTv.setText(getString(R.string.no_conection_with_fb));
        noDataTv.setTypeface(FTUtils.loadFont(getActivity().getAssets(), getString(R.string.font_helvatica_lt)));
        connectToFbTv = (TextView) view.findViewById(R.id.connectToFbTv);
        connectToFbTv.setVisibility(View.INVISIBLE);
        connectToFbTv.setTypeface(FTUtils.loadFont(getActivity().getAssets(), getActivity().getString(R.string.font_helvatica_bold)));
        TextView followFriends = (TextView) view.findViewById(R.id.followFriends);
        followFriends.setVisibility(View.VISIBLE);
        followFriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "FOLLOW TASK ON THE RUN: " + followTaskIsOnTheRun);
                if (!followTaskIsOnTheRun) {
                    mListener.goToMainActivity();
                }
            }
        });
        loadMoreFooterView = getLoadMoreView(inflater);
        useLoader();
        return view;
    }

    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);
        try {
            mListener = (BasicFragment.OnLoginFragmentInteractionListener) activity;
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
    public Loader<ArrayList<User>> onCreateLoader(int id, Bundle args) {
        loader = new FacebookFriendsLoader(getActivity(), Constants.FB_FRIENDS_LOADER);
        return loader;
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<User>> loader, ArrayList<User> data) {
        if (data == null) {
            //Log.d(TAG, "DATA IS NULL");
            Handler handler = new Handler();
            handler.post(new Runnable() {
                @Override
                public void run() {
                    app.openOKDialog(FacebookFriendsLoginFragment.this.getActivity(), FacebookFriendsLoginFragment.this, "no_connection");
                }
            });

            return;
        }

        getListView().setOnScrollListener(this);
        itemCountPerLoad = data.size();
        Log.d(TAG, "LOAD SIZE: " + itemCountPerLoad);
        ListView listView = getListView();
        if (dataList == null) {
            adapter.addData(data);
            dataList = data;
            if (listView != null)
                setListAdapter(adapter);
            //listView.setAdapter(adapter);
            if (canLoadMoreData()) {
                listView.addFooterView(loadMoreFooterView);
                loadMoreFooterView.setVisibility(View.VISIBLE);
            }
        } else {
            //Log.d(TAG, "LOAD MORE DATA TO THE ADAPTER: ");
            dataList.addAll(data);
            //Log.d(TAG, "dataList size: " + dataList.size());
            adapter.addData(data);
        }

        if (!canLoadMoreData()) {
            if (listView != null) {
                listView.removeFooterView(loadMoreFooterView);
                loadMoreFooterView.setVisibility(View.INVISIBLE);
            }
        }

        if (dataList.size() == 0) {

            progressBar.setVisibility(View.GONE);
            noDataTv.setVisibility(View.VISIBLE);
            mListener.goToMainActivity();
        }
        loading = false;
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<User>> loader) {
        dataList = null;
        loading = false;
    }

    private void useLoader() {
        if (this.canLoadMoreData() && !loading) {
            Log.d(TAG, "USE LOADER FRAGMENT FB FRIENDS ");
            loading = true;
            if (loader == null) {
                loader = (FacebookFriendsLoader) getActivity().getLoaderManager()
                        .restartLoader(Constants.FB_FRIENDS_LOADER, null, this);
                loader.forceLoad();
            } else {
                Log.d(TAG, "USE LOADER FRAGMENT FOLLOWER - ON CONTENT CHANGEDD");
                loader.forceLoad();
            }
            calculateLoadValues();
        } else {
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
        if (loader == null)
            return true;
        return loader.perPage > itemCountPerLoad ? false : true;
    }

    public void calculateLoadValues() {
        if (dataList == null) {
            loader.startIndex = 0;
        } else {
            loader.startIndex += loader.perPage;
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

        if (firstVisibleItem + visibleItemCount == totalItemCount && !loading && canLoadMoreData()) {
            loadMoreFooterView.setVisibility(View.VISIBLE);
            Log.d(TAG, "ON REACHED TO END " + firstVisibleItem + " - visibleItemCount: " + visibleItemCount + " - totalItemCount: " + totalItemCount);
            useLoader();
        }
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Log.d(TAG, "ON ITEM CLICKED" + position);
//        if (null != mListener) {
//            // Notify the active callbacks interface (the activity, if the
//            // fragment is attached to one) that an item has been selected.
//            //mListener.onFragmentInteraction(DummyContent.ITEMS.get(position).id);
//            Intent intent = new Intent(getActivity(), ProfileActivity.class);
//            User user = dataList.get(position);
//            app.setOther(user);
//            intent.putExtra("PROFILE_ID", user.getId());
//            //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//            startActivity(intent);
//            BaseActivity.setTranslateAnimation(getActivity());
//            //getActivity().overridePendingTransition(R.anim.activity_open_scale, R.anim.activity_close_translate);
//        }
    }

    @Override
    public void onFollowTaskStarted() {
        Log.d(TAG, "ON FOLLOW TASK STARTED");
        progressBar.setVisibility(View.VISIBLE);
        followTaskIsOnTheRun = true;
    }

    @Override
    public void onFollowTaskFinished() {
        Log.d(TAG, "ON FOLLOW TASK ENDED");
        progressBar.setVisibility(View.GONE);
        followTaskIsOnTheRun = false;
    }
}
