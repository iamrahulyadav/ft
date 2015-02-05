package com.mallardduckapps.fashiontalks.loaders;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.mallardduckapps.fashiontalks.objects.Post;
import com.mallardduckapps.fashiontalks.objects.User;
import com.mallardduckapps.fashiontalks.services.RestClient;
import com.mallardduckapps.fashiontalks.utils.Constants;

import java.util.ArrayList;

/**
 * Created by oguzemreozcan on 03/02/15.
 */
public class GlammerListLoader extends AsyncTaskLoader<ArrayList<User>> {

    private final int loaderId;
    private final String postId;
    private ArrayList<User> glammerList;
    private final String TAG = "GlammerListLoader";
    boolean loadingInProgress;

    public GlammerListLoader(Context context, int loaderId, String postId){
        super(context);
        this.loaderId = loaderId;
        this.postId = postId;
    }

    @Override
    public ArrayList<User> loadInBackground() {
        glammerList = new ArrayList<User>();
        String response = "";
        RestClient restClient = new RestClient();
        try {
            String url = new StringBuilder(Constants.GLAMMER_LIST_PREFIX).append(postId).toString();
            response = restClient.doGetRequest(url, null);
            Log.d(TAG, "RESPONSE FROM API: " + response);
        } catch (Exception e) {
            response = "NO_CONNECTION";
            e.printStackTrace();
        }

        JsonArray dataObjects = new JsonParser().parse(response).getAsJsonObject().getAsJsonArray("data");
        Gson gson = new Gson();
        for (JsonElement item : dataObjects) {
            User user = gson.fromJson(item.getAsJsonObject().get("user"), User.class);
            glammerList.add(user);
        }
        return glammerList;
    }

    @Override
    protected void onStartLoading() {
        loadingInProgress = true;
        //Log.d(TAG, "ON START LOADING");
        if (glammerList != null) {
            if (glammerList.size() != 0) { // && !refreshData
                // Deliver any previously loaded data immediately.
                deliverResult(glammerList);
            }
        }
        if (takeContentChanged() || glammerList == null) {
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
