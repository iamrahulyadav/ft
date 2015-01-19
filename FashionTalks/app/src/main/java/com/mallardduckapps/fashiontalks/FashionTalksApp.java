package com.mallardduckapps.fashiontalks;

import android.app.Application;
import android.graphics.Bitmap;

import com.mallardduckapps.fashiontalks.utils.DataSaver;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

/**
 * Created by oguzemreozcan on 10/01/15.
 */
public class FashionTalksApp extends Application {

    public DataSaver dataSaver;
    private final String TAG = "FashionTalksApp";
    public DisplayImageOptions options;

    @Override
    public void onCreate() {
        super.onCreate();
        dataSaver = new DataSaver(getApplicationContext(), "FashionTalks", false);
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.ic_stub)
                .showImageForEmptyUri(R.drawable.ic_empty)
                .showImageOnFail(R.drawable.ic_error)
                .cacheInMemory(true)
                .cacheOnDisk(false)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
                .defaultDisplayImageOptions(options).build();
        ImageLoader.getInstance().init(config);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        dataSaver = null;
    }

}
