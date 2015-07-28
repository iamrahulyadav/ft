package com.mallardduckapps.fashiontalks.loaders;

/**
 * Created by oguzemreozcan on 22/03/15.
 */

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.mallardduckapps.fashiontalks.objects.Post;
import com.mallardduckapps.fashiontalks.services.RestClient;
import com.mallardduckapps.fashiontalks.utils.Constants;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by oguzemreozcan on 15/02/15.
 */

public class PostLoader extends AsyncTaskLoader<Post> {

    final String TAG = "Post_Loader";
    int loaderId;
    int postId;
    Post post;
    boolean loadingInProgress;


    public PostLoader(Context context, int loaderId, int postId) {
        super(context);
        this.loaderId = loaderId;
        this.postId = postId;
    }

    @Override
    public Post loadInBackground() {
        String response = "";
        RestClient restClient = new RestClient();
        try {
            String url = new StringBuilder(Constants.POST_DETAILS_PREFIX).append(postId).toString();
            response = restClient.doGetRequest(url, null);
            Log.d(TAG, "RESPONSE FROM API: " + response);
        } catch (Exception e) {
            response = "NO_CONNECTION";
            e.printStackTrace();
            return null;
        }
        //HANDLE THIS
        int status = -1;
        //TODO {"status":404,"msg":"post deleted"
        JSONObject object;
        try {
            object = new JSONObject(response);
            String msg = object.getString("msg");
            status = object.getInt("status");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //Caused by: java.lang.IllegalStateException: Not a JSON Object: "NO_CONNECTION"
        if(status == 0){
            JsonElement dataObject = new JsonParser().parse(response).getAsJsonObject().getAsJsonObject("data");
            Exclude ex = new Exclude();
            Gson gson = new GsonBuilder().addDeserializationExclusionStrategy(ex).addSerializationExclusionStrategy(ex).create();
            post = gson.fromJson(dataObject, Post.class);
        }else{
            Post invalidPost = new Post();
            invalidPost.setInvalid(true);
            return invalidPost;
        }

        return post;
    }

    @Override
    public void deliverResult(Post data) {
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
        if (post != null) {
           deliverResult(post);

        }
        if (takeContentChanged() || post == null) {
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


