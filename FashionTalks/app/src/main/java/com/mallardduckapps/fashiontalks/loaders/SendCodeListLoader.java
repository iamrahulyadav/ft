package com.mallardduckapps.fashiontalks.loaders;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.mallardduckapps.fashiontalks.objects.User;
import com.mallardduckapps.fashiontalks.services.RestClient;
import com.mallardduckapps.fashiontalks.utils.Constants;

import java.util.ArrayList;

/**
 * Created by oguzemreozcan on 21/02/15.
 */

public class SendCodeListLoader extends AsyncTaskLoader<ArrayList<User>> {

    private ArrayList<User> sendCodeList;
    private final String TAG = "SendCodeListLoader";
    boolean loadingInProgress;

    public SendCodeListLoader(Context context){
        super(context);
    }

    @Override
    public ArrayList<User> loadInBackground() {
        sendCodeList = new ArrayList<User>();
        String response = "";
        RestClient restClient = new RestClient();
        try {
            String url = new StringBuilder(Constants.POST_CODE_REQUEST_PREFIX).toString();
            //append(startIndex).append("/").append(perPage).toString();
            response = restClient.doGetRequest(url, null);
            Log.d(TAG, "RESPONSE FROM API: " + response);
        } catch (Exception e) {
            response = "NO_CONNECTION";
            e.printStackTrace();
            return null;
        }

        JsonArray dataObjects = new JsonParser().parse(response).getAsJsonObject().getAsJsonArray("data");
        Exclude ex = new Exclude();
        Gson gson = new GsonBuilder().addDeserializationExclusionStrategy(ex).addSerializationExclusionStrategy(ex).create();
        for (JsonElement item : dataObjects) {
            User user = gson.fromJson(item, User.class);
            sendCodeList.add(user);
        }
        return sendCodeList;
    }

    @Override
    protected void onStartLoading() {
        loadingInProgress = true;
        //Log.d(TAG, "ON START LOADING");
        if (sendCodeList != null) {
            if (sendCodeList.size() != 0) { // && !refreshData
                // Deliver any previously loaded data immediately.
                deliverResult(sendCodeList);
            }
        }
        if (takeContentChanged() || sendCodeList == null) {
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


