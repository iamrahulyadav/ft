package com.mallardduckapps.fashiontalks.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedImageView;
import com.mallardduckapps.fashiontalks.BaseActivity;
import com.mallardduckapps.fashiontalks.FashionTalksApp;
import com.mallardduckapps.fashiontalks.PostActivity;
import com.mallardduckapps.fashiontalks.R;
import com.mallardduckapps.fashiontalks.fragments.ClassificationDialog;
import com.mallardduckapps.fashiontalks.fragments.ReportDialog;
import com.mallardduckapps.fashiontalks.objects.PopularUser;
import com.mallardduckapps.fashiontalks.objects.Post;
import com.mallardduckapps.fashiontalks.objects.User;
import com.mallardduckapps.fashiontalks.tasks.FollowTask;
import com.mallardduckapps.fashiontalks.utils.Constants;
import com.mallardduckapps.fashiontalks.utils.FTUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.Collection;


/**
 * Created by oguzemreozcan on 03/02/15.
 */
public class PopularUserListAdapter extends BaseAdapter implements FollowTask.FollowCallback {

    private FragmentActivity activity;
    private ArrayList<PopularUser> data;
    //HashMap<String, User> dataToSearch
    private LayoutInflater inflater = null;
    private final String TAG = "POPULAR_USERS_ADAPTER";
    Resources res;
    DisplayImageOptions options;
    String pathMainUrl;
    FashionTalksApp app;

    public PopularUserListAdapter(FragmentActivity act, ArrayList<PopularUser> popularUsers){

        this.activity = act;
        data = popularUsers;
        res = act.getResources();
        inflater = (LayoutInflater) act
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        app = (FashionTalksApp) act.getApplication();
        options = app.options;
        //TODO
        pathMainUrl = new StringBuilder(Constants.CLOUD_FRONT_URL).append("/100x100/").toString();
    }

    public void addData(ArrayList<PopularUser> data){
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
        final PopularUser user = data.get(position);
        final ArrayList<Post> photos = user.getPhotos();
        if (vi == null) {
            vi = inflater.inflate(R.layout.popular_users_row, parent,
                    false);
            holder = new ViewHolder();
            holder.nameTv = (TextView) vi.findViewById(R.id.nameTv);
            holder.glamTv = (TextView) vi.findViewById(R.id.glamTv);
            holder.classificationTv = (TextView) vi.findViewById(R.id.classificationTv);
            holder.classificationTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ClassificationDialog dialog = new ClassificationDialog();
                    Bundle bundle = new Bundle();
                    bundle.putInt("DIALOG_NO", ClassificationDialog.CLASSIFICATION_DIALOG);
                    dialog.setArguments(bundle);
                    dialog.show(activity.getSupportFragmentManager(), "ClassificationDialog");
                }
            });
            holder.nameTv.setTypeface(FTUtils.loadFont(activity.getAssets(), activity.getString(R.string.font_helvatica_lt)));
            holder.glamTv.setTypeface(FTUtils.loadFont(activity.getAssets(), activity.getString(R.string.font_helvatica_lt)));
            holder.classificationTv.setTypeface(FTUtils.loadFont(activity.getAssets(), activity.getString(R.string.font_helvatica_thin)));
            holder.column1Image = (ImageView) vi.findViewById(R.id.imageColumn1);
            holder.column2Image = (ImageView) vi.findViewById(R.id.imageColumn2);
            holder.column3Image = (ImageView) vi.findViewById(R.id.imageColumn3);
            holder.thumbView = (RoundedImageView) vi.findViewById(R.id.thumbnailImage);
            holder.button = (CheckBox) vi.findViewById(R.id.followButton);

            vi.setTag(holder);
        }
        else {
            holder = (ViewHolder) vi.getTag();
        }

        holder.button.setChecked(user.getIsFollowing() == 1 );
        holder.nameTv.setText(user.getUserName());
        holder.glamTv.setText(new StringBuilder(user.getGlamCountPattern()).append(" Glam - ").toString());
        holder.classificationTv.setText(classifyUser(user.getGlamCount()));
        showImages(holder, user, photos);
        return vi;
    }

    private String classifyUser(int glamCount){
        if(glamCount <10000){
            return activity.getString(R.string.fashionTalker);
        }else if(glamCount >= 10000 && glamCount < 50000){
            return activity.getString(R.string.fashionLover);
        }else if(glamCount >= 50000 && glamCount < 100000){
            return activity.getString(R.string.raisingStar);
        }else if(glamCount >= 100000 && glamCount < 1000000){
            return activity.getString(R.string.fashionista);
        }else{
            return activity.getString(R.string.fashionIcon);
        }
    }

    private boolean showImages(final ViewHolder holder, final User user, ArrayList<Post> photos){

        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isChecked = holder.button.isChecked();
                    Log.d(TAG, "BUTTON CHECKED is " + isChecked);
                    FollowTask task = new FollowTask(PopularUserListAdapter.this, isChecked, user.getId());
                    app.executeAsyncTask(task);
                    user.setIsFollowing(isChecked ? 1 : 0);
                    //int index = data.indexOf(user);
                    //getItem(index)

            }
        });
