package com.mallardduckapps.fashiontalks.fragments;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ListFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mallardduckapps.fashiontalks.FashionTalksApp;
import com.mallardduckapps.fashiontalks.ProfileActivity;
import com.mallardduckapps.fashiontalks.R;
import com.mallardduckapps.fashiontalks.adapters.GlammerListAdapter;
import com.mallardduckapps.fashiontalks.loaders.SearchUserLoader;
import com.mallardduckapps.fashiontalks.objects.User;
import com.mallardduckapps.fashiontalks.utils.Constants;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class SearchUserFragment extends ListFragment implements LoaderManager.LoaderCallbacks<ArrayList<User>>  {

    private boolean keyboardOpen;
    GlammerListAdapter adapter;
    private ArrayList<User> userList;
    private Timer timer;
    private final long DELAY = 500; // in ms
    public final String TAG = "Search_User";
    private EditText searchUser;
    private TextView noDataTv;
    private TextView xTv;
    private ProgressBar progressBar;
    FashionTalksApp app;
    SearchUserLoader loader;

    public static SearchUserFragment newInstance() {
        SearchUserFragment fragment = new SearchUserFragment();
        return fragment;
    }

    public SearchUserFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        app = (FashionTalksApp)getActivity().getApplication();
        timer = new Timer();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(
                R.layout.search_list_layout, container, false);
        searchUser = (EditText) rootView.findViewById(R.id.searchEditText);
        progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);
        noDataTv = (TextView) rootView.findViewById(R.id.noDataTv);
        xTv = (TextView) rootView.findViewById(R.id.xTv);
        xTv.setVisibility(View.GONE);
        searchUser.addTextChangedListener(new TextWatcher() {
            String text = "";

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (timer != null) {
                    timer.cancel();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                String t = s.toString().trim();
                if (t.equals(text)) {
                    return;
                }
                text = t.toLowerCase(Locale.getDefault());
                Log.d(TAG, "TEXT: " + text);
                if (text.length() > 1) {
                    xTv.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.VISIBLE);
                    noDataTv.setVisibility(View.GONE);
                    timer = new Timer();
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            SearchUserFragment.this.getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    useLoader(text);
//                                    SearchUserTask task = new SearchUserTask();
//                                    task.execute(text);
                                }
                            });
                        }
                    }, DELAY);
                } else {
                    xTv.setVisibility(View.GONE);
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
        xTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchUser.setText("");
            }
        });
        showKeyboard(searchUser);
        return rootView;
    }

    private void sendEventToGoogleAnalytics(String text){
        app.sendAnalyticsEvent("Search Result View", "UX", "SEARCH_TERM", text);
        app.sendAnalyticsEvent("Search Users View", "UX", "SEARCH_TERM", text);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        if(userList == null){
            return;
        }
        super.onListItemClick(l, v, position, id);
        Log.d(TAG, "ITEM CLICKED " + position);
        Intent intent = new Intent(getActivity(), ProfileActivity.class);
        User user = userList.get(position);
        app.setOther(user);
        intent.putExtra("PROFILE_ID", user.getId());
        //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void hideKeyboard() {
        // Check if no view has focus:
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            keyboardOpen = false;
        }
    }

    private void showKeyboard(EditText editText) {
        editText.requestFocus();
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        //InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        //imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
        keyboardOpen = true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        if (item.getItemId() == R.id.action_null) {

        } else if (item.getItemId() == android.R.id.home) {
            hideKeyboard();
        }
        return true;
    }

    public void resetLoader(String text){
        Bundle bundle = new Bundle();
        Log.d(TAG, "RESET LOADER");
        bundle.putString("SEARCH_KEY", text);
        loader = (SearchUserLoader) getActivity().getLoaderManager()
                .restartLoader(Constants.SEARCH_USER_LOADER, bundle, this);
    }

    public void useLoader(String text){
        Log.d(TAG, "USE LOADER - START");
        resetLoader(text);
        loader.forceLoad();
        sendEventToGoogleAnalytics(text);
    }

    @Override
    public Loader<ArrayList<User>> onCreateLoader(int id, Bundle args) {
        String text = args.getString("SEARCH_KEY");
        Log.d(TAG, "ON CREATE LOADER: with text " + text );
        loader = new SearchUserLoader(getActivity(), Constants.SEARCH_USER_LOADER, text);
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(View.VISIBLE);
                userList = null;
                adapter = new GlammerListAdapter(getActivity(),userList);
            }
        });
        return loader;
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<User>> loader, ArrayList<User> data) {
        if(data == null){
            Log.d(TAG, "DATA IS NULL");
            Handler handler = new Handler();
            handler.post(new Runnable() {
                @Override
                public void run() {
                    app.openOKDialog(SearchUserFragment.this.getActivity(), SearchUserFragment.this, "no_connection");
                }
            });

            return;
        }
        Log.d(TAG, "ON LOAD FINISHED: " + data.size());
        userList = data;
        progressBar.setVisibility(View.GONE);
        adapter.addData(userList);
        setListAdapter(adapter);
        if(userList.size() == 0){
            //noDataTv.setVisibility(View.VISIBLE);
        }else{
            noDataTv.setVisibility(View.GONE);
        }
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<User>> loader) {
        //listData.clear();
        //lv.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
    }


//    public class SearchUserTask extends AsyncTask<String, Void, String> {
//        private final String TAG = "SearchUserTask";
//
//        public SearchUserTask(){
//            userList = null;
//            adapter = new GlammerListAdapter(getActivity(),userList);
//        }
//
//        @Override
//        protected String doInBackground(String... params) {
//            String response = "";
//            RestClient restClient = new RestClient();
//            try {
//                String url = new StringBuilder(Constants.SEARCH_USERS).append(params[0]).toString();
//                response = restClient.doGetRequest(url, null);
//                //Log.d(TAG, "User REQUEST RESPONSE: " + response);
//                Gson gson = new GsonBuilder().create();
//                Type collectionType = new TypeToken<Collection<User>>(){}.getType();
//                JsonArray object = new JsonParser().parse(response).getAsJsonObject().getAsJsonArray("data");
//                userList = gson.fromJson(object, collectionType);
//            } catch (Exception e) {
//                response = "NO_CONNECTION";
//                e.printStackTrace();
//            }
//            return response;
//        }
//
//        @Override
//        protected void onPostExecute(String text) {
//            super.onPostExecute(text);
//            progressBar.setVisibility(View.GONE);
//            adapter.addData(userList);
//            setListAdapter(adapter);
//            if(userList.size() == 0){
//                noDataTv.setVisibility(View.VISIBLE);
//            }else{
//                noDataTv.setVisibility(View.GONE);
//            }
//        }
//    }
}
