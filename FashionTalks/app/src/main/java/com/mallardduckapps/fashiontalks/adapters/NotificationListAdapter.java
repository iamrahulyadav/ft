package com.mallardduckapps.fashiontalks.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.makeramen.RoundedImageView;
import com.mallardduckapps.fashiontalks.BaseActivity;
import com.mallardduckapps.fashiontalks.FashionTalksApp;
import com.mallardduckapps.fashiontalks.ProfileActivity;
import com.mallardduckapps.fashiontalks.R;
import com.mallardduckapps.fashiontalks.objects.Notification;
import com.mallardduckapps.fashiontalks.objects.User;
import com.mallardduckapps.fashiontalks.utils.Constants;
import com.mallardduckapps.fashiontalks.utils.FTUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

/**
 * Created by oguzemreozcan on 05/02/15.
 */


public class NotificationListAdapter extends BaseAdapter {

    private Activity activity;
    private ArrayList<Notification> data;
    private LayoutInflater inflater = null;
    private final String TAG = "NOTIF_LIST_ADAPTER";
    Resources res;
    DisplayImageOptions options;
    String pathMainUrl;
    AssetManager manager;
    String font;
    FashionTalksApp app;

    public NotificationListAdapter(Activity act, ArrayList<Notification> notificationList){

        this.activity = act;
        manager = activity.getAssets();
        font = activity.getString(R.string.font_helvatica_lt);
        data = notificationList;
        res = act.getResources();
        inflater = (LayoutInflater) act
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        options = ((FashionTalksApp) act.getApplication()).options;
        app = ((FashionTalksApp) activity.getApplication());
        //TODO
        pathMainUrl = new StringBuilder(Constants.CLOUD_FRONT_URL).append("/100x100/").toString();
    }

    @Override
    public int getCount() {
        if (data == null) {
            return 0;
        }
        return data.size();
    }

    public ArrayList<Notification> getList(){
        return data;
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return data.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        ViewHolder holder;
        final Notification notification = data.get(position);
        final User source = notification.getSource();
        final String photoUrl = notification.getFullPhotoSource();
        String path ="";
        final String message = notification.getMessage();//notification.getContent();
        if (vi == null) {
            vi = inflater.inflate(R.layout.notification_list_row, parent,
                    false);
            holder = new ViewHolder();
            holder.nameTv = (TextView) vi.findViewById(R.id.nameTv);
            holder.nameTv.setTypeface(FTUtils.loadFont(manager,font));
            holder.thumbView = (RoundedImageView) vi.findViewById(R.id.thumbnailImage);
            holder.postImage = (ImageView) vi.findViewById(R.id.postImage);

            //holder.photoUrl = photoUrl;
            vi.setTag(holder);
        }
        else {
            holder = (ViewHolder) vi.getTag();
        }

        holder.nameTv.setText(message);
        if(photoUrl == null){
            holder.postImage.setVisibility(View.GONE);
        }else{
            holder.postImage.setVisibility(View.VISIBLE);
            ImageLoader.getInstance()
                    .displayImage(photoUrl, holder.postImage, options);
        }

        if(source != null){
            path = new StringBuilder(pathMainUrl).append(source.getPhotoPath()).toString();
            holder.thumbView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(activity, ProfileActivity.class);
                    intent.putExtra("PROFILE_ID", source.getId());
                    app.setOther(source);
                    //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    activity.startActivity(intent);
                    BaseActivity.setTranslateAnimation(activity);
                }
            });
        }
        ImageLoader.getInstance()
                .displayImage(path, holder.thumbView, options);



        return vi;
    }

    public static class ViewHolder {
        RoundedImageView thumbView;
        TextView nameTv;
        ImageView postImage;
        //String photoUrl;
    }
}

