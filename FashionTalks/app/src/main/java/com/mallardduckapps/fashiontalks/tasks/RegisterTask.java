package com.mallardduckapps.fashiontalks.tasks;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mallardduckapps.fashiontalks.fragments.RegisterFragment;
import com.mallardduckapps.fashiontalks.loaders.Exclude;
import com.mallardduckapps.fashiontalks.objects.User;
import com.mallardduckapps.fashiontalks.services.RestClient;
import com.mallardduckapps.fashiontalks.utils.Constants;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by oguzemreozcan on 13/01/15.
 */
public class RegisterTask extends AsyncTask<BasicNameValuePair, Void, String> {

    public static final String TAG = "REGISTER_TASK";
    //LoginActivity activity;
    private int authStatus;
    RegisterTaskCallback callBack;
    String[] tokens;
    final boolean editProfile;
//    String json;

    public RegisterTask(RegisterFragment fragment, final boolean editProfile){
        //this.activity = activity;
        callBack = (RegisterTaskCallback) fragment;
        this.editProfile = editProfile;
    }

    @Override
    protected String doInBackground(BasicNameValuePair... param) {
        String response = "";
        RestClient restClient = new RestClient();
        try {
            if(!editProfile) {
                response = restClient.doPostRequestWithJSON(Constants.REGISTER_PREFIX, null, param);
            }else{
                response = restClient.doPostRequestWithJSON(Constants.GET_USER_PREFIX, null, param);
            }
            Log.d(TAG, "RESPONSE FROM API: " + response);
        } catch ( Exception e) {
            response = "NO_CONNECTION";
            e.printStackTrace();
        }
        return response;
    }

    @Override
    protected void onPostExecute(String response) {
        super.onPostExecute(response);
        try{
            parseToken(response);
        }catch(Exception e){
            callBack.getAuthStatus(Constants.AUTHENTICATION_FAILED, null, null);
            return;
        }

    }

    //    private ArrayList<BasicNameValuePair> getExtraParameters(){
//        ArrayList<BasicNameValuePair> pairs = new ArrayList<BasicNameValuePair>();
//        pairs.add(new BasicNameValuePair("email", email));
//        pairs.add(new BasicNameValuePair("password", password));
//        pairs.add(new BasicNameValuePair("client_id",Constants.CLIENT_ID));
//        pairs.add(new BasicNameValuePair("client_secret",Constants.CLIENT_SECRET));
//        return pairs;
////        pairs.add(new BasicNameValuePair("grant_type", "password"));
//    }

    public interface RegisterTaskCallback {
        void getAuthStatus(int authStatus,User user, String... tokens);
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

        Exclude ex = new Exclude();
        Gson gson = new GsonBuilder().addDeserializationExclusionStrategy(ex).addSerializationExclusionStrategy(ex).create();

        JsonObject dataObject = object.getAsJsonObject("data");
        JsonObject oauthObject = dataObject.getAsJsonObject("OAuth");
        JsonObject userObject = dataObject.getAsJsonObject("User");

        User me = null;
        if(!editProfile){
            me = gson.fromJson(userObject, User.class);
           // Log.d(TAG, "USER NAME: " + me.getFirstName() + "lastName: " + me.getLastName() + " - canPost: " + me.getCanPost());
            JSONObject loginObject = null;
            int status = -1;
            try {
                loginObject = new JSONObject(response);
                status = loginObject.getInt("status");
                if(status == 1002){ //DUBLICATE ENTRY
                    callBack.getAuthStatus(Constants.DUBLICATE_USERNAME, null, null);
                    return;
                }else if(status == 1003){
                    callBack.getAuthStatus(Constants.DUBLICATE_EMAIL, null, null);
                    return;
                }else if(status != 0){
                    callBack.getAuthStatus(Constants.AUTHENTICATION_FAILED, null, null);
                    return;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            accessToken = oauthObject.get("access_token").getAsString();
            refreshToken = oauthObject.get("refresh_token").getAsString();
            callBack.getAuthStatus(Constants.AUTHENTICATION_SUCCESSFUL,me, accessToken, refreshToken);
        }else{
            me = gson.fromJson(dataObject, User.class);
            //Log.d(TAG, "USER NAME UPDATE: " + me.getFirstName() + "lastName: " + me.getLastName() + " - about: " + me.getAbout());
            callBack.getAuthStatus(Constants.PROFILE_EDIT_SUCCESSFUL,me, null, null);
        }

    }
}
