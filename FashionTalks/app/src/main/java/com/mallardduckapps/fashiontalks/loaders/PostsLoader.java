package com.mallardduckapps.fashiontalks.loaders;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.mallardduckapps.fashiontalks.objects.Post;
import com.mallardduckapps.fashiontalks.services.RestClient;
import com.mallardduckapps.fashiontalks.utils.Constants;

import java.util.ArrayList;

/**
 * Created by oguzemreozcan on 15/02/15.
 */

public class PostsLoader extends AsyncTaskLoader<ArrayList<Post>> {

    final String TAG = "PopularPosts_Loader";
    int loaderId;
    ArrayList<Post> popularPostItems;
    boolean loadingInProgress;
    public int startIndex = 0;
    public int perPage = 15;
    public int userId = 0; // If there is id
    boolean ownPosts = false;

    public PostsLoader(Context context, int loaderId, int userId) {
        super(context);
        this.loaderId = loaderId;
        this.userId = userId;
        if(loaderId == Constants.MY_POSTS_LOADER_ID){
            ownPosts = true;
        }else{
            ownPosts = false;
        }
    }

    @Override
    public ArrayList<Post> loadInBackground() {
        String response = "";

        popularPostItems = new ArrayList<Post>();
        RestClient restClient = new RestClient();
        try {
            String url = new StringBuilder(Constants.POSTS_BY_USER_PREFIX).append(userId).append("/").append(startIndex).append("/").append(perPage).toString();
            response = restClient.doGetRequest(url, null);
            Log.d(TAG, "RESPONSE FROM API: " + response);
        } catch (Exception e) {
            response = "NO_CONNECTION";
            e.printStackTrace();
            return null;
        }
        //HANDLE THIS
        //Caused by: java.lang.IllegalStateException: Not a JSON Object: "NO_CONNECTION"
        try{
            JsonArray dataObjects = new JsonParser().parse(response).getAsJsonObject().getAsJsonArray("data");
            Exclude ex = new Exclude();
            Gson gson = new GsonBuilder().addDeserializationExclusionStrategy(ex).addSerializationExclusionStrategy(ex).create();
            for (JsonElement item : dataObjects) {
                Post post = gson.fromJson(item, Post.class);
                popularPostItems.add(post);
            }
        }catch(Exception e){
            e.printStackTrace();
        }

        return popularPostItems;
    }

    @Override
    public void deliverResult(ArrayList<Post> data) {
        if (isReset()) {
            Log.d(TAG, "DELIVER LOADER RESET");
            // releaseResources(data);
            return;
        }
        if (isStarted()) {
            // If the Loader is in a started state, deliver the results to the
            // client. The superclass method does this for us.
            loadingInProgress = false;
            super.deliverResult(data);
        }
    }

    @Override
    protected void onStopLoading() {
        // The Loader is in a stopped state, so we should attempt to cancel the
        // current load (if there is one).
        cancelLoad();
        // Note that we leave the observer as is. Loaders in a stopped state
        // should still monitor the data source for changes so that the Loader
        // will know to force a new load if it is ever started again.
    }

    @Override
    protected void onReset() {
        // Ensure the loader has been stopped.
        onStopLoading();
    }

    @Override
    protected void onStartLoading() {
        loadingInProgress = true;
        //Log.d(TAG, "ON START LOADING");
        if (popularPostItems != null) {
            if (popularPostItems.size() != 0) { // && !refreshData
                // Deliver any previously loaded data immediately.
                deliverResult(popularPostItems);
            }
        }
        if (takeContentChanged() || popularPostItems == null) {
            // When the observer detects a change, it should call
            // onContentChanged()
            // on the Loader, which will cause the next call to
            // takeContentChanged()
            // to return true. If this is ever the case (or if the current data
            // is
            // null), we force a new load.
            forceLoad();
        }
    }
}


