package com.mallardduckapps.fashiontalks.fragments;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
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
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mallardduckapps.fashiontalks.BaseActivity;
import com.mallardduckapps.fashiontalks.FashionTalksApp;
import com.mallardduckapps.fashiontalks.GalleryActivity;
import com.mallardduckapps.fashiontalks.R;
import com.mallardduckapps.fashiontalks.loaders.SearchBrandLoader;
import com.mallardduckapps.fashiontalks.objects.Tag;
import com.mallardduckapps.fashiontalks.utils.Constants;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class SearchBrandFragment extends ListFragment implements LoaderManager.LoaderCallbacks<ArrayList<Tag>> {
    private boolean keyboardOpen;
    private ArrayList<Tag> tags;
    private ArrayList<String> listData;
    private Timer timer;
    private final long DELAY = 500; // in ms
    public final String TAG = "Search_Brand";
    private EditText searchBrand;
    private TextView noDataTv;
    private TextView xTv;
    private ProgressBar progressBar;
    FashionTalksApp app;
    private ArrayAdapter<String> adapter;
    SearchBrandLoader loader;

    public static SearchBrandFragment newInstance() {
        SearchBrandFragment fragment = new SearchBrandFragment();
        return fragment;
    }

    public SearchBrandFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        app = (FashionTalksApp)getActivity().getApplication();
        tags = new ArrayList<>();
        listData = new ArrayList<>();
        timer = new Timer();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(
                R.layout.search_list_layout, container, false);
        searchBrand = (EditText) rootView.findViewById(R.id.searchEditText);
        searchBrand.setHint(getString(R.string.search_brand));
        progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);
        noDataTv = (TextView) rootView.findViewById(R.id.noDataTv);
        xTv = (TextView) rootView.findViewById(R.id.xTv);
        xTv.setVisibility(View.GONE);
        searchBrand.addTextChangedListener(new TextWatcher() {
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
                if(t.equals(text)){
                    return;
                }
                text = t.toLowerCase(Locale.getDefault());
                //Log.d(TAG, "TEXT: " + text);
                if (text.length() > 1) {
                    xTv.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.VISIBLE);
                    noDataTv.setVisibility(View.GONE);
                    timer = new Timer();
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            SearchBrandFragment.this.getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    useLoader(text);
                                    //SearchUserTask task = new SearchUserTask();
                                    //task.execute(text);
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
                searchBrand.setText("");
            }
        });
        showKeyboard(searchBrand);
        return rootView;
    }

    private void sendEventToGoogleAnalytics(String text){
        app.sendAnalyticsEvent("Search Result View", "UX", "SEARCH_TERM", text);
        app.sendAnalyticsEvent("Search Brands View", "UX", "SEARCH_TERM", text);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        if(listData == null || tags == null){
            return;
        }
        Log.d(TAG, "ITEM CLICKED " + position);
        Intent intent = new Intent(getActivity(), GalleryActivity.class);
        intent.putExtra("GALLERY_ID", tags.get(position).getId());
        intent.putExtra("GALLERY_NAME", listData.get(position));
        intent.putExtra("LOADER_ID", Constants.GALLERY_POSTS_BY_TAG_LOADER_ID);
        //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        getActivity().startActivity(intent);
        BaseActivity.setTranslateAnimation(getActivity());

/*        Intent intent = new Intent(getActivity(), ProfileActivity.class);
        User user = userList.get(position);
        app.setOther(user);
        intent.putExtra("PROFILE_ID", user.getId());
        //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);*/
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

        //InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        //imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
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

    public void filter() {
        //charText = charText.toLowerCase(); // Locale.getDefault()
        //Log.d(TAG, "CHAR TEXT: " + charText + " tags.length: " + tags.size());
        progressBar.setVisibility(View.GONE);
        listData.clear();
        //lv.setVisibility(View.GONE);
        if(tags.size() == 0){
            //noDataTv.setVisibility(View.VISIBLE);
            return;
        }else{
            noDataTv.setVisibility(View.GONE);
        }

        for(Tag tag : tags){
            listData.add(tag.getTag());
        }

        if(adapter == null){
            try{
                adapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_list_item_1, listData);
                setListAdapter(adapter);
            }catch(Exception e){
                e.printStackTrace();
                return;
            }

        }else{
            adapter.notifyDataSetChanged();
        }
    }


    public void resetLoader(String text){
        Bundle bundle = new Bundle();
        Log.d(TAG, "RESET LOADER");
        bundle.putString("SEARCH_KEY", text);
        loader = (SearchBrandLoader) getActivity().getLoaderManager()
                .restartLoader(Constants.SEARCH_TAG_LOADER, bundle, this);
    }

    public void useLoader(String text){
        Log.d(TAG, "USE LOADER - START");
        if(text == null ){
            return;
        }
        if(text.equals("")){
            return;
        }
        resetLoader(text);
        loader.forceLoad();
        sendEventToGoogleAnalytics(text);
    }

    @Override
    public android.content.Loader<ArrayList<Tag>> onCreateLoader(int id, Bundle args) {
        String text = args.getString("SEARCH_KEY");
        Log.d(TAG, "ON CREATE LOADER: with text " + text );

        loader = new SearchBrandLoader(getActivity(), Constants.SEARCH_TAG_LOADER, text);
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(View.VISIBLE);
            }
        });

        return loader;
    }

    @Override
    public void onLoadFinished(android.content.Loader<ArrayList<Tag>> loader, ArrayList<Tag> data) {
        if(data == null){
            Log.d(TAG, "DATA IS NULL");
            Handler handler = new Handler();
            handler.post(new Runnable() {
                @Override
                public void run() {
                    app.openOKDialog(SearchBrandFragment.this.getActivity(), SearchBrandFragment.this, "no_connection");
                }
            });

            return;
        }

        Log.d(TAG, "ON LOAD FINISHED: " + data.size());
        tags = data;
        progressBar.setVisibility(View.GONE);
        filter();
    }

    @Override
    public void onLoaderReset(android.content.Loader<ArrayList<Tag>> loader) {
        listData.clear();
        //lv.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
    }
}

