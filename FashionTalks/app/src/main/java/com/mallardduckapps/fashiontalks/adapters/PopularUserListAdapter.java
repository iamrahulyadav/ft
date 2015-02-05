package com.mallardduckapps.fashiontalks.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.makeramen.RoundedImageView;
import com.mallardduckapps.fashiontalks.FashionTalksApp;
import com.mallardduckapps.fashiontalks.R;
import com.mallardduckapps.fashiontalks.objects.Photo;
import com.mallardduckapps.fashiontalks.objects.PopularUser;
import com.mallardduckapps.fashiontalks.objects.User;
import com.mallardduckapps.fashiontalks.tasks.FollowTask;
import com.mallardduckapps.fashiontalks.utils.Constants;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;


/**
 * Created by oguzemreozcan on 03/02/15.
 */
public class PopularUserListAdapter extends BaseAdapter {

    private Activity activity;
    private ArrayList<PopularUser> data;
    private LayoutInflater inflater = null;
    private final String TAG = "POPULAR_USERS_ADAPTER";
    Resources res;
    DisplayImageOptions options;
    String pathMainUrl;

    public PopularUserListAdapter(Activity act, ArrayList<PopularUser> popularUsers){

        this.activity = act;
        data = popularUsers;
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
        final PopularUser user = data.get(position);
        final ArrayList<Photo> photos = user.getPhotos();
        if (vi == null) {
            vi = inflater.inflate(R.layout.popular_users_row, parent,
                    false);
            holder = new ViewHolder();
            holder.nameTv = (TextView) vi.findViewById(R.id.nameTv);
            holder.glamTv = (TextView) vi.findViewById(R.id.glamTv);
            holder.column1Image = (ImageView) vi.findViewById(R.id.imageColumn1);
            holder.column2Image = (ImageView) vi.findViewById(R.id.imageColumn2);
            holder.column3Image = (ImageView) vi.findViewById(R.id.imageColumn3);
            holder.thumbView = (RoundedImageView) vi.findViewById(R.id.thumbnailImage);
            holder.button = (ToggleButton) vi.findViewById(R.id.followButton);
            vi.setTag(holder);
        }
        else {
            holder = (ViewHolder) vi.getTag();
        }
        holder.nameTv.setText(user.getUserName());
        holder.glamTv.setText(new StringBuilder("").append(user.getGlamCount()).append(" Glam").toString());
        String path = new StringBuilder(pathMainUrl).append(user.getPhotoPath()).toString();
        ImageLoader.getInstance()
                .displayImage(path, holder.thumbView, options);

        holder.button.setChecked(user.getIsFollowing() == 1);
        holder.button.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.d(TAG, "BUTTON CHECKED is " + isChecked);
                FollowTask task = new FollowTask(isChecked, user.getId());
                task.execute();
            }
        });
        if(photos != null){
            if(photos.size() == 0){
                return vi;
            }
            String pathPhoto1 = new StringBuilder(pathMainUrl).append(photos.get(0).getPhotoUrl()).toString();
            String pathPhoto2 = new StringBuilder(pathMainUrl).append(photos.get(1).getPhotoUrl()).toString();
            String pathPhoto3 = new StringBuilder(pathMainUrl).append(photos.get(2).getPhotoUrl()).toString();
            ImageLoader.getInstance()
                    .displayImage(pathPhoto1, holder.column1Image, options);
            ImageLoader.getInstance()
                    .displayImage(pathPhoto2, holder.column2Image, options);
            ImageLoader.getInstance()
                    .displayImage(pathPhoto3, holder.column3Image, options);
        }

        return vi;
    }

    public static class ViewHolder {
        RoundedImageView thumbView;
        ImageView column1Image;
        ImageView column2Image;
        ImageView column3Image;
        TextView nameTv;
        TextView glamTv;
        ToggleButton button;
    }
}

