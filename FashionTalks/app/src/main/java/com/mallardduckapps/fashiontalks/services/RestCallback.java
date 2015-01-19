package com.mallardduckapps.fashiontalks.services;

/**
 * Created by oguzemreozcan on 25/12/14.
 */
public interface RestCallback {

    public void onSuccesfullRequest(String response);
    public void onFailure(String message, int responseCode);
}
