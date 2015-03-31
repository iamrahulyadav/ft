package com.mallardduckapps.fashiontalks.loaders;

/**
 * Created by oguzemreozcan on 05/02/15.
 */

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.mallardduckapps.fashiontalks.objects.Notification;
import com.mallardduckapps.fashiontalks.services.RestClient;
import com.mallardduckapps.fashiontalks.utils.Constants;

import java.util.ArrayList;

/**
 * Created by oguzemreozcan on 03/02/15.
 */
public class NotificationListLoader extends AsyncTaskLoader<ArrayList<Notification>> {

    private final int loaderId;
    private ArrayList<Notification> notificationList;
    private final String TAG = "NotificationListLoader";
    boolean loadingInProgress;

    public NotificationListLoader(Context context, int loaderId) {
        super(context);
        this.loaderId = loaderId;
    }

    @Override
    public ArrayList<Notification> loadInBackground() {
        notificationList = new ArrayList<Notification>();
        String response = "";
        RestClient restClient = new RestClient();
        try {
            String url = new StringBuilder(Constants.NOTIFICATION_LIST_PREFIX).toString();
            response = restClient.doGetRequest(url, null);
            Log.d(TAG, "RESPONSE FROM API: " + response);
        } catch (Exception e) {
            response = "NO_CONNECTION";
            e.printStackTrace();
            return null;
        }
        //String object = new JSONObject(response).getJSONObject("data").toString();
        JsonElement element = new JsonParser().parse(response).getAsJsonObject().get("data");
        JsonArray dataObjects = element.getAsJsonObject().getAsJsonArray("notifications");
        Gson gson = new Gson();
        for (JsonElement item : dataObjects) {
            Notification notification = gson.fromJson(item, Notification.class);
            notificationList.add(notification);
        }

        return notificationList;
    }

    @Override
    protected void onStartLoading() {
        loadingInProgress = true;
        //Log.d(TAG, "ON START LOADING");
        if (notificationList != null) {
            if (notificationList.size() != 0) { // && !refreshData
                // Deliver any previously loaded data immediately.
                deliverResult(notificationList);
            }
        }
        if (takeContentChanged() || notificationList == null) {
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
    public void deliverResult(ArrayList<Notification> data) {
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

