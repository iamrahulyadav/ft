package com.mallardduckapps.fashiontalks.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.mallardduckapps.fashiontalks.services.RestClient;
import com.mallardduckapps.fashiontalks.utils.Constants;
import com.mallardduckapps.fashiontalks.utils.DataSaver;
import com.mallardduckapps.fashiontalks.utils.FTUtils;

import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;

/**
 * Created by oguzemreozcan on 13/01/15.
 */
public class GCMTask {

    GoogleCloudMessaging gcm;
    Context context;
    DataSaver dataSaver;
    String regId;

    public GCMTask(Context context, DataSaver dataSaver){
        gcm = GoogleCloudMessaging.getInstance(context);
        this.context = context.getApplicationContext();
        this.dataSaver = dataSaver;
    }

    public void registerInBackground() {
        new AsyncTask() {
            @Override
            protected String doInBackground(Object... params) {
                String msg = "";
                try {
                    if (gcm == null) {
                        gcm = GoogleCloudMessaging.getInstance(context);
                    }
                    regId = gcm.register(Constants.SENDER_ID);
                    msg = "Device registered, registration ID=" + regId;
                    Log.d("GCM_TASK", msg);

                    //Toast.makeText(context,"DEVICE REGISTERED: " + regId, Toast.LENGTH_LONG).show();
                    // You should send the registration ID to your server over HTTP,
                    // so it can use GCM/HTTP or CCS to send messages to your app.
                    // The request to your server should be authenticated if your app
                    // is using accounts.
                    sendRegistrationIdToBackend(context);
                    int appVersion = FTUtils.getAppVersion(context);
                    dataSaver.putInt("APP_VERSION", appVersion);
                    dataSaver.putString("REGISTRATION_ID",regId);
                    dataSaver.save();
                    //storeRegistrationId(context, regid);
                } catch (IOException ex) {
                    msg = "Error :" + ex.getMessage();
                    // If there is an error, don't just keep trying to register.
                    // Require the user to click a button again, or perform
                    // exponential back-off.
                }
                return msg;
            }

            @Override
            protected void onPostExecute(Object message) {
                super.onPostExecute(message);
            }
        }.execute(null, null, null);

    }

    private void sendRegistrationIdToBackend(Context context){

        //Toast.makeText(context,"DEVICE REGISTERED: " + regId, Toast.LENGTH_LONG).show();
        String response = "";
        RestClient restClient = new RestClient();
        String accessToken = dataSaver.getString("accessToken");
//      ArrayList<BasicNameValuePair> nameValuePairs = new ArrayList<BasicNameValuePair>(params.length);
        //BasicNameValuePair param = new BasicNameValuePair("Authorization","bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1bmlxdWVfbmFtZSI6Im9ndXpAYXJtdXQuY29tIiwicm9sZSI6IkF1dGhvcml6ZWRVc2VycyIsImlzcyI6IkFybXV0V2ViQVBJIiwiYXVkIjoiYjY5Y2ZjNzNjZTk4NDIyMThlODlhNDVjMjhjMmIwYzEiLCJleHAiOjE0NTE5ODcxODUsIm5iZiI6MTQyMDQ1MTE4NX0.qWt-sq2s_NivRaxnp9qv34SqKzE9l0m26stKgct9tD0");
        try {
            response = restClient.doPostRequestWithJSON(Constants.REGISTER_GCM_TOKEN,accessToken, new BasicNameValuePair("android_token", regId));
            Log.d("GCM_TASK", "RESPONSE FROM API (send reg id): " + "accessToken: " +accessToken + "-response: " + response);
            //Toast.makeText(context,"RESPONSE FROM API (send reg id): " + response, Toast.LENGTH_LONG).show();
        } catch ( Exception e) {
            // TODO Auto-generated catch block
            response = "NO_CONNECTION";
            e.printStackTrace();
        }
    }
}
