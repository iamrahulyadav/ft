package com.mallardduckapps.fashiontalks.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mallardduckapps.fashiontalks.objects.User;
import com.mallardduckapps.fashiontalks.services.RestClient;
import com.mallardduckapps.fashiontalks.utils.Constants;
import com.mallardduckapps.fashiontalks.utils.FTUtils;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;

import java.io.IOException;

/**
 * Created by oguzemreozcan on 28/03/15.
 */
public class LoginFBTask extends AsyncTask<Void, Void, String> {

    public static final String TAG = "LOGIN_FB_TASK";
    public Context context;
    private int authStatus;
    LoginTask.LoginTaskCallback callBack;
    String[] tokens;
    String fbToken;
    boolean getUserInfo = false;

    public LoginFBTask(Context context, LoginTask.LoginTaskCallback fragment, String fbToken){
        this.context = context;
        this.fbToken = fbToken;
        callBack = (LoginTask.LoginTaskCallback) fragment;
    }

    public LoginFBTask(LoginTask.LoginTaskCallback fragment, Context context){
        this.context = context;
        getUserInfo = true;
        callBack = fragment;
    }

    @Override
    protected String doInBackground(Void... param) {
        String response = "";
        RestClient restClient = new RestClient();
        if(!getUserInfo){
            try {
                response = restClient.doPostRequestWithJSON(Constants.LOGIN_FB, null,new BasicNameValuePair("access_token", fbToken),
                        new BasicNameValuePair("client_id", Constants.CLIENT_ID),
                        new BasicNameValuePair("client_secret", Constants.CLIENT_SECRET));
                Log.d(TAG, "RESPONSE FROM API: " + response);
            } catch ( Exception e) {
                response = "NO_CONNECTION";
                e.printStackTrace();
            }
        }else{
            try {
                //restClient.setAccessToken(token);
                response = restClient.doGetRequest(Constants.GET_USER_PREFIX, null);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return response;
    }

    @Override
    protected void onPostExecute(String response) {
        if(!getUserInfo){
            parseToken(response);
        }else{
            Log.d(TAG, "GET USER: " + response);
            try{
                Gson gson = new GsonBuilder().create();
                JsonObject object = new JsonParser().parse(response).getAsJsonObject();
                JsonObject dataObject = object.getAsJsonObject("data");
                JsonObject userObject = dataObject.getAsJsonObject("User");
                User me = gson.fromJson(userObject, User.class);
                callBack.getUser(Constants.AUTHENTICATION_SUCCESSFUL, me);
            }catch(IllegalStateException e){
                Log.d(TAG, "EXCEPTION: " );
                e.printStackTrace();
                if(FTUtils.isNetworkAvailable(context)){
                    Log.d(TAG, "FAILED CONNECTION: " );
                    callBack.getUser(Constants.AUTHENTICATION_FAILED,null);
                }else{
                    Log.d(TAG, "NO CONNECTION: " );
                    callBack.getUser(Constants.NO_CONNECTION, null);
                }

            }
        }
    }

    private void parseToken(String response){
        String accessToken = null;
        String refreshToken = null;

        JsonObject object = null;
        try{
            object = new JsonParser().parse(response).getAsJsonObject();
        }catch(IllegalStateException ex){
            callBack.getAuthStatus(Constants.NO_CONNECTION, null, null);
        }

        Gson gson = new GsonBuilder().create();

        JsonObject dataObject = object.getAsJsonObject("data");
        JsonObject oauthObject = dataObject.getAsJsonObject("OAuth");
        JsonObject userObject = dataObject.getAsJsonObject("User");
        accessToken = oauthObject.get("access_token").getAsString();
        refreshToken = oauthObject.get("refresh_token").getAsString();
        User me = gson.fromJson(userObject, User.class);

        Log.d(TAG, "USER NAME: " + me.getFirstName() + "lastName: " + me.getLastName() + " - canPost: " + me.getCanPost());
        callBack.getUser(Constants.AUTHENTICATION_SUCCESSFUL, me);
        callBack.getAuthStatus(Constants.FB_AUTHENTICATION_SUCCESSFUL,null, accessToken, refreshToken);
    }
}


