package com.mallardduckapps.fashiontalks;


import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.crashlytics.android.Crashlytics;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.mallardduckapps.fashiontalks.fragments.BasicFragment;
import com.mallardduckapps.fashiontalks.fragments.DeletePhotoDialog;
import com.mallardduckapps.fashiontalks.fragments.ExitDialog;
import com.mallardduckapps.fashiontalks.fragments.NoConnectionDialog;
import com.mallardduckapps.fashiontalks.fragments.UploadPicDialog;
import com.mallardduckapps.fashiontalks.objects.Post;
import com.mallardduckapps.fashiontalks.objects.User;
import com.mallardduckapps.fashiontalks.utils.Constants;
import com.mallardduckapps.fashiontalks.utils.DataSaver;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.tweetcomposer.TweetComposer;

import java.io.IOException;
import java.util.ArrayList;

import io.fabric.sdk.android.Fabric;

/**
 * Created by oguzemreozcan on 10/01/15.
 */
public class FashionTalksApp extends android.app.Application {

    public DataSaver dataSaver;
    private final String TAG = "FashionTalksApp";
    public DisplayImageOptions options;
    public boolean newNotification = false;
    //public DisplayImageOptions optionsNoCache;
    //ArrayList<Gallery> galleryArrayList;
    private ArrayList<Post> popularPostArrayList;
    ArrayList<Post> feedPostArrayList;
    ArrayList<Post> galleryPostArrayList;
    ArrayList<Post> userPostArrayList;
    ArrayList<Post> myPostArrayList;
    ArrayList<Post> brandGalleryPostList;
    //Default Post array , to save the status of post list, since vertical pager add to list externally
    ArrayList<Post> defaultPostArray;
    Post userFavoritePost;
    public int lastGalleryId;
    public int lastBrandId;
    SlidingMenu menu;
    User me;
    User other;
    //USED FOR NOTIFICATION DIRECTION, note that bundle is useless in this specific case
    // direction can be either NOTIFICATION or MAIN
    //public String direction = "";

    //TODO Later change this
    int galleryId;
    public static GoogleAnalytics analytics;
    public static Tracker tracker;

    @Override
    public void onCreate() {
        super.onCreate();

        TwitterAuthConfig authConfig =  new TwitterAuthConfig(Constants.CONSUMER_KEY, Constants.CONSUMER_SECRET);
        Fabric.with(this,new TwitterCore(authConfig), new TweetComposer(), new Crashlytics()); //
        //Fabric.with(this);
        Log.d(TAG, "APP CREATE");
        dataSaver = new DataSaver(getApplicationContext(), "FashionTalks", false);
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.gallery_card_drawable)
                .showImageForEmptyUri(R.drawable.ic_empty)
                .showImageOnFail(R.drawable.reload_2x)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
                .defaultDisplayImageOptions(options).build();
        ImageLoader.getInstance().init(config);
        analytics = GoogleAnalytics.getInstance(this);
        analytics.setLocalDispatchPeriod(1800);

        tracker = analytics.newTracker("UA-54810966-1"); // Replace with actual tracker/property Id
        tracker.enableExceptionReporting(true);
        tracker.enableAdvertisingIdCollection(true);
        tracker.enableAutoActivityTracking(true);



