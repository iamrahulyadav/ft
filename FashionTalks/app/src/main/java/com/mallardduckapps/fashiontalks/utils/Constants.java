package com.mallardduckapps.fashiontalks.utils;

/**
 * Created by oguzemreozcan on 10/01/15.
 */
public class Constants {

    public final static int NO_CONNECTION = -99;
    public final static int AUTHENTICATION_FAILED = 0;
    public final static int AUTHENTICATION_CANCELED = 1;
    public final static int AUTHENTICATION_SUCCESSFUL = 2;
    public final static int PROFILE_EDIT_SUCCESSFUL = 4;
    public final static int PROFILE_EDIT_UNSUCCESSFUL = 5;
    public final static int FB_AUTHENTICATION_SUCCESSFUL = 6;
    public final static int DUBLICATE_EMAIL = 7;
    public final static int DUBLICATE_USERNAME = 8;
    public final static int WRONG_CREDENTIALS = 3;

    public final static int TOOLBAR_WITH_TITLE = 0;
    public final static int TOOLBAR_WITH_BACK_BUTTON = 1;
    public final static int TOOLBAR_WITH_BACK_AND_EXIT_BUTTON = 2;

    public final static String API_ADDRESS = "http://api.ft-api.com";//"http://ft-api2.elasticbeanstalk.com";//;

    public final static String TEST = "http://ft-notifs.elasticbeanstalk.com";

    public final static String LOGIN_PREFIX = "/users/login";
    public final static String GET_USER_PREFIX = "/members/profile";
    public final static String REGISTER_PREFIX = "/users/register";
    public final static String GET_COUNTRIES = "/users/country-list";
    public final static String CONNECT_FB = "/connect/facebook";
    public final static String LOGIN_FB = "/users/facebook";
    public final static String RESET_PASS = "/users/reset";

    public final static String REGISTER_GCM_TOKEN = "/members/android-token";
    public final static String TEST_NOTIFICATIONS = "/members/test-push-notification";

    public final static String GALLERIES_PREFIX = "/posts/galleries";
    public final static String GALLERY_POSTS_PREFIX = "/posts/gallery-posts";
    public final static String GALLERY_POSTS_BY_TAG_PREFIX ="/posts/tag/";
    public final static String CATEGORY_POSTS_PREFIX ="";
    public final static String POPULAR_PREFIX = "/posts/popular";
    public final static String FEED_PREFIX = "/posts/feed";
    public final static String POST_CODE_REQUEST_PREFIX = "/posts/list-code-requests";
    public final static String POSTS_BY_USER_PREFIX = "/posts/user/";
    public final static String POSTS_INVITE_USERS = "/posts/invite-friends";

    public final static String POST_DETAILS_PREFIX = "/posts/view/";
    public final static String POST_COMMENTS = "/comments/comments/";
    public final static String POST_COMMENT = "/comments/comment";
    public final static String DELETE_COMMENT = "/comments/delete/";
    public final static String GLAMMER_LIST_PREFIX= "/glam/glammers/";
    public final static String GLAM_TAG_PREFIX = "/glam/tag/";
    public final static String GLAM_AC_TAG_PREFIX = "/tags/ac/";

    public final static String FOLLOW_USER_PREFIX = "/members/follow/";
    public final static String UNFOLLOW_USER_PREFIX = "/members/unfollow/";
    public final static String FOLLOWERS_PREFIX = "/members/followers/";
    public final static String FOLLOWING_PREFIX = "/members/following/";
    public final static String BLOCK_USER_PREFIX = "/members/block/";
    public final static String UNBLOCK_USER_PREFIX = "/members/unblock/";
    public final static String POPULAR_USERS_PREFIX = "/members/popular";
    public final static String FB_FRIENDS_PREFIX = "/members/fbfriends";
    public final static String SETTINGS_PREFIX = "/members/settings";
    public final static String FB_CONNECT ="connect/facebook";
    public final static String SETTINGS_TOGGLE_PREFIX ="/members/toggle-setting/";

    public final static String SEARCH_USERS = "/search/users/";
    public final static String SEARCH_BRANDS = "/tags/ac/";

    public final static String POST_NEW = "/posts/add";
    public final static String POST_DELETE ="/posts/delete/";

    public final static String PRIVACY_URL = "http://www.fashiontalks.org/privacy-policy.html";
    public final static String TERMS_OF_USE_URL = "http://www.fashiontalks.org/terms-of-use.html";
    public final static String FT_EMAIL =  "hello@fashiontalks.org";

    public final static String NOTIFICATION_LIST_PREFIX ="/notifications/list";
    public final static String POSTS_BASE_URL = "http://api.ft-api.com/posts/galleries/";
    public final static String CLOUD_FRONT_URL = "http://d3lhyn1u5tugzg.cloudfront.net";
    public final static String CLOUD_FRONT_URL_NOTIF = "http://d3lhyn1u5tugzg.cloudfront.net/80x80/";

    public final static String CLIENT_ID = "2";
    public final static String CLIENT_SECRET = "ZtqYh3hkF6v=mn";

    public final static String ACCESS_TOKEN_KEY = "ACCESS_TOKEN";
    public final static String FB_ACCESS_TOKEN_KEY = "FB_ACCESS_TOKEN";
    public final static String REFRESH_TOKEN_KEY = "REFRESH_TOKEN";

    public final static String SENDER_ID = "337533113430";

    public final static int USER_FAVORITE_POST_LOADER_ID = 22;
    public final static int GALLERY_POSTS_BY_TAG_LOADER_ID = 21;
    public final static int SETTINGS_LOADER_ID = 20;
    public final static int NOTIFICATION_MY_POST_LOADER_ID = 18;
    public final static int NOTIFICATION_OTHER_POST_LOADER_ID = 19;
    public final static int FB_FRIENDS_LOADER = 17;
    public final static int SEARCH_USER_LOADER = 16;
    public final static int SEARCH_TAG_LOADER = 15;
    public final static int CODE_REQUESTS_LOADER = 14;
    public final static int FOLLOWERS_LOADER_ID = 12;
    public final static int FOLLOWING_LOADER_ID = 13;
    public final static int MY_POSTS_LOADER_ID = 10;
    public final static int USER_POSTS_LOADER_ID = 11;
    public final static int COMMENTS_LOADER_ID = 8;
    public final static int NOTIFICATIONS_LOADER_ID = 7;
    public final static int POPULAR_USERS_LOADER_ID = 6;
    public final static int GLAMMERS_LOADER_ID = 5;
    public final static int GALLERY_POSTS_LOADER_ID = 4;
    public final static int GALLERIES_LOADER_ID = 3;
    public final static int POPULAR_POSTS_LOADER_ID = 2;
    public final static int FEED_POSTS_LOADER_ID = 1;

    public final static String TARGET_PROFILE = "profile";
    public final static String TARGET_POST = "post";
    public final static String TARGET_COMMENT = "comment";
    public final static String TARGET_CODE_REQUEST = "code-request";

    public static final int VIRTUAL_WIDTH = 320;
    public static final int VIRTUAL_HEIGHT = 320;

}
