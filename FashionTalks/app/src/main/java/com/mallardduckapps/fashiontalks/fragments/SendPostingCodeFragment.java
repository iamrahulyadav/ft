package com.mallardduckapps.fashiontalks.fragments;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.Loader;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mallardduckapps.fashiontalks.FashionTalksApp;
import com.mallardduckapps.fashiontalks.R;
import com.mallardduckapps.fashiontalks.adapters.SendCodeListAdapter;
import com.mallardduckapps.fashiontalks.loaders.SendCodeListLoader;
import com.mallardduckapps.fashiontalks.objects.User;
import com.mallardduckapps.fashiontalks.services.RestClient;
import com.mallardduckapps.fashiontalks.utils.Constants;
import com.mallardduckapps.fashiontalks.utils.FTUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SendPostingCodeFragment extends ListFragment implements LoaderManager.LoaderCallbacks<ArrayList<User>> {

    SendCodeListLoader loader;
    boolean loading;
    ArrayList<User> dataList;
    SendCodeListAdapter adapter;
    FashionTalksApp app;
    private final String TAG = "Followers_Fragment";
    ProgressBar progressBar;
    TextView noDataTv;
    private BasicFragment.OnFragmentInteractionListener mListener;

    // TODO: Rename and change types of parameters
    public static SendPostingCodeFragment newInstance() {
        SendPostingCodeFragment fragment = new SendPostingCodeFragment();
        return fragment;
    }

    public SendPostingCodeFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new SendCodeListAdapter(getActivity(),dataList);
        app = (FashionTalksApp)getActivity().getApplication();
        //loaderId = Constants.CODE_REQUESTS_LOADER;
        useLoader();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(
                R.layout.send_code_layout, container, false);
        if(dataList != null){
            adapter.addData(dataList);
            setListAdapter(adapter);
        }
        User me = app.getMe();
        TextView codesLeft = (TextView) view.findViewById(R.id.codesLeft);
        Button sendPermission = (Button) view.findViewById(R.id.sendPermission);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        noDataTv = (TextView) view.findViewById(R.id.noDataTv);
        sendPermission.setTypeface(FTUtils.loadFont(getActivity().getAssets(), getActivity().getString(R.string.font_helvatica_md)));
        codesLeft.setTypeface(FTUtils.loadFont(getActivity().getAssets(), getActivity().getString(R.string.font_helvatica_lt)));
        //loadMoreFooterView = getLoadMoreView(inflater);
        codesLeft.setText(me.getInvitesLeft() + " Davet Kaldı.");

        if(me.getInvitesLeft() > 0){
            sendPermission.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int[] userIds = adapter.getSelectedUserIds();
                    try {
                        JSONArray array = new JSONArray("ids");
                        for(int i : userIds){
                            if(i != 0){
                                array.put(i);
                            }
                        }
                        SendCodeTask task = new SendCodeTask(array);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            });
        }
        return view;
    }

    @Override
    public void onAttach(Activity activity) {
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
/*        Log.d(TAG, "ITEM CLICKED " + position);
        Intent intent = new Intent(getActivity(), ProfileActivity.class);
        User user = dataList.get(position);
        app.setOther(user);
        intent.putExtra("PROFILE_ID", user.getId());
        //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);*/
        // }
    }

    @Override
    public Loader<ArrayList<User>> onCreateLoader(int id, Bundle args) {
        loader = new SendCodeListLoader(getActivity().getApplicationContext());
        return loader;
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<User>> loader, ArrayList<User> data) {
        //getListView().setOnScrollListener(this);
        //itemCountPerLoad = data.size();
        //Log.d(TAG, "LOAD SIZE: " + itemCountPerLoad);
        ListView listView = getListView();
        if(dataList == null){
            adapter.addData(data);
            dataList = data;
            if(listView != null)
                setListAdapter(adapter);
            //listView.setAdapter(adapter);
        }else{
            //Log.d(TAG, "LOAD MORE DATA TO THE ADAPTER: ");
            dataList.addAll(data);
            //Log.d(TAG, "dataList size: " + dataList.size());
            adapter.addData(data);
        }
        if(dataList.size() == 0){
            //dataList.add(app.getMe());
            //dataList.add(app.getOther());
            //setListAdapter(adapter);
            progressBar.setVisibility(View.GONE);
            //noDataTv.setVisibility(View.VISIBLE);
        }
        loading = false;
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<User>> loader) {
        dataList = null;
        loading = false;
    }

    private void useLoader(){
        if (!loading) {
            //Log.d(TAG, "USE LOADER FRAGMENT FOLLOWER ");
            loading = true;
            if (loader == null) {
                loader = (SendCodeListLoader) getActivity().getLoaderManager()
                        .initLoader(Constants.CODE_REQUESTS_LOADER, null, this);
                loader.forceLoad();
            } else {
                //Log.d(TAG, "USE LOADER FRAGMENT FOLLOWER - ON CONTENT CHANGEDD");
                loader.forceLoad();
            }
            //calculateLoadValues();
        }
    }

    public class SendCodeTask extends AsyncTask<Void, Void, String> {

        private final String TAG = "SendCodeTask";
        //TODO add lsitener to get callback value
        private final JSONArray array;

        public SendCodeTask(JSONArray array){

            this.array = array;
        }

        @Override
        protected String doInBackground(Void... params) {
            String response = "";
            RestClient restClient = new RestClient();
            try {
                //String url = new StringBuilder(Constants.FOLLOW_USER_PREFIX).toString();
                response = restClient.doPostRequestWithJSON(Constants.POST_CODE_REQUEST_PREFIX, null, array.toString());
                JSONObject object = new JSONObject(response);
                int status = object.getInt("status");
                Log.d(TAG, "RESPONSE FROM API: " + response);
            } catch (Exception e) {
                response = "NO_CONNECTION";
                e.printStackTrace();
            }
            return response;
        }
    }

}