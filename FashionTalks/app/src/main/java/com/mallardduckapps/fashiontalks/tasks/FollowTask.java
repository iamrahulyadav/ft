package com.mallardduckapps.fashiontalks.tasks;

import android.os.AsyncTask;
import android.util.Log;

import com.mallardduckapps.fashiontalks.services.RestClient;
import com.mallardduckapps.fashiontalks.utils.Constants;

import org.json.JSONObject;

/**
 * Created by oguzemreozcan on 03/02/15.
 */
public class FollowTask extends AsyncTask<Void, Void, String> {

    private final boolean follow;
    private final int userId;
    private final String TAG = "Follow_TASK";

    public FollowTask(boolean follow, int userId){
        this.follow = follow;
        this.userId = userId;
    }

    @Override
    protected String doInBackground(Void... params) {
        String response = "";
        RestClient restClient = new RestClient();
        try {
            String url = null;
            if(follow){
                url = new StringBuilder(Constants.FOLLOW_USER_PREFIX).append(userId).toString();
            }else{
                url = new StringBuilder(Constants.UNFOLLOW_USER_PREFIX).append(userId).toString();
            }
            response = restClient.doGetRequest(url, null);
            JSONObject object = new JSONObject(response);
            int status = object.getInt("status");
            Log.d(TAG, "RESPONSE FROM API: " + response);
        } catch (Exception e) {
            response = "NO_CONNECTION";
            e.printStackTrace();
        }
        return response;
    }
}
