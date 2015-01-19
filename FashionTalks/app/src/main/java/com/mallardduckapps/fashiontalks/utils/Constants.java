package com.mallardduckapps.fashiontalks.utils;

/**
 * Created by oguzemreozcan on 10/01/15.
 */
public class Constants {

    public final static int NO_CONNECTION = -1;
    public final static int AUTHENTICATION_FAILED = 0;
    public final static int AUTHENTICATION_CANCELED = 1;
    public final static int AUTHENTICATION_SUCCESSFUL = 2;
    public final static int WRONG_CREDENTIALS = 3;

    public final static int TOOLBAR_WITH_TITLE = 0;
    public final static int TOOLBAR_WITH_BACK_BUTTON = 1;
    public final static int TOOLBAR_WITH_BACK_AND_EXIT_BUTTON = 2;

    public final static String API_ADDRESS = "http://api.ft-api.com";
    public final static String LOGIN_PREFIX = "/users/login";
    public final static String REGISTER_PREFIX = "/users/register";
    public final static String REGISTER_GCM_TOKEN = "/members/android-token";
    public final static String TEST_NOTIFICATIONS = "/members/test-push-notification";
    public final static String GALLERIES_PREFIX = "/posts/galleries";
    public final static String POPULAR_PREFIX = "/posts/popular";
    public final static String POSTS_BASE_URL = "http://api.ft-api.com/posts/galleries/";
    public final static String CLOUD_FRONT_URL = "http://d3lhyn1u5tugzg.cloudfront.net";

    public final static String CLIENT_ID = "2";
    public final static String CLIENT_SECRET = "ZtqYh3hkF6v=mn";

    public final static String SENDER_ID = "337533113430";

    public final static int GALLERIES_LOADER_ID = 3;
    public final static int POPULAR_POSTS_LOADER_ID = 2;

}
