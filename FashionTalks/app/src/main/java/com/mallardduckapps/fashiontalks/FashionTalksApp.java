package com.mallardduckapps.fashiontalks;

import android.app.Activity;
import android.app.Application;
import android.app.DialogFragment;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;

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

import java.util.ArrayList;

/**
 * Created by oguzemreozcan on 10/01/15.
 */
public class FashionTalksApp extends Application {

    public DataSaver dataSaver;
    private final String TAG = "FashionTalksApp";
    public DisplayImageOptions options;
    //public DisplayImageOptions optionsNoCache;
    //ArrayList<Gallery> galleryArrayList;
    ArrayList<Post> popularPostArrayList;
    ArrayList<Post> feedPostArrayList;
    ArrayList<Post> galleryPostArrayList;
    ArrayList<Post> userPostArrayList;
    ArrayList<Post> myPostArrayList;
    public int lastGalleryId;
    SlidingMenu menu;
    User me;
    User other;

    @Override
    public void onCreate() {
        super.onCreate();
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
        //menu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
        //menu.setMenu(R.layout.menu);
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
        }

        dialog.setArguments(args);
        dialog.setTargetFragment(fragment, Constants.NO_CONNECTION);
        dialog.show(activity.getFragmentManager(), fragment.getTag());
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
        return popularPostArrayList;
    }

    public void setPopularPostArrayList(ArrayList<Post> popularPostArrayList) {
        this.popularPostArrayList = popularPostArrayList;
    }

    public void addPopularPostArrayList(ArrayList<Post> popularPostArrayList) {
        if(this.popularPostArrayList == null){
            this.popularPostArrayList = popularPostArrayList;
        }else{
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

    public User getMe() {
        return me;
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
    }
}
