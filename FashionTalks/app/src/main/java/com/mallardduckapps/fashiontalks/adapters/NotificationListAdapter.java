package com.mallardduckapps.fashiontalks.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.makeramen.RoundedImageView;
import com.mallardduckapps.fashiontalks.FashionTalksApp;
import com.mallardduckapps.fashiontalks.R;
import com.mallardduckapps.fashiontalks.objects.Notification;
import com.mallardduckapps.fashiontalks.objects.User;
import com.mallardduckapps.fashiontalks.utils.Constants;
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

    public NotificationListAdapter(Activity act, ArrayList<Notification> notificationList){

        this.activity = act;
        data = notificationList;
        res = act.getResources();
        inflater = (LayoutInflater) act
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        options = ((FashionTalksApp) act.getApplication()).options;
        pathMainUrl = new StringBuilder(Constants.CLOUD_FRONT_URL).append("/40x40/").toString();
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
        String path ="";
        String message = notification.getContent();
        if (vi == null) {
            vi = inflater.inflate(R.layout.list_row, parent,
                    false);
            holder = new ViewHolder();
            holder.nameTv = (TextView) vi.findViewById(R.id.nameTv);
            holder.thumbView = (RoundedImageView) vi.findViewById(R.id.thumbnailImage);
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


        ImageLoader.getInstance()
                .displayImage(path, holder.thumbView, options);

        return vi;
    }

    public static class ViewHolder {
        RoundedImageView thumbView;
        TextView nameTv;
    }
}

