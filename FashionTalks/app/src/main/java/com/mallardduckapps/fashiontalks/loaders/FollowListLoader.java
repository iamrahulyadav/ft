package com.mallardduckapps.fashiontalks.loaders;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.mallardduckapps.fashiontalks.objects.User;
import com.mallardduckapps.fashiontalks.services.RestClient;
import com.mallardduckapps.fashiontalks.utils.Constants;

import java.util.ArrayList;

/**
 * Created by oguzemreozcan on 16/02/15.
 */
public class FollowListLoader extends AsyncTaskLoader<ArrayList<User>> {

    private final int loaderId;
    private final int userId;
    private ArrayList<User> followList;
    private final String TAG = "FollowListLoader";
    boolean loadingInProgress;
    public int startIndex = 0;
    public int perPage = 15;
    boolean followers;

    public FollowListLoader(Context context, int loaderId, int userId, boolean followers){
        super(context);
        this.loaderId = loaderId;
        this.followers = followers;
        this.userId = userId;
    }

    @Override
    public ArrayList<User> loadInBackground() {
        followList = new ArrayList<User>();
        String response = "";
        String prefix = followers ? Constants.FOLLOWERS_PREFIX : Constants.FOLLOWING_PREFIX;
        String userIdTxt = (userId != 0)?Integer.toString(userId).concat("/"):"";
        RestClient restClient = new RestClient();
        try {
            String url = new StringBuilder(prefix).append(userIdTxt).toString();
                    //append(startIndex).append("/").append(perPage).toString();
            response = restClient.doGetRequest(url, null);
            Log.d(TAG, "RESPONSE FROM API: " + response);
        } catch (Exception e) {
            response = "NO_CONNECTION";
            e.printStackTrace();
            return null;
        }

        JsonArray dataObjects = new JsonParser().parse(response).getAsJsonObject().getAsJsonArray("data");
        Gson gson = new Gson();
        String key = followers ? "follower" : "following";
        for (JsonElement item : dataObjects) {
            User user = gson.fromJson(item.getAsJsonObject().get(key), User.class);
            followList.add(user);
        }
        return followList;
    }

    @Override
    protected void onStartLoading() {
        loadingInProgress = true;
        //Log.d(TAG, "ON START LOADING");
        if (followList != null) {
            if (followList.size() != 0) { // && !refreshData
                // Deliver any previously loaded data immediately.
                deliverResult(followList);
            }
        }
        if (takeContentChanged() || followList == null) {
            forceLoad();
        }
    }

    @Override
    public void deliverResult(ArrayList<User> data) {
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

