package com.mallardduckapps.fashiontalks.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mallardduckapps.fashiontalks.fragments.LoginFragment;
import com.mallardduckapps.fashiontalks.objects.User;
import com.mallardduckapps.fashiontalks.services.RestClient;
import com.mallardduckapps.fashiontalks.utils.Constants;
import com.mallardduckapps.fashiontalks.utils.FTUtils;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by oguzemreozcan on 10/01/15.
 */
public class LoginTask extends AsyncTask<Void, Void, String> {

    public static final String TAG = "LOGIN_TASK";
    public Context context;
   // LoginActivity activity;
    private int authStatus;
    LoginTaskCallback callBack;
    String[] tokens;
    String email;
    String password;
    boolean getUserInfo = false;
   // String loginUrl;

    public LoginTask(Context context, LoginTaskCallback fragment, String email, String password){
       // this.activity = activity;
        this.email = email;
        this.password = password;
        this.context = context;
        //this.loginUrl = loginUrl;
        callBack = fragment;
    }

    public LoginTask(LoginTaskCallback fragment, Context context){
        // this.activity = activity;
        this.context = context;
        this.email = email;
        this.password = password;
        //this.loginUrl = loginUrl;
        getUserInfo = true;
        callBack = fragment;
    }

    @Override
    protected String doInBackground(Void... param) {
        String response = "";
        RestClient restClient = new RestClient();
        if(!getUserInfo){
            try {
                response = restClient.doPostRequestWithJSON(Constants.LOGIN_PREFIX, null,new BasicNameValuePair("email", email),
                        new BasicNameValuePair("password", password),
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
                //if(!FTUtils.isNetworkAvailable(context)){
                //    Log.d(TAG, "FAILED CONNECTION: " );
                //    callBack.getUser(Constants.AUTHENTICATION_FAILED,null);
                //}else{
                    Log.d(TAG, "NO CONNECTION: " );
                    callBack.getUser(Constants.NO_CONNECTION, null);
                //}

            }
        }
    }

    public interface LoginTaskCallback {
        public void getAuthStatus(int authStatus,User user, String... tokens);
        public void getUser(int authStatus, User user);
    }

    private void parseToken(String response){
        String accessToken = null;
        String refreshToken = null;
        JSONObject loginObject = null;
        int status = -1;
        try {
           loginObject = new JSONObject(response);
           status = loginObject.getInt("status");
           if(status == 1011){
               callBack.getAuthStatus(Constants.WRONG_CREDENTIALS, null, null);
               return;
           }
        } catch (JSONException e) {
            e.printStackTrace();
        }

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
        callBack.getAuthStatus(Constants.AUTHENTICATION_SUCCESSFUL,null, accessToken, refreshToken);
    }
}

