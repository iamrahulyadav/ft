package com.mallardduckapps.fashiontalks.fragments;

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
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.mallardduckapps.fashiontalks.BaseActivity;
import com.mallardduckapps.fashiontalks.FashionTalksApp;
import com.mallardduckapps.fashiontalks.ProfileActivity;
import com.mallardduckapps.fashiontalks.R;
import com.mallardduckapps.fashiontalks.adapters.GlammerListAdapter;
import com.mallardduckapps.fashiontalks.loaders.FacebookFriendsLoader;
import com.mallardduckapps.fashiontalks.objects.User;
import com.mallardduckapps.fashiontalks.services.RestClient;
import com.mallardduckapps.fashiontalks.tasks.LoginFBTask;
import com.mallardduckapps.fashiontalks.tasks.LoginTask;
import com.mallardduckapps.fashiontalks.utils.Constants;
import com.mallardduckapps.fashiontalks.utils.FTUtils;

import java.util.ArrayList;

public class FacebookFriendsFragment extends ListFragment implements LoaderManager.LoaderCallbacks<ArrayList<User>>,
        AbsListView.OnScrollListener, LoginTask.LoginTaskCallback{

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
    CallbackManager callbackManager;
    boolean inLogin;

    AccessTokenTracker accessTokenTracker;

    private BasicFragment.OnFragmentInteractionListener mListener;

    public static FacebookFriendsFragment newInstance(boolean inLogin) {
        FacebookFriendsFragment fragment = new FacebookFriendsFragment();
        Bundle args = new Bundle();
        args.putBoolean("IN_LOGIN", inLogin);
        fragment.setArguments(args);
        return fragment;
    }

    public FacebookFriendsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new GlammerListAdapter(getActivity(),dataList);
        app = (FashionTalksApp)getActivity().getApplication();
        inLogin = getArguments().getBoolean("IN_LOGIN");
        FacebookSdk.sdkInitialize(getActivity().getApplicationContext());
        //callbackManager = CallbackManager.Factory.create();
        fbLoginManager();
        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken newAccessToken) {
                Log.d(TAG, "OLD ACCESS TOKEN: " + oldAccessToken + " - newAccessTOken: " + newAccessToken);
                //updateWithToken(newAccessToken);
            }
        };
        useLoader();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        if(requestCode == Constants.NO_CONNECTION && resultCode == 1){
            getActivity().finish();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        accessTokenTracker.stopTracking();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(
                R.layout.list_layout, container, false);
        if(dataList != null){
            adapter.addData(dataList);
            setListAdapter(adapter);
        }
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        noDataTv = (TextView) view.findViewById(R.id.noDataTv);
        noDataTv.setText(getString(R.string.no_conection_with_fb));
        noDataTv.setTypeface(FTUtils.loadFont(getActivity().getAssets(), getString(R.string.font_helvatica_lt)));
        connectToFbTv = (TextView) view.findViewById(R.id.connectToFbTv);
        connectToFbTv.setTypeface(FTUtils.loadFont(getActivity().getAssets(), getActivity().getString(R.string.font_helvatica_bold)));
        if(inLogin){
            TextView followFriends = (TextView) view.findViewById(R.id.followFriends);
            followFriends.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }
        loadMoreFooterView = getLoadMoreView(inflater);
        return view;
    }

    @Override
    public void onAttach(Context activity) {
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
    public Loader<ArrayList<User>> onCreateLoader(int id, Bundle args) {
        loader = new FacebookFriendsLoader(getActivity(), Constants.FB_FRIENDS_LOADER);
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
                    app.openOKDialog(FacebookFriendsFragment.this.getActivity(), FacebookFriendsFragment.this, "no_connection");
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
                listView.removeFooterView(loadMoreFooterView);
                loadMoreFooterView.setVisibility(View.INVISIBLE);
            }
        }

        if(dataList.size() == 0){
            if(this.loader.getStatus() == 0){
                noDataTv.setText(getString(R.string.no_user_to_suggest));
                connectToFbTv.setVisibility(View.INVISIBLE);
            }else{
                noDataTv.setText(getString(R.string.no_conection_with_fb));
                connectToFbTv.setVisibility(View.VISIBLE);
                connectToFbTv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        facebookLogin();
                    }
                });
            }
            progressBar.setVisibility(View.GONE);
            noDataTv.setVisibility(View.VISIBLE);
        }
        loading = false;
    }

    public void facebookLogin() {
        Log.d(TAG, "FB LOGIN BUTTON CLICKED");
        ArrayList<String> permissions = new ArrayList<>();
        permissions.add("user_friends");
        permissions.add("public_profile");
        permissions.add("email");
        permissions.add("user_birthday");
        permissions.add("user_location");
        LoginManager.getInstance().logInWithReadPermissions(this, permissions);
    }

    public void fbLoginManager(){
        //Create callback manager to handle login response
        callbackManager = CallbackManager.Factory.create();

        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.i(TAG, "LoginManager FacebookCallback onSuccess");
                if (loginResult.getAccessToken() != null) {
                    Log.i(TAG, "Access Token:: " + loginResult.getAccessToken());
                    String accessToken = loginResult.getAccessToken().getToken();
                    Log.d(TAG, "FB ON SUCCESS + " + accessToken);
                    LoginFBTask task = new LoginFBTask(getActivity(), FacebookFriendsFragment.this, accessToken);
                    //ConnectFBTask task = new ConnectFBTask(accessToken, true);
                    task.execute();
                    //facebookSuccess();
                }
            }

            @Override
            public void onCancel() {
                Log.i(TAG, "LoginManager FacebookCallback onCancel");
            }

            @Override
            public void onError(FacebookException e) {
                Log.i(TAG, "LoginManager FacebookCallback onError");
            }
        });
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<User>> loader) {
        dataList = null;
        loading = false;
    }

    private void useLoader(){
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
    public void getAuthStatus(int authStatus, User user, String... tokens) {
        Log.d(TAG, "GET AUTH STATUS : " + authStatus);
        if(authStatus == Constants.FB_AUTHENTICATION_SUCCESSFUL && tokens != null){

            app.dataSaver.putString(Constants.ACCESS_TOKEN_KEY, tokens[0]);
            app.dataSaver.putString(Constants.REFRESH_TOKEN_KEY, tokens[1]);
            app.dataSaver.putString(Constants.FB_ACCESS_TOKEN_KEY, tokens[0]);
            Log.d(TAG, "GET AUTH STATUS token : " + tokens[0]);
            RestClient.setAccessToken(tokens[0]);
            app.dataSaver.save();

            loading = true;
            loader = (FacebookFriendsLoader) getActivity().getLoaderManager()
                    .restartLoader(Constants.FB_FRIENDS_LOADER, null, this);
            loader.forceLoad();
            calculateLoadValues();
        }else if(authStatus == Constants.AUTHENTICATION_FAILED){
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getActivity(), getString(R.string.problem_occured), Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    @Override
    public void getUser(int authStatus, User user) {
        if(user != null){
            app.setMe(user);
        }
    }
}
