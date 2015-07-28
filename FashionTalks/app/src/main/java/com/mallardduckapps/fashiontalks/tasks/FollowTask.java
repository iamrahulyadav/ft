package com.mallardduckapps.fashiontalks.tasks;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.mallardduckapps.fashiontalks.R;
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
    //private Button followButton;
    private Activity activity;
    FollowCallback callback;
    //TODO add listener to get callback value

    public FollowTask(FollowCallback callback,Activity activity, boolean follow, int userId, Button followButton){
        this.activity = activity;
        this.follow = follow;
        this.userId = userId;
        //this.followButton = followButton;
        this.callback = callback;
    }

    public FollowTask(FollowCallback callback, boolean follow, int userId){
        //this.activity = activity;
        this.follow = follow;
        this.userId = userId;
        this.callback = callback;
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
                    callback.isFollowed(true, userId);
                    //followButton.setText(activity.getString(R.string.follow));
                }else{
                    callback.isUnfollowed(true, userId);
                   // followButton.setText(activity.getString(R.string.unfollow));
                }
            }else{
                onError();
            }
        } catch (Exception e) {
            e.printStackTrace();
            onError();
        }
    }

    private void onError(){
        if(follow){
            callback.isFollowed(false, userId);
            //followButton.setText(activity.getString(R.string.follow));
        }else{
            callback.isUnfollowed(false, userId);
            // followButton.setText(activity.getString(R.string.unfollow));
        }
        showErrorMessage();
        Log.e(TAG, "ERROR ON FOLLOW");
    }

    private void showErrorMessage(){
       activity.runOnUiThread(new Runnable() {
           @Override
           public void run() {
               Toast.makeText(activity, activity.getResources().getString(R.string.problem_occured), Toast.LENGTH_SHORT).show();
           }
       });
    }

    public interface FollowCallback{
        void isFollowed(boolean success, int userId);
        void isUnfollowed(boolean success, int userId);
    }
}
