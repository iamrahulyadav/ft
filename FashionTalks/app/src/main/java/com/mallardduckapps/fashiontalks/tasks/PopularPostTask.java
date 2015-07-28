package com.mallardduckapps.fashiontalks.tasks;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mallardduckapps.fashiontalks.loaders.Exclude;
import com.mallardduckapps.fashiontalks.objects.Post;
import com.mallardduckapps.fashiontalks.services.RestClient;
import com.mallardduckapps.fashiontalks.utils.Constants;

import java.util.ArrayList;

/**
 * Created by oguzemreozcan on 20/05/15.
 */
public class PopularPostTask extends AsyncTask<Void,Void, String> {

    final String TAG = "PopularPosts_Task";
    int loaderId;

    boolean loadingInProgress;
    public int startIndex = 0;
    public int perPage = 15;
    public int galleryId = 0; // If there is id
    public int userId;
    //Context context;
    NewPostsLoaded callback;

    public PopularPostTask(NewPostsLoaded callback, int loaderId, int galleryId, int startIndex, int userId, int perPage){
        this.callback = callback;
        this.loaderId = loaderId;
        this.galleryId = galleryId;
        this.startIndex = startIndex ;
        this.perPage = perPage;
        this.userId = userId;
    }

    @Override
    protected String doInBackground(Void... params) {
        String response = "";

        ArrayList<Post> popularPostItems = new ArrayList<Post>();
        RestClient restClient = new RestClient();
        try {
            String url = new StringBuilder(getLoaderPrefix()).append("/").append(startIndex).append("/").append(perPage).toString();
            response = restClient.doGetRequest(url, null);
            Log.d(TAG, "**RESPONSE FROM API p.post task: " + response);
        } catch (Exception e) {
            response = "NO_CONNECTION";
            e.printStackTrace();
            return null;
        }
        //HANDLE THIS
        //Caused by: java.lang.IllegalStateException: Not a JSON Object: "NO_CONNECTION"
        try{
            JsonArray dataObjects = new JsonParser().parse(response).getAsJsonObject().getAsJsonArray("data");
            Exclude ex = new Exclude();
            Gson gson = new GsonBuilder().addDeserializationExclusionStrategy(ex).addSerializationExclusionStrategy(ex).create();

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
        }catch(Exception e){
            e.printStackTrace();
        }

        callback.getNewPosts(popularPostItems);
        return response;
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
                break;
            case Constants.USER_POSTS_LOADER_ID:
                prefix =  Constants.POSTS_BY_USER_PREFIX + userId;
                break;
            case Constants.MY_POSTS_LOADER_ID:
                prefix = Constants.POSTS_BY_USER_PREFIX + userId;
                break;

        }
        return prefix;
    }

    public interface NewPostsLoaded{
        void getNewPosts(ArrayList<Post> posts);
    }
}
