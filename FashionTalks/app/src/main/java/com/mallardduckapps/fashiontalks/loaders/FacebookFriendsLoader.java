package com.mallardduckapps.fashiontalks.loaders;

import android.app.Activity;
import android.content.AsyncTaskLoader;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.mallardduckapps.fashiontalks.FashionTalksApp;
import com.mallardduckapps.fashiontalks.objects.PopularUser;
import com.mallardduckapps.fashiontalks.objects.User;
import com.mallardduckapps.fashiontalks.services.RestClient;
import com.mallardduckapps.fashiontalks.utils.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by oguzemreozcan on 21/03/15.
 */

public class FacebookFriendsLoader extends AsyncTaskLoader<ArrayList<User>> {

    private final int loaderId;
    private ArrayList<User> usersList;
    private final String TAG = "FB_FRIENDS_Loader";
    boolean loadingInProgress;
    public int startIndex = 0;
    public int perPage = 50;
    FashionTalksApp app;
    public int status = 500;

    public FacebookFriendsLoader(Activity context, int loaderId){
        super(context);
        app = (FashionTalksApp) context.getApplication();
        this.loaderId = loaderId;
    }

    public int getStatus(){
        return status;
    }

    @Override
    public ArrayList<User> loadInBackground() {
        usersList = new ArrayList<User>();
        String response = "";
        RestClient restClient = new RestClient();
        try {
            String url = new StringBuilder(Constants.FB_FRIENDS_PREFIX).toString();//.append("/").append(startIndex).append("/").append(perPage).toString();
            response = restClient.doGetRequest(url, null);
            Log.d(TAG, "RESPONSE FROM API: " + response);
        } catch (Exception e) {
            response = "NO_CONNECTION";
            e.printStackTrace();
            return null;
        }

        try {
            JSONObject object = new JSONObject(response);
            status = object.getInt("status");
            Log.d(TAG, "FB FRIENDS STATUS = " + status);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try{
            JsonArray dataObjects = new JsonParser().parse(response).getAsJsonObject().getAsJsonArray("data");
            Exclude ex = new Exclude();
            Gson gson = new GsonBuilder().addDeserializationExclusionStrategy(ex).addSerializationExclusionStrategy(ex).create();
            for (JsonElement item : dataObjects) {
                PopularUser user = gson.fromJson(item, PopularUser.class);
                //TODO look if followed
                if(user.getIsFollowing() != 1){
                    usersList.add(user);
                }

            }
        }catch(Exception e){
            e.printStackTrace();
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

