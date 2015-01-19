package com.mallardduckapps.fashiontalks.tasks;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mallardduckapps.fashiontalks.LoginActivity;
import com.mallardduckapps.fashiontalks.fragments.LoginFragment;
import com.mallardduckapps.fashiontalks.objects.User;
import com.mallardduckapps.fashiontalks.services.RestClient;
import com.mallardduckapps.fashiontalks.utils.Constants;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by oguzemreozcan on 10/01/15.
 */
public class LoginTask extends AsyncTask<Void, Void, String> {

    public static final String TAG = "LOGIN_TASK";
   // LoginActivity activity;
    private int authStatus;
    LoginTaskCallback callBack;
    String[] tokens;
    String email;
    String password;
   // String loginUrl;

    public LoginTask(LoginFragment fragment, String email, String password){
       // this.activity = activity;
        this.email = email;
        this.password = password;
        //this.loginUrl = loginUrl;
        callBack = (LoginTaskCallback) fragment;
    }

    @Override
    protected String doInBackground(Void... param) {
        String response = "";
        RestClient restClient = new RestClient();
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

        return response;
    }

    @Override
    protected void onPostExecute(String response) {
        parseToken(response);
    }

    public interface LoginTaskCallback {
        public void getAuthStatus(int authStatus,User user, String... tokens);
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

        callBack.getAuthStatus(Constants.AUTHENTICATION_SUCCESSFUL,null, accessToken, refreshToken);
    }
}

