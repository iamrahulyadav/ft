package com.mallardduckapps.fashiontalks.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.makeramen.RoundedImageView;
import com.mallardduckapps.fashiontalks.FashionTalksApp;
import com.mallardduckapps.fashiontalks.R;
import com.mallardduckapps.fashiontalks.objects.User;
import com.mallardduckapps.fashiontalks.tasks.FollowTask;
import com.mallardduckapps.fashiontalks.utils.Constants;
import com.mallardduckapps.fashiontalks.utils.FTUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

/**
 * Created by oguzemreozcan on 03/02/15.
 */
public class GlammerListAdapter extends BaseAdapter {

    private Activity activity;
    private ArrayList<User> data;
    private LayoutInflater inflater = null;
    private final String TAG = "GLAMMER_LIST_ADAPTER";
    Resources res;
    DisplayImageOptions options;
    String pathMainUrl;
    AssetManager manager;
    String font;

    public GlammerListAdapter(Activity act, ArrayList<User> glammerList){

        this.activity = act;
        data = glammerList;
        manager = activity.getAssets();
        font = activity.getString(R.string.font_helvatica_lt);
        res = act.getResources();
        inflater = (LayoutInflater) act
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        options = ((FashionTalksApp) act.getApplication()).options;
        pathMainUrl = new StringBuilder(Constants.CLOUD_FRONT_URL).append("/80x80/").toString();
    }

    public void addData(ArrayList<User> data){
        if(this.data == null){
            this.data = data;
        }else{
            // this.data.addAll(data);
            notifyDataSetChanged();
        }
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
        final User user = data.get(position);
        if (vi == null) {
            vi = inflater.inflate(R.layout.toggled_list_row, parent,
                    false);
            holder = new ViewHolder();
            holder.nameTv = (TextView) vi.findViewById(R.id.nameTv);
            holder.nameTv.setTypeface(FTUtils.loadFont(manager, font));
            holder.thumbView = (RoundedImageView) vi.findViewById(R.id.thumbnailImage);
            holder.button = (ToggleButton) vi.findViewById(R.id.followButton);
            vi.setTag(holder);
        }
        else {
            holder = (ViewHolder) vi.getTag();
        }
        holder.nameTv.setText(user.getFirstName().concat(" ").concat(user.getLastName()));
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

        return vi;
    }

    public static class ViewHolder {
        RoundedImageView thumbView;
        TextView nameTv;
        ToggleButton button;
    }
}
