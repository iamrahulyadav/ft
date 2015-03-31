package com.mallardduckapps.fashiontalks.loaders;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.mallardduckapps.fashiontalks.objects.User;
import com.mallardduckapps.fashiontalks.services.RestClient;
import com.mallardduckapps.fashiontalks.utils.Constants;

import java.lang.reflect.Type;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by oguzemreozcan on 09/03/15.
 */
public class SearchUserLoader extends AsyncTaskLoader<ArrayList<User>> {

    final String TAG = "SearchBrandLoader";
    int loaderId;
    ArrayList<User> userList;
    String searchText;
    URI uri;

    public SearchUserLoader(Context context, int loaderId, String searchText) {
        super(context);
        this.loaderId = loaderId;
        this.searchText = searchText;
        try {
            uri = new URI(searchText.replace(" ", "%20"));
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    @Override
    public ArrayList<User> loadInBackground() {
        String response = "";
        RestClient restClient = new RestClient();
        Log.d(TAG, "TASK START");
        try {
            String url = new StringBuilder(Constants.SEARCH_USERS).append(uri).toString();
            response = restClient.doGetRequest(url, null);
            //Log.d(TAG, "User REQUEST RESPONSE: " + response);
            Gson gson = new GsonBuilder().create();
            Type collectionType = new TypeToken<Collection<User>>(){}.getType();
            JsonArray object = new JsonParser().parse(response).getAsJsonObject().getAsJsonArray("data");
            userList = gson.fromJson(object, collectionType);
        } catch (Exception e) {
            response = "NO_CONNECTION";
            e.printStackTrace();
            return null;
        }
        return userList;
    }

    @Override
    public void deliverResult(ArrayList<User> data) {
        if (isReset()) {
            // An async query came in while the loader is stopped.  We
            // don't need the result.
            if (userList != null) {
                onReleaseResources(userList);
            }
        }

        if (isStarted()) {
            super.deliverResult(data);
        }
    }

    @Override
    protected void onStopLoading() {
        cancelLoad();
    }

    @Override
    public void onCanceled(ArrayList<User> data) {
        super.onCanceled(data);
        onReleaseResources(data);
    }

    @Override
    protected void onReset() {
        super.onReset();
//        Log.d("AppLog", "onReset");
        // Ensure the loader is stopped
        onStopLoading();
        // At this point we can release the resources associated with 'data' if needed.
        if (userList != null) {
            onReleaseResources(userList);

        }
    }

    protected void onReleaseResources(ArrayList<User> data) {
        userList = null;
        //  nothing to do.
    }
}