/*        holder.button.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                //task.execute();
            }
        });*/
        String path = new StringBuilder(pathMainUrl).append(user.getPhotoPath()).toString();
        ImageLoader.getInstance()
                .displayImage(path, holder.thumbView, options);
        if(photos != null){
            if(photos.size() == 0){
                return false;
            }
            String pathPhoto1 = new StringBuilder(pathMainUrl).append(photos.get(0).getPhoto()).toString();
            String pathPhoto2 = new StringBuilder(pathMainUrl).append(photos.get(1).getPhoto()).toString();
            String pathPhoto3 = new StringBuilder(pathMainUrl).append(photos.get(2).getPhoto()).toString();
            ImageLoader.getInstance()
                    .displayImage(pathPhoto1, holder.column1Image, options);
            ImageLoader.getInstance()
                    .displayImage(pathPhoto2, holder.column2Image, options);
            ImageLoader.getInstance()
                    .displayImage(pathPhoto3, holder.column3Image, options);

            holder.column1Image.setOnClickListener(new PostClick(photos.get(0), user));
            holder.column2Image.setOnClickListener(new PostClick(photos.get(1), user));
            holder.column3Image.setOnClickListener(new PostClick(photos.get(2), user));

        }else{
            return false;
        }
        return true;
    }

    @Override
    public void isFollowed(boolean success, int userId) {

    }

    @Override
    public void isUnfollowed(boolean success, int userId) {

    }

    public class PostClick implements View.OnClickListener {

        final Post post;
        final User user;
        int postId;

        public PostClick(final Post post, final User user){
            this.user = user;
            this.post = post;
            post.setUser(user);
            postId = post.getId();
            //Log.d(TAG, "POST CLICK CONSTRUCTOR " + postId);
        }

        @Override
        public void onClick(View v) {
            app.setUserFavoritePost(post);
            Intent intent = new Intent(activity, PostActivity.class);
            //intent.putExtra("LOADER_ID", Constants.USER_FAVORITE_POST_LOADER_ID);
            intent.putExtra("POST_ID", postId);
            //intent.putExtra("POST_INDEX", -1);
            //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            activity.startActivity(intent);
            BaseActivity.setTranslateAnimation(activity);

//            PostFragment postFragment = new PostFragment();
//            Bundle bundle = new Bundle();
//            bundle.putInt("LOADER_ID", Constants.USER_FAVORITE_POST_LOADER_ID);
//            bundle.putInt("POST_ID", postId);
//            bundle.putInt("POST_INDEX", -1);
//            //bundle.putBoolean("OPEN_COMMENT", openComment);
//            postFragment.setArguments(bundle);
//            replaceFragment(postFragment);
        }

    };

    public static class ViewHolder {
        RoundedImageView thumbView;
        ImageView column1Image;
        ImageView column2Image;
        ImageView column3Image;
        TextView nameTv;
        TextView glamTv;
        TextView classificationTv;
        CheckBox button;
    }
}

