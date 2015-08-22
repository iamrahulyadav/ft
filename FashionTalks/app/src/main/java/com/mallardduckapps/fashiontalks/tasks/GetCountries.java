package com.mallardduckapps.fashiontalks.tasks;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.mallardduckapps.fashiontalks.loaders.Exclude;
import com.mallardduckapps.fashiontalks.objects.Country;
import com.mallardduckapps.fashiontalks.services.RestClient;
import com.mallardduckapps.fashiontalks.utils.Constants;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by oguzemreozcan on 02/08/15.
 */
public class GetCountries extends AsyncTask<Void, Void, String> {

    private final String TAG = "GET_COUNTRIES";
    int status = 0;

    public GetCountries() {

    }

    @Override
    protected String doInBackground(Void... params) {
        String response = "";
        RestClient restClient = new RestClient();
        try {
            String url = null;
            url = new StringBuilder(Constants.GET_COUNTRIES).toString();

            response = restClient.doGetRequest(url, null);
            JSONObject object = new JSONObject(response);
            status = object.getInt("status");
            Log.d(TAG, "RESPONSE FROM API: status: "+ status + "-response: " + response);
        } catch (Exception e) {
            response = "NO_CONNECTION";
            e.printStackTrace();
        }

        return response;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        if(status != 200){

        }
        ArrayList<Country> countryArrayList;

        try{
            JsonArray dataObjects = new JsonParser().parse(s).getAsJsonObject().getAsJsonArray("data");
            Exclude ex = new Exclude();
            Gson gson = new GsonBuilder().addDeserializationExclusionStrategy(ex).addSerializationExclusionStrategy(ex).create();
            countryArrayList = new ArrayList<>();
            for (JsonElement item : dataObjects) {
                Country country = gson.fromJson(item, Country.class);
                countryArrayList.add(country);

            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public interface GetCountriesCallback{
        void getCountries(ArrayList<Country> countries);
    }
}
