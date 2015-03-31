package com.mallardduckapps.fashiontalks.loaders;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.mallardduckapps.fashiontalks.objects.Settings;
import com.mallardduckapps.fashiontalks.objects.User;
import com.mallardduckapps.fashiontalks.services.RestClient;
import com.mallardduckapps.fashiontalks.utils.Constants;

import java.lang.reflect.Type;
import java.util.Collection;

/**
 * Created by oguzemreozcan on 23/03/15.
 */
public class GetSettingsLoader extends AsyncTaskLoader<Settings> {

    final String TAG = "GetSettingsLoader";
    int loaderId;
    Settings settings;

    public GetSettingsLoader(Context context, final int loaderId){
        super(context);
        this.loaderId = loaderId;
    }

    @Override
    public Settings loadInBackground() {
        String response = "";
        RestClient restClient = new RestClient();
        //Log.d(TAG, "TASK START");
        try {
            String url = new StringBuilder(Constants.SETTINGS_PREFIX).toString();
            response = restClient.doGetRequest(url, null);
            Log.d(TAG, "SETTINGS RESPONSE : " + response);
            //Log.d(TAG, "User REQUEST RESPONSE: " + response);
            Gson gson = new GsonBuilder().create();
            Type collectionType = new TypeToken<Settings>(){}.getType();
            JsonElement object = new JsonParser().parse(response).getAsJsonObject().getAsJsonObject("data");
            settings = gson.fromJson(object, collectionType);
        } catch (Exception e) {
            response = "NO_CONNECTION";
            e.printStackTrace();
            return null;
        }
        return settings;
    }
}
