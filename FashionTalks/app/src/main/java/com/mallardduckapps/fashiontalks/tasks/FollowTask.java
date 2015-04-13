package com.mallardduckapps.fashiontalks.tasks;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.mallardduckapps.fashiontalks.services.RestClient;
import com.mallardduckapps.fashiontalks.utils.Constants;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by oguzemreozcan on 03/02/15.
 */
public class FollowTask extends AsyncTask<Void, Void, String> {

    private final boolean follow;
    private final int userId;
    private final String TAG = "Follow_TASK";
    private Button followButton;
    private Activity activity;
    //TODO add lsitener to get callback value

    public FollowTask(Activity activity, boolean follow, int userId, Button followButton){
        this.activity = activity;
        this.follow = follow;
        this.userId = userId;
        this.followButton = followButton;
    }

    public FollowTask(boolean follow, int userId){
        //this.activity = activity;
        this.follow = follow;
        this.userId = userId;
        //this.followButton = followButton;
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
            Log.d(TAG, "RESPONSE FROM API: " + response);
        } catch (Exception e) {
            response = "NO_CONNECTION";
            e.printStackTrace();
        }
        return response;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        if(s.equals("") || activity == null){
            return;
        }
        int status = -1;

        JSONObject object = null;
        try {
            object = new JSONObject(s);
            status = object.getInt("status");
            if(status == 0){
                if(follow){
                    //followButton.setText(activity.getString(R.string.follow));
                }else{
                   // followButton.setText(activity.getString(R.string.unfollow));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
