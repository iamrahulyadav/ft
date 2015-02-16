package com.mallardduckapps.fashiontalks.tasks;

import android.os.AsyncTask;
import android.util.Log;

import com.mallardduckapps.fashiontalks.components.ExpandablePanel;
import com.mallardduckapps.fashiontalks.services.RestClient;
import com.mallardduckapps.fashiontalks.utils.Constants;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by oguzemreozcan on 08/02/15.
 */

public class GlamTask extends AsyncTask<Void, Void, Integer> {
    private final int pivotId;
    private final int glamCount;
    private AsyncResponse callback;
    private final String TAG = "GlamTask";

    public GlamTask(AsyncResponse panel, int pivotId, int glamCount){
        this.pivotId = pivotId;
        this.glamCount = glamCount;
        callback = panel;
    }

    @Override
    protected Integer doInBackground(Void... params) {
        String response = "";
        RestClient restClient = new RestClient();
        try {
            String url = new StringBuilder(Constants.GLAM_TAG_PREFIX).append(pivotId).toString();
            response = restClient.doGetRequest(url, null);
            JSONObject object = new JSONObject(response);
            int status = object.getInt("status");
            //Log.d(TAG, "RESPONSE FROM API: " + response);
        } catch (Exception e) {
            response = "NO_CONNECTION";
            e.printStackTrace();
        }
        Log.d(TAG, "GLAM RESPONSE: " + response);
        JSONObject object = null;
        int newGlamCount = glamCount;
        try {
            object = new JSONObject(response);
            newGlamCount = object.getJSONObject("data").getInt("glam_count");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return newGlamCount;
    }

    @Override
    protected void onPostExecute(Integer newGlamCount) {
        super.onPostExecute(newGlamCount);
        if(newGlamCount > glamCount){
            Log.d(TAG, "GLAM COUNT INCREASED call process finish");
            callback.processFinish(newGlamCount);
        }
    }

    public interface AsyncResponse {
        void processFinish(int glamCount);
    }
}

