package com.mallardduckapps.fashiontalks.services;

import android.util.Log;

import com.mallardduckapps.fashiontalks.objects.BasicNameValuePair;
import com.mallardduckapps.fashiontalks.utils.Constants;
import com.mallardduckapps.fashiontalks.utils.FTUtils;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import org.json.JSONException;

import java.io.IOException;

/**
 * Created by oguzemreozcan on 25/12/14.
 */
public class RestClient {

    private OkHttpClient client = new OkHttpClient();
    private String mainUrl = Constants.API_ADDRESS;
    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");
    private static String ACCESS_TOKEN;
    private final String TAG = "REST_CLIENT";
    //public final String testAuthToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJBcm11dFdlYkFQSSIsImF1ZCI6ImI2OWNmYzczY2U5ODQyMjE4ZTg5YTQ1YzI4YzJiMGMxIiwiZXhwIjoxNDUxMzkwMjk4LCJuYmYiOjE0MTk4NTQyOTh9.PTxgbzMlOWyNmBcMMS1IxJ3xQNGRloFluCQury154Ww";

    public RestClient(){
    }

    public String doGetRequest(String url,String accessToken, BasicNameValuePair ... params) throws IOException, JSONException {
        if(ACCESS_TOKEN == null){
            ACCESS_TOKEN = accessToken;
        }
        return doGetRequest(url, params);
    }

    public String doGetRequest(String url,BasicNameValuePair... params) throws IOException, JSONException {
        Request request = null;
        url =  new StringBuilder(mainUrl).append(url).toString();
        if(ACCESS_TOKEN != null){
            request = createRequestWithBody(url, ACCESS_TOKEN, FTUtils.getBasicJson(params));
        }else{
            Log.e("EXCEPTION", "ACCESS TOKEN NULL!!!" );
        }
        //createRequest(url);
        Response response = client.newCall(request).execute();
        return response.body().string();
    }
    public String doGetRequestTest(String url, BasicNameValuePair ... params) throws IOException, JSONException {
        Request request = null;
        url =  new StringBuilder(Constants.TEST).append(url).toString();
        if(ACCESS_TOKEN != null){
            request = createRequestWithBody(url, ACCESS_TOKEN, FTUtils.getBasicJson(params));
        }else{
            Log.e("EXCEPTION", "ACCESS TOKEN NULL!!!" );
        }
        //createRequest(url);
        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    public String doPostRequestWithJSON(String url, String accessToken, BasicNameValuePair ... params) throws IOException, JSONException {
        url = new StringBuilder(mainUrl).append(url).toString();
        if(accessToken == null){
            accessToken = ACCESS_TOKEN;
        }else{
            if(ACCESS_TOKEN == null){
                ACCESS_TOKEN = accessToken;
            }
        }
        Request request = createRequestWithBody(url,accessToken, FTUtils.getBasicJson(params));
        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    public String doPostRequestWithJSON(String url, String accessToken, String json) throws IOException, JSONException {
        url = new StringBuilder(mainUrl).append(url).toString();
        if(accessToken == null){
            accessToken = ACCESS_TOKEN;
        }else{
            if(ACCESS_TOKEN == null){
                ACCESS_TOKEN = accessToken;
            }
        }
        Request request = createRequestWithBody(url,accessToken, json);
        Response response = client.newCall(request).execute();
        return response.body().string();
    }

//    public void doGetRequestWithCallback(String url, RestCallback callback) throws IOException {
//        Request request = new Request.Builder()
//                .url(url)
//                .build();
//        Call call = client.newCall(request);
//        call.enqueue(callback);
//       // Response response = call.execute();
//        return response.body().string();
//    }

//    public String doPostRequest(String url, boolean requestWithHeader, BasicNameValuePair... pairs) throws IOException {
//        Request request = null;
//        if(!requestWithHeader){
//            request = createRequestWithBody(url, pairs);
//        }else{
//            request = createRequestWithHeaders(url, pairs);
//        }
//        Response response = client.newCall(request).execute();
//        return response.body().string();
//    }

    private Request createRequest(String url){
        return new Request.Builder().url(url).build();
    }

    private Request createRequestWithHeaders(String url, BasicNameValuePair... pairs ){
        Request.Builder requestBuilder = new Request.Builder().url(url);
        for(BasicNameValuePair pair : pairs){
            requestBuilder.addHeader(pair.getName(), pair.getValue());
        }
        return requestBuilder.build();
    }

    private Request createRequestWithBody(String url, String accessToken, String json){
        RequestBody body = null;
        if(json != null) {
            body = RequestBody.create(JSON, json);
        }

        Request.Builder requestBuilder = new Request.Builder().url(url);
        if(accessToken !=null){
            Log.d(TAG, "ACCESS TOKEN: " + accessToken);
            requestBuilder.addHeader("Content-Type", "application/json");
//            requestBuilder.addHeader("access_token", accessToken);
            //Log.d(TAG, "ACCESS TOKEN: AH6VHwcNNBQbi61LhvHgOPXIq9nuwIdHk6CeQPvb");
            requestBuilder.addHeader("Authorization", "Bearer "+ accessToken);

        }
        if(body == null){
            return requestBuilder.build();
        }
        return requestBuilder.post(body).build();
    }

    private Request createRequestWithBody(String url, BasicNameValuePair... pairs ){
        RequestBody body = null;
        FormEncodingBuilder builder = new FormEncodingBuilder();
        for(BasicNameValuePair pair: pairs){
            builder.add(pair.getName(), pair.getValue());
        }
        body = builder.build();

        Request.Builder requestBuilder = new Request.Builder().url(url);
        return requestBuilder.post(body).build();
    }

    public static void setAccessToken(String accessToken){
        ACCESS_TOKEN = accessToken;
    }
}
