package com.mallardduckapps.fashiontalks.fragments;

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
import com.mallardduckapps.fashiontalks.PostsActivity;
import com.mallardduckapps.fashiontalks.ProfileActivity;
import com.mallardduckapps.fashiontalks.R;
import com.mallardduckapps.fashiontalks.adapters.NotificationListAdapter;
import com.mallardduckapps.fashiontalks.components.BounceListView;
import com.mallardduckapps.fashiontalks.loaders.NotificationListLoader;
import com.mallardduckapps.fashiontalks.objects.Notification;
import com.mallardduckapps.fashiontalks.objects.User;
import com.mallardduckapps.fashiontalks.utils.Constants;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;


public class NotificationsFragment extends ListFragment implements LoaderManager.LoaderCallbacks<ArrayList<Notification>> {

    private BasicFragment.OnFragmentInteractionListener mListener;
    private NotificationListLoader loader;
    ProgressBar progressBar;
    TextView noDataTv;
    NotificationListAdapter adapter;
    FashionTalksApp app;

    public NotificationsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        app = (FashionTalksApp)getActivity().getApplication();
        useLoader();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(
                R.layout.notifications_list_layout, container, false);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        noDataTv = (TextView) view.findViewById(R.id.noDataTv);

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
            mListener.onFragmentInteraction("");
            ArrayList<Notification> notificationList = adapter.getList();
            Notification notification = notificationList.get(position);
            User source = notification.getSource();
            boolean myPost = true;
            Log.d("NOTIFICATION","TARGET: " + notification.getTargetAction() + " - targetID: " + notification.getTargetId());
            if(source == null){
                Log.d("NOTIFICATION","SOURCE NULL" );
                goToTarget(true, notification, app.getMe());
                //return;
            }else{
                if(source.getId() != app.getMe().getId()){
                    app.setOther(source);
                    myPost = false;
                }
                goToTarget(myPost, notification, source);
            }
        }
    }

    private void goToTarget(boolean myPost, Notification notification, User source){
        if(notification.getTargetAction().equals(Constants.TARGET_PROFILE)){
            Intent intent = new Intent(getActivity(), ProfileActivity.class);
            intent.putExtra("PROFILE_ID", source.getId());
            //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            BaseActivity.setTranslateAnimation(getActivity());
        }else if(notification.getTargetAction().equals(Constants.TARGET_POST)){
            Intent intent = new Intent(getActivity(), PostsActivity.class);
            intent.putExtra("LOADER_ID", myPost ? Constants.NOTIFICATION_MY_POST_LOADER_ID :  Constants.NOTIFICATION_OTHER_POST_LOADER_ID);
            intent.putExtra("POST_ID", notification.getTargetId());
            //intent.putExtra("POST_INDEX", -1);
            //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            this.startActivity(intent);
            BaseActivity.setTranslateAnimation(getActivity());
        }else if(notification.getTargetAction().equals(Constants.TARGET_COMMENT)){
            Intent intent = new Intent(getActivity(), PostsActivity.class);
            intent.putExtra("LOADER_ID", myPost ? Constants.NOTIFICATION_MY_POST_LOADER_ID :  Constants.NOTIFICATION_OTHER_POST_LOADER_ID);
            intent.putExtra("POST_ID", notification.getTargetId());
            intent.putExtra("OPEN_COMMENT", true);
            //intent.putExtra("POST_INDEX", -1);
            //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            this.startActivity(intent);
            BaseActivity.setTranslateAnimation(getActivity());
        }
    }

    @Override
    public Loader<ArrayList<Notification>> onCreateLoader(int id, Bundle args) {
        loader = new NotificationListLoader(getActivity().getApplicationContext(), Constants.NOTIFICATIONS_LOADER_ID);
        return loader;
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<Notification>> loader, ArrayList<Notification> data) {

        if(data == null){
            Log.d("NotificationsFragment", "DATA IS NULL");
            Handler handler = new Handler();
            handler.post(new Runnable() {
                @Override
                public void run() {
                    app.openOKDialog(NotificationsFragment.this.getActivity(), NotificationsFragment.this, "no_connection");
                }
            });

            return;
        }
        adapter = new NotificationListAdapter(getActivity(),data);
        setListAdapter(adapter);
        if(data.size() == 0){
            progressBar.setVisibility(View.GONE);
            noDataTv.setVisibility(View.VISIBLE);
            noDataTv.setText(getString(R.string.no_notification));
        }
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<Notification>> loader) {

    }

    private void useLoader() {
        if (loader == null) {
            loader = (NotificationListLoader) getActivity().getLoaderManager()
                    .restartLoader(Constants.NOTIFICATIONS_LOADER_ID, null, this);
            loader.forceLoad();
        }
    }
}
