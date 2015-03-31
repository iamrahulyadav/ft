package com.mallardduckapps.fashiontalks.loaders;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.mallardduckapps.fashiontalks.objects.PopularUser;
import com.mallardduckapps.fashiontalks.services.RestClient;
import com.mallardduckapps.fashiontalks.utils.Constants;

import java.util.ArrayList;

/**
 * Created by oguzemreozcan on 04/02/15.
 */
public class PopularUsersLoader extends AsyncTaskLoader<ArrayList<PopularUser>> {

    private final int loaderId;
    private ArrayList<PopularUser> usersList;
    private final String TAG = "PopularUsersLoader";
    boolean loadingInProgress;
    public int startIndex = 0;
    public int perPage = 50;

    public PopularUsersLoader(Context context, int loaderId){
        super(context);
        this.loaderId = loaderId;
    }

    @Override
    public ArrayList<PopularUser> loadInBackground() {
        usersList = new ArrayList<PopularUser>();
        String response = "";
        RestClient restClient = new RestClient();
        try {
            String url = new StringBuilder(Constants.POPULAR_USERS_PREFIX).append("/").append(startIndex).append("/").append(perPage).toString();
            response = restClient.doGetRequest(url, null);
            Log.d(TAG, "RESPONSE FROM API: " + response);
        } catch (Exception e) {
            response = "NO_CONNECTION";
            e.printStackTrace();
            return null;
        }

        JsonArray dataObjects = new JsonParser().parse(response).getAsJsonObject().getAsJsonArray("data");
        Gson gson = new Gson();
        for (JsonElement item : dataObjects) {
            PopularUser user = gson.fromJson(item, PopularUser.class);
            usersList.add(user);
        }
        return usersList;
    }


    @Override
    protected void onStartLoading() {
        loadingInProgress = true;
        //Log.d(TAG, "ON START LOADING");
        if (usersList != null) {
            if (usersList.size() != 0) { // && !refreshData
                // Deliver any previously loaded data immediately.
                deliverResult(usersList);
            }
        }
        if (takeContentChanged() || usersList == null) {
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
    public void deliverResult(ArrayList<PopularUser> data) {
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
