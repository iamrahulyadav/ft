package com.mallardduckapps.fashiontalks;

import android.app.Application;
import android.graphics.Bitmap;
import android.graphics.Color;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.mallardduckapps.fashiontalks.objects.Post;
import com.mallardduckapps.fashiontalks.objects.User;
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
    //ArrayList<Gallery> galleryArrayList;
    ArrayList<Post> popularPostArrayList;
    ArrayList<Post> feedPostArrayList;
    ArrayList<Post> galleryPostArrayList;
    SlidingMenu menu;
    User me;

    @Override
    public void onCreate() {
        super.onCreate();
        dataSaver = new DataSaver(getApplicationContext(), "FashionTalks", false);
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.gallery_card_drawable)
                .showImageForEmptyUri(R.drawable.ic_empty)
                .showImageOnFail(R.drawable.reload_small)
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

    public void addGalleryPostArrayList(ArrayList<Post> galleryPostArrayList) {
        if(this.galleryPostArrayList == null){
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

    public User getMe() {
        return me;
    }

    public void setMe(User me) {
        this.me = me;
    }
}
