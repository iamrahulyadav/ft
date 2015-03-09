package com.mallardduckapps.fashiontalks.fragments;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.Loader;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mallardduckapps.fashiontalks.R;
import com.mallardduckapps.fashiontalks.adapters.NotificationListAdapter;
import com.mallardduckapps.fashiontalks.loaders.NotificationListLoader;
import com.mallardduckapps.fashiontalks.objects.Notification;
import com.mallardduckapps.fashiontalks.utils.Constants;

import java.util.ArrayList;


public class NotificationsFragment extends ListFragment implements LoaderManager.LoaderCallbacks<ArrayList<Notification>> {

    private BasicFragment.OnFragmentInteractionListener mListener;
    private NotificationListLoader loader;
    ProgressBar progressBar;
    TextView noDataTv;

    public NotificationsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        }
    }


    @Override
    public Loader<ArrayList<Notification>> onCreateLoader(int id, Bundle args) {
        loader = new NotificationListLoader(getActivity().getApplicationContext(), Constants.NOTICATIONS_LOADER_ID);
        return loader;
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<Notification>> loader, ArrayList<Notification> data) {
        NotificationListAdapter adapter = new NotificationListAdapter(getActivity(),data);
        setListAdapter(adapter);
        if(data.size() == 0){
            progressBar.setVisibility(View.GONE);
            noDataTv.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<Notification>> loader) {

    }

    private void useLoader() {
        if (loader == null) {
            loader = (NotificationListLoader) getActivity().getLoaderManager()
                    .initLoader(Constants.NOTICATIONS_LOADER_ID, null, this);
            loader.forceLoad();
        }
    }
}
