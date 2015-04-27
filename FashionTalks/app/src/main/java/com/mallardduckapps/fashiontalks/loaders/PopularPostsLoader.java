package com.mallardduckapps.fashiontalks.loaders;

/**
 * Created by oguzemreozcan on 19/01/15.
 */

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
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
    boolean loadingInProgress;
    public int startIndex = 0;
    public int perPage = 15;
    public int galleryId = 0; // If there is id

    public PopularPostsLoader(Context context, int loaderId, int galleryId) {
        super(context);
        this.loaderId = loaderId;
        this.galleryId = galleryId;
    }

    @Override
    public ArrayList<Post> loadInBackground() {
        String response = "";

        popularPostItems = new ArrayList<Post>();
        RestClient restClient = new RestClient();
        try {
            String url = new StringBuilder(getLoaderPrefix()).append("/").append(startIndex).append("/").append(perPage).toString();
            response = restClient.doGetRequest(url, null);
            Log.d(TAG, "RESPONSE FROM API: " + response);
        } catch (Exception e) {
            response = "NO_CONNECTION";
            e.printStackTrace();
            return null;
        }
        //HANDLE THIS
        //Caused by: java.lang.IllegalStateException: Not a JSON Object: "NO_CONNECTION"
        JsonArray dataObjects = new JsonParser().parse(response).getAsJsonObject().getAsJsonArray("data");
        Gson gson = new Gson();

        if (loaderId == Constants.GALLERY_POSTS_BY_TAG_LOADER_ID) {
            for (JsonElement item : dataObjects) {
                JsonObject object = item.getAsJsonObject();
                String json = object.get("post").toString();
               // Log.d(TAG, "POST: " + json);
                Post post = gson.fromJson(json, Post.class);
                popularPostItems.add(post);
            }
        } else if (galleryId == 0) {
            for (JsonElement item : dataObjects) {
                Post post = gson.fromJson(item, Post.class);
                popularPostItems.add(post);
            }
            //popularPostItems = gson.fromJson(dataObjects, popularPostItems.getClass() );
            //System.out.println(gson.toJson(popularPostItems));

        } else {
            JsonArray postObjects = dataObjects.get(0).getAsJsonObject().getAsJsonArray("posts");
            for (JsonElement item : postObjects) {
                Post post = gson.fromJson(item, Post.class);
                popularPostItems.add(post);
            }
        }
        return popularPostItems;
    }

    protected String getLoaderPrefix() {
        String prefix = "";
        switch (loaderId) {
            case Constants.POPULAR_POSTS_LOADER_ID:
                prefix = Constants.POPULAR_PREFIX;
                break;
            case Constants.FEED_POSTS_LOADER_ID:
                prefix = Constants.FEED_PREFIX;
                break;
            case Constants.GALLERY_POSTS_LOADER_ID:
                prefix = Constants.GALLERY_POSTS_PREFIX + "/" + galleryId;
                break;
            case Constants.GALLERY_POSTS_BY_TAG_LOADER_ID:
                prefix = Constants.GALLERY_POSTS_BY_TAG_PREFIX + galleryId;

        }
        return prefix;
    }

    @Override
    public void deliverResult(ArrayList<Post> data) {

        //  Log.d(TAG, "LOADER DELIVER LOADER RESULT");
        if (isReset()) {
            // The Loader has been reset; ignore the result and invalidate the
            // data.
//			Log.d(TAG, "IS RESET TRUE: ");
            Log.d(TAG, "DELIVER LOADER RESET");
            // releaseResources(data);
            return;
        }
        // Hold a reference to the old data so it doesn't get garbage collected.
        // We must protect it until the new data has been delivered.
        // if(!refreshData){
        //ArrayList<Post> oldData = popularPostItems;
        // popularPostItems = data;
        if (isStarted()) {
            // If the Loader is in a started state, deliver the results to the
            // client. The superclass method does this for us.
            loadingInProgress = false;
            super.deliverResult(data);
        }
        // Invalidate the old data as we don't need it any more.

        //  if (oldData != null && oldData != data) {
        // releaseResources(oldData);
        // }
        //Log.d(TAG, "DELIVER LOADER RESULT FINAL :" + data.size() );
        //Log.d(TAG, "DELIVER LOADER RESULT FINAL :" + data.get(0).getTag() );
        //Log.d(TAG, "DELIVER LOADER RESULT FINAL :" + data.get(data.size()-1).getTag() );
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
        // At this point we can release the resources associated with
        // 'contacts'.
        // if (expenseItems != null) {
        //     releaseResources(expenseItems);
        //    expenseItems = null;
        //}

        // The Loader is being reset, so we should stop monitoring for changes.
        // if (mObserver != null) {
        // // TODO: unregister the observer
        // mObserver = null;
        // }
    }

    @Override
    protected void onStartLoading() {
        loadingInProgress = true;
        //Log.d(TAG, "ON START LOADING");
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

