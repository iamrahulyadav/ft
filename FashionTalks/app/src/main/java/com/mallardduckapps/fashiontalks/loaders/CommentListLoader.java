package com.mallardduckapps.fashiontalks.loaders;

/**
 * Created by oguzemreozcan on 10/02/15.
 */

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.mallardduckapps.fashiontalks.objects.Comment;
import com.mallardduckapps.fashiontalks.services.RestClient;
import com.mallardduckapps.fashiontalks.utils.Constants;

import java.util.ArrayList;

public class CommentListLoader extends AsyncTaskLoader<ArrayList<Comment>> {

    private final int loaderId;
    private final String postId;
    private ArrayList<Comment> commentList;
    private final String TAG = "CommentListLoader";
    boolean loadingInProgress;

    public CommentListLoader(Context context, int loaderId, String postId){
        super(context);
        this.loaderId = loaderId;
        this.postId = postId;
    }

    @Override
    public ArrayList<Comment> loadInBackground() {
        commentList = new ArrayList<Comment>();
        String response = "";
        RestClient restClient = new RestClient();
        try {
            String url = new StringBuilder(Constants.POST_COMMENTS).append(postId).toString();
            response = restClient.doGetRequest(url, null);
            Log.d(TAG, "RESPONSE FROM API: " + response);
        } catch (Exception e) {
            response = "NO_CONNECTION";
            e.printStackTrace();
        }

        JsonArray dataObjects = new JsonParser().parse(response).getAsJsonObject().getAsJsonArray("data");
        Gson gson = new Gson();
        for (JsonElement item : dataObjects) {
            Comment comment = gson.fromJson(item, Comment.class);
            commentList.add(comment);
        }
        return commentList;
    }

    @Override
    protected void onStartLoading() {
        loadingInProgress = true;
        //Log.d(TAG, "ON START LOADING");
        if (commentList != null) {
            if (commentList.size() != 0) { // && !refreshData
                // Deliver any previously loaded data immediately.
                deliverResult(commentList);
            }
        }
        if (takeContentChanged() || commentList == null) {
            forceLoad();
        }
    }

    @Override
    public void deliverResult(ArrayList<Comment> data) {
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
}