        //menu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
        //menu.setMenu(R.layout.menu);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB) // API 11
    public <T> void executeAsyncTask(AsyncTask<T, ?, ?> asyncTask, T... params) {
        try{
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
                asyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, params);
            else
                asyncTask.execute(params);
        }catch(Exception e){
            asyncTask.execute(params);
        }
    }

    public void sendAnalyticsEvent(String screenName, String category, String action, String label){
        tracker.setScreenName(screenName);
        tracker.send(new HitBuilders.EventBuilder()
                .setCategory(category)
                .setAction(action)
                        .setLabel(label)
                .build());
    }

    public void openOKDialog(Activity activity, Fragment fragment, String dialogType){
        NoConnectionDialog dialog = new NoConnectionDialog();
        Bundle args = new Bundle();
        if(dialogType.equals("no_connection")){
            args.putString("title", getString(R.string.no_connection_title));
            args.putString("message", getString(R.string.no_connection));
        }else if(dialogType.equals("send_password_success")){
            args.putString("title", getString(R.string.send_password));
            args.putString("message", getString(R.string.password_success_dialog));
        }else if(dialogType.equals("send_password_unsuccessful")){
            args.putString("title", getString(R.string.send_password));
            args.putString("message", getString(R.string.password_error_dialog));
        }else if(dialogType.equals("no_post_access")){
            args.putString("title", getString(R.string.unsuccessful));
            args.putString("message", getString(R.string.posting_not_allowed));
        }else if(dialogType.equals("no_more_tag")){
            args.putString("title", getString(R.string.warning));
            args.putString("message", getString(R.string.no_more_glam));
        }
        String tag ="tag";
        dialog.setArguments(args);
        if(fragment != null){
            dialog.setTargetFragment(fragment, Constants.NO_CONNECTION);
            tag = fragment.getTag();
        }

        dialog.show(activity.getFragmentManager(), tag);
    }

    public void openUploadPicDialog(Activity activity, BasicFragment fragment){
        UploadPicDialog dialog = new UploadPicDialog();
        Bundle args = new Bundle();
        args.putString("title", getString(R.string.upload_title));
        args.putString("message", getString(R.string.upload_select_or_shoot));
        args.putString("positive_button", getString(R.string.upload_shoot));
        args.putString("negative_button", getString(R.string.upload_select));
        dialog.setArguments(args);
        dialog.setTargetFragment(fragment);
        //dialog.setTargetFragment(fragment, Constants.NO_CONNECTION);
        //TOdo control if tag is right?
        dialog.show(activity.getFragmentManager(), fragment.getTag());
    }

    public void openErasePicDialog(Activity activity, BasicFragment fragment){
        DeletePhotoDialog dialog = new DeletePhotoDialog();
        Bundle args = new Bundle();
        args.putString("title", getString(R.string.erase));
        args.putString("message", getString(R.string.delete_post_alert));
        args.putString("positive_button", getString(R.string.yes));
        args.putString("negative_button", getString(R.string.no));
        dialog.setArguments(args);
        dialog.setTargetFragment(fragment);
        //dialog.setTargetFragment(fragment, Constants.NO_CONNECTION);
        //TOdo control if tag is right?
        dialog.show(activity.getFragmentManager(), fragment.getTag());
    }

    public void exitDialog(Activity activity){
        ExitDialog dialog = new ExitDialog();
        Bundle args = new Bundle();
        args.putString("title", getString(R.string.logout));
        args.putString("message", getString(R.string.logout_alert));
        args.putString("positive_button", getString(R.string.yes));
        args.putString("negative_button", getString(R.string.no));
        dialog.setArguments(args);
        //dialog.setTargetFragment(fragment);
        //dialog.setTargetFragment(fragment, Constants.NO_CONNECTION);
        dialog.show(activity.getFragmentManager(), "Exit_Dialog");
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        dataSaver = null;
        flushAllData();
    }

    public ArrayList<Post> getDefaultPostArray() {
        return defaultPostArray;
    }

    public void setDefaultPostArray(ArrayList<Post> defaultPostArray) {
        this.defaultPostArray = defaultPostArray;
    }

    public ArrayList<Post> getGalleryPostArrayList() {
        return galleryPostArrayList;
    }

    public void setGalleryPostArrayList(ArrayList<Post> galleryPostArrayList) {
        this.galleryPostArrayList = galleryPostArrayList;
    }

    public void addGalleryPostArrayList(ArrayList<Post> galleryPostArrayList, int galleryId) {

        if(this.galleryPostArrayList == null || lastGalleryId != galleryId){
            this.galleryPostArrayList = galleryPostArrayList;
        }else{
            this.galleryPostArrayList.addAll(galleryPostArrayList);
        }
    }

    public ArrayList<Post> getBrandGalleryPostList() {
        return brandGalleryPostList;
    }

    public void setBrandGalleryPostList(ArrayList<Post> brandGalleryPostList) {
        this.brandGalleryPostList = brandGalleryPostList;
    }

    public void addBrandGalleryPostList(ArrayList<Post> brandGalleryPostList, int brandId) {
        if(this.brandGalleryPostList == null || lastBrandId != brandId){
            this.brandGalleryPostList = brandGalleryPostList;
        }else{
            this.brandGalleryPostList.addAll(brandGalleryPostList);
        }
    }

    public ArrayList<Post> getFeedPostArrayList() {
        return feedPostArrayList;
    }

    public void setFeedPostArrayList(ArrayList<Post> feedPostArrayList) {
        this.feedPostArrayList = feedPostArrayList;
    }

    public void addFeedPostArrayList(ArrayList<Post> feedPostArrayList) {
        if(this.feedPostArrayList == null){
            this.feedPostArrayList = feedPostArrayList;
        }else{
            this.feedPostArrayList.addAll(feedPostArrayList);
        }
    }

    public ArrayList<Post> getPopularPostArrayList() {
        //Log.d(TAG, "**GET POPULAR POSTS ARRAY LIST  " + popularPostArrayList.size());
        return popularPostArrayList;
    }

    public void setPopularPostArrayList(ArrayList<Post> popularPostArrayList) {
        if(popularPostArrayList != null){
            Log.d(TAG, "**SET POPULAR POSTS ARRAY LIST  " + popularPostArrayList.size());
        }

        this.popularPostArrayList = popularPostArrayList;
    }

    public void addPopularPostArrayList(ArrayList<Post> popularPostArrayList) {

        if(this.popularPostArrayList == null){
            //Log.d(TAG, "**ADD POPULAR POSTS ARRAY LIST  " + popularPostArrayList.size());
            this.popularPostArrayList = popularPostArrayList;
        }else{
            Log.d(TAG, "**ADD POPULAR POSTS ARRAY LIST add all " + popularPostArrayList.size());
            this.popularPostArrayList.addAll(popularPostArrayList);
        }
    }

    public void addUserPostArrayList(ArrayList<Post> userPostArrayList) {
        if(this.userPostArrayList == null){
            this.userPostArrayList = userPostArrayList;
        }else{
            this.userPostArrayList.addAll(userPostArrayList);
        }
    }

    public void addMyPostArrayList(ArrayList<Post> myPostArrayList) {
        if(this.myPostArrayList == null){
            this.myPostArrayList = myPostArrayList;
        }else{
            this.myPostArrayList.addAll(myPostArrayList);
        }
    }

    public ArrayList<Post> getUserPostArrayList() {
        return userPostArrayList;
    }

    public void setUserPostArrayList(ArrayList<Post> userPostArrayList) {
        this.userPostArrayList = userPostArrayList;
    }

    public ArrayList<Post> getMyPostArrayList() {
        return myPostArrayList;
    }

    public void setMyPostArrayList(ArrayList<Post> myPostArrayList) {
        this.myPostArrayList = myPostArrayList;
    }

    public Post getUserFavoritePost() {
        return userFavoritePost;
    }

    public void setUserFavoritePost(Post userFavoritePost) {
        this.userFavoritePost = userFavoritePost;
    }

    public User getMe() {
        return me;
    }

    public boolean isUserMe(int userId){
        if(me == null){
            return false;
        }
        if(me.getId() == userId){
            return true;
        }
        return false;
    }

    public int getGalleryId() {
        return galleryId;
    }

    public void setGalleryId(int galleryId) {
        this.galleryId = galleryId;
    }

    //TODO Control for adding same item multiple times
    public void setMe(User me) {
        myPostArrayList = null;
        this.me = me;
    }

    public User getOther() {
        return other;
    }

    public void setOther(User other) {
        userPostArrayList = null;
        this.other = other;
    }

    public void flushAllData(){
        popularPostArrayList = null;
        feedPostArrayList = null;
        galleryPostArrayList = null;
        userPostArrayList = null;
        myPostArrayList = null;
        lastGalleryId = 0;
        galleryId = 0;
        try {
            GoogleCloudMessaging.getInstance(this).unregister();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
