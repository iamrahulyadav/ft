package com.mallardduckapps.fashiontalks.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.mallardduckapps.fashiontalks.objects.BasicNameValuePair;
import com.mallardduckapps.fashiontalks.services.RestClient;
import com.mallardduckapps.fashiontalks.utils.Constants;
/**
 * Created by oguzemreozcan on 28/03/15.
 */
public class ConnectFBTask extends AsyncTask<Void, Void, String> {
    public static final String TAG = "CONNECT_FB_TASK";
   // LoginTask.LoginTaskCallback callBack;
    public Context context;
    String fbToken;
    boolean taskForLogin; //if false just connects account to fb

    public ConnectFBTask( String fbToken, boolean taskForLogin){
        this.fbToken = fbToken;
        this.taskForLogin = taskForLogin;
        //callBack = (LoginTask.LoginTaskCallback) fragment;
    }

    @Override
    protected String doInBackground(Void... param) {
        String response = "";
        RestClient restClient = new RestClient();
        if(!taskForLogin){
            try {

                response = restClient.doPostRequestWithJSON(Constants.CONNECT_FB, null,new BasicNameValuePair("access_token", fbToken));
                Log.d(TAG, "RESPONSE FROM API: " + response);
            } catch ( Exception e) {
                response = "NO_CONNECTION";
                e.printStackTrace();
            }
        }else{
            try {
                response = restClient.doPostRequestWithJSON(Constants.LOGIN_FB, null,new BasicNameValuePair("access_token", fbToken),
                new BasicNameValuePair("client_id", Constants.CLIENT_ID),
                        new BasicNameValuePair("client_secret", Constants.CLIENT_SECRET));
                Log.d(TAG, "RESPONSE FROM API: " + response);
            } catch ( Exception e) {
                response = "NO_CONNECTION";
                e.printStackTrace();
            }

        }

        return response;
    }

    @Override
    protected void onPostExecute(String response) {
        if(response.equals("NO_CONNECTION")){
            //TODO OPEN DIALOG
            return;
        }



    }

}
