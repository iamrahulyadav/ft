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
    private Activity activity;
    FollowCallback callback;

    public FollowTask(FollowCallback callback,Activity activity, boolean follow, int userId){
        this.activity = activity;
        this.follow = follow;
        this.userId = userId;
        this.callback = callback;
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
            }else if(status == 99){
                String message = object.getString("msg");
                onError(message);
            }
            else{
                onError("");
            }
        } catch (Exception e) {
            e.printStackTrace();
            onError("");
        }
    }

    private void onError(String message){
        if(follow){
            callback.isFollowed(false, userId);
            //followButton.setText(activity.getString(R.string.follow));
        }else{
            callback.isUnfollowed(false, userId);
            // followButton.setText(activity.getString(R.string.unfollow));
        }
        showErrorMessage(message);
        Log.e(TAG, "ERROR ON FOLLOW");
    }

    private void showErrorMessage(final String message){

       activity.runOnUiThread(new Runnable() {
           @Override
           public void run() {
               String messageTxt = message;
               if(messageTxt.equals("")){
                   messageTxt = activity.getResources().getString(R.string.problem_occured);
               }
               Toast.makeText(activity, messageTxt, Toast.LENGTH_SHORT).show();
           }
       });
    }

    public interface FollowCallback{
        void isFollowed(boolean success, int userId);
        void isUnfollowed(boolean success, int userId);
    }
}
