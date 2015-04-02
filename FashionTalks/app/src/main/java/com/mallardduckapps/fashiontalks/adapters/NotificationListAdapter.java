package com.mallardduckapps.fashiontalks.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.makeramen.RoundedImageView;
import com.mallardduckapps.fashiontalks.FashionTalksApp;
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
    private final String TAG = "NOTIFICATION_LIST_ADAPTER";
    Resources res;
    DisplayImageOptions options;
    String pathMainUrl;
    AssetManager manager;
    String font;

    public NotificationListAdapter(Activity act, ArrayList<Notification> notificationList){

        this.activity = act;
        manager = activity.getAssets();
        font = activity.getString(R.string.font_helvatica_lt);
        data = notificationList;
        res = act.getResources();
        inflater = (LayoutInflater) act
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        options = ((FashionTalksApp) act.getApplication()).options;
        //TODO
        pathMainUrl = new StringBuilder(Constants.CLOUD_FRONT_URL).append("/80x80/").toString();
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
        String photoUrl = notification.getPhoto();
        String path ="";
        String message = notification.getContent();
        if (vi == null) {
            vi = inflater.inflate(R.layout.notification_list_row, parent,
                    false);
            holder = new ViewHolder();
            holder.nameTv = (TextView) vi.findViewById(R.id.nameTv);
            holder.nameTv.setTypeface(FTUtils.loadFont(manager,font));
            holder.thumbView = (RoundedImageView) vi.findViewById(R.id.thumbnailImage);
            holder.postImage = (ImageView) vi.findViewById(R.id.postImage);
            vi.setTag(holder);
        }
        else {
            holder = (ViewHolder) vi.getTag();
        }

        if(source == null){
            holder.nameTv.setText(message);
        }else{
            holder.nameTv.setText(new StringBuilder(source.getFirstName()).append(" ").append(source.getLastName()).append(message).toString());
            path = new StringBuilder(pathMainUrl).append(source.getPhotoPath()).toString();
        }

        if(photoUrl != null){
            photoUrl = new StringBuilder(pathMainUrl).append(photoUrl).toString();
            ImageLoader.getInstance()
                    .displayImage(photoUrl, holder.postImage, options);
        }
        ImageLoader.getInstance()
                .displayImage(path, holder.thumbView, options);


        return vi;
    }

    public static class ViewHolder {
        RoundedImageView thumbView;
        TextView nameTv;
        ImageView postImage;
    }
}

