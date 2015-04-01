package com.mallardduckapps.fashiontalks.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.makeramen.RoundedImageView;
import com.mallardduckapps.fashiontalks.FashionTalksApp;
import com.mallardduckapps.fashiontalks.R;
import com.mallardduckapps.fashiontalks.objects.Comment;
import com.mallardduckapps.fashiontalks.objects.User;
import com.mallardduckapps.fashiontalks.utils.Constants;
import com.mallardduckapps.fashiontalks.utils.FTUtils;
import com.mallardduckapps.fashiontalks.utils.TimeUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.apache.commons.lang3.StringEscapeUtils;

import java.util.ArrayList;

/**
 * Created by oguzemreozcan on 10/02/15.
 */

public class CommentListAdapter extends BaseAdapter {

    private Activity activity;
    private ArrayList<Comment> data;
    private LayoutInflater inflater = null;
    private final String TAG = "Comment_List_Adapter";
    Resources res;
    DisplayImageOptions options;
    String pathMainUrl;
    String font;
    AssetManager manager;

    public CommentListAdapter(Activity act, ArrayList<Comment> commentsList){

        this.activity = act;
        manager = activity.getAssets();
        font = activity.getString(R.string.font_helvatica_thin);
        data = commentsList;
        res = act.getResources();
        inflater = (LayoutInflater) act
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        options = ((FashionTalksApp) act.getApplication()).options;
        //TODO
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
        final Comment comment = data.get(position);
        final User user = comment.getUser();
        if (vi == null) {
            vi = inflater.inflate(R.layout.comment_row, parent,
                    false);
            holder = new ViewHolder();
            holder.nameTv = (TextView) vi.findViewById(R.id.nameTv);
            holder.comment = (TextView) vi.findViewById(R.id.commentTv);
            holder.nameTv.setTypeface(FTUtils.loadFont(manager, font));
            holder.comment.setTypeface(FTUtils.loadFont(manager,font));
            holder.thumbView = (RoundedImageView) vi.findViewById(R.id.thumbnailImage);

            vi.setTag(holder);
        }
        else {
            holder = (ViewHolder) vi.getTag();
        }
        holder.nameTv.setText(user.getFirstName().concat(" ").concat(user.getLastName().concat(" | ").concat(TimeUtil.compareDateWithToday(comment.getCreatedAt(), res))));
        holder.comment.setText(StringEscapeUtils.unescapeJson(comment.getComment()));

        String path = new StringBuilder(pathMainUrl).append(user.getPhotoPath()).toString();
        ImageLoader.getInstance()
                .displayImage(path, holder.thumbView, options);


        return vi;
    }

    public static class ViewHolder {
        RoundedImageView thumbView;
        TextView nameTv;
        TextView comment;

    }
}

