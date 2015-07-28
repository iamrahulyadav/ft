package com.mallardduckapps.fashiontalks.loaders;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.mallardduckapps.fashiontalks.objects.Tag;
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
public class SearchBrandLoader extends AsyncTaskLoader<ArrayList<Tag>> {

    final String TAG = "SearchBrandLoader";
    int loaderId;
    ArrayList<Tag> tagList;
    String searchText;
    URI uri;

    public SearchBrandLoader(Context context, int loaderId, String searchText) {
        super(context);
        this.loaderId = loaderId;
        try {
            uri = new URI(searchText.replace(" ", "%20"));
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        this.searchText = searchText;
        Log.d(TAG, "SEARCH TEXT: " + searchText + " - uri: " + uri);
    }

    @Override
    public ArrayList<Tag> loadInBackground() {
        String response = "";
        RestClient restClient = new RestClient();
        Log.d(TAG, "TAsK START");
        try {
            String url = new StringBuilder(Constants.GLAM_AC_TAG_PREFIX).append(uri).toString();
            response = restClient.doGetRequest(url, null);
            Exclude ex = new Exclude();
            Gson gson = new GsonBuilder().addDeserializationExclusionStrategy(ex).addSerializationExclusionStrategy(ex).create();
            Type collectionType = new TypeToken<Collection<Tag>>(){}.getType();
            JsonArray object = new JsonParser().parse(response).getAsJsonObject().getAsJsonArray("data");
            tagList = gson.fromJson(object, collectionType);
            Log.d(TAG, "RESPONSE FROM API: " + response);
        } catch (Exception e) {
            response = "NO_CONNECTION";
            e.printStackTrace();
            return null;
        }
        return tagList;
    }

    @Override
    public void deliverResult(ArrayList<Tag> data) {
        if (isReset()) {
            // An async query came in while the loader is stopped.  We
            // don't need the result.
            if (tagList != null) {
                onReleaseResources(tagList);
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
    public void onCanceled(ArrayList<Tag> data) {
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
        if (tagList != null) {
            onReleaseResources(tagList);

        }
    }

    protected void onReleaseResources(ArrayList<Tag> data) {
        tagList = null;
        //  nothing to do.
    }
}
