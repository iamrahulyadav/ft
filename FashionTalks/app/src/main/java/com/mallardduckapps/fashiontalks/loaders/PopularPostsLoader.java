package com.mallardduckapps.fashiontalks.loaders;

/**
 * Created by oguzemreozcan on 19/01/15.
 */

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mallardduckapps.fashiontalks.objects.Gallery;
import com.mallardduckapps.fashiontalks.objects.Post;
import com.mallardduckapps.fashiontalks.services.RestClient;
import com.mallardduckapps.fashiontalks.utils.Constants;

import java.util.ArrayList;

/**
 * Created by oguzemreozcan on 17/01/15.
 */
public class PopularPostsLoader extends AsyncTaskLoader<ArrayList<Post>> {

    final String TAG = "PopularPosts_Loader";
    int loaderId;
    ArrayList<Post> popularPostItems;
    //	public static int pageIndex = 1;
    //public static int perPage = 25;
    //public static boolean noMoreData = false;
    boolean loadingInProgress;
    int startIndex = 0;
    int perPage = 15;

    public PopularPostsLoader(Context context, int loaderId) {
        super(context);
        this.loaderId = loaderId;
        popularPostItems = new ArrayList<Post>();
    }

    @Override
    public ArrayList<Post> loadInBackground() {
        String response = "";
        RestClient restClient = new RestClient();
        try {
            String url = new StringBuilder(Constants.POPULAR_PREFIX).append("/0/14").toString();
            response = restClient.doGetRequest(url, null);
            Log.d(TAG, "RESPONSE FROM API: " + response);
        } catch (Exception e) {
            response = "NO_CONNECTION";
            e.printStackTrace();
        }
/*        JsonObject object = new JsonParser().parse(response).getAsJsonObject();
        Gson gson = new GsonBuilder().create();

        JsonArray dataObjects = object.getAsJsonArray("data");
        for (JsonElement item : dataObjects) {
            Gallery gallery = gson.fromJson(item, Gallery.class);
            galleryItems.add(gallery);
        }*/
        return popularPostItems;
    }

    @Override
    protected void onStartLoading() {
        loadingInProgress = true;
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

