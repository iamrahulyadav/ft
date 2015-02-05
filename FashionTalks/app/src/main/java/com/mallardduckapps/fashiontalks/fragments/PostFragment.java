package com.mallardduckapps.fashiontalks.fragments;


import android.app.Fragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.makeramen.RoundedImageView;
import com.mallardduckapps.fashiontalks.PostsActivity;
import com.mallardduckapps.fashiontalks.R;
import com.mallardduckapps.fashiontalks.components.ExpandablePanel;
import com.mallardduckapps.fashiontalks.objects.Pivot;
import com.mallardduckapps.fashiontalks.objects.Post;
import com.mallardduckapps.fashiontalks.objects.Tag;
import com.mallardduckapps.fashiontalks.objects.User;
import com.mallardduckapps.fashiontalks.utils.Constants;
import com.mallardduckapps.fashiontalks.utils.FTUtils;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

/**
 * A simple {@link Fragment} subclass.
 */
public class PostFragment extends BasicFragment{

    int postId;
    int postIndex = -1;
    int loaderId;
    int width;
    int height;
    final int VIRTUAL_WIDTH = 320;
    final int VIRTUAL_HEIGHT = 320;
    RelativeLayout layout;

    int finalImageWidth;
    int finalImageHeight;
    int imageTopMargin = 0;

    public PostFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        width = PostsActivity.width;
        height = PostsActivity.height;
    }

    @Override
    public void setTag() {
        TAG = "Post_Fragment";
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.post_layout, container, false);
        layout = (RelativeLayout) rootView.findViewById(R.id.mainPostLayout);
        final RoundedImageView thumbnailView = (RoundedImageView)rootView.findViewById(R.id.thumbnail);
        final ImageView postPhoto = (ImageView)rootView.findViewById(R.id.postImage);
        final TextView tvUserName = (TextView) rootView.findViewById(R.id.userName);
        final TextView tvName = (TextView) rootView.findViewById(R.id.name);
        final TextView tvGlamCount = (TextView) rootView.findViewById(R.id.glamCount);
        final TextView tvChatText = (TextView) rootView.findViewById(R.id.chatText);
        final ProgressBar progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);
        postId = getArguments().getInt("POST_ID");
        postIndex = getArguments().getInt("POST_INDEX");
        loaderId = getArguments().getInt("LOADER_ID");

        Log.d(TAG, "POST FR: POST ID: " + postId);
        final Post post = getPost();
        User user = post.getUser();
        String path = new StringBuilder(Constants.CLOUD_FRONT_URL).append("/").append(width).append("x").append(width).append("/").append(post.getPhoto()).toString();
        String thumbPath = new StringBuilder(Constants.CLOUD_FRONT_URL).append("/").append(40).append("x").append(40).append("/").append(user.getPhotoPath()).toString();
        tvUserName.setText(user.getUserName());
        tvName.setText(post.getTitle());
        tvGlamCount.setText(new StringBuilder(Integer.toString(post.getGlamCount())).append(" Glam").toString());
        tvGlamCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GlammersFragment fragment = GlammersFragment.newInstance(Integer.toString(postId));
                //PopularUsersFragment fragment = PopularUsersFragment.newInstance("");
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.container, fragment).addToBackStack(fragment.getTag())
                        .commit();
            }
        });
        tvChatText.setText(new StringBuilder(Integer.toString(post.getCommentCount())).append(" Konuşma").toString());

        ImageLoader.getInstance().displayImage(thumbPath, thumbnailView,app.options);
        finalImageWidth = 0;
        ImageLoader.getInstance()
                .displayImage(path, postPhoto, app.options, new SimpleImageLoadingListener() {
                    @Override
                    public void onLoadingStarted(String imageUri, View view) {
                        progressBar.setVisibility(View.VISIBLE);
                        final ViewTreeObserver vto = postPhoto.getViewTreeObserver();
                        vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                            public boolean onPreDraw() {
                                if(finalImageWidth == 0){
                                    finalImageHeight = postPhoto.getHeight();
                                    finalImageWidth = postPhoto.getWidth();
                                    imageTopMargin = (int)postPhoto.getY();
                                    Log.e("IMAGE","Height: " + finalImageHeight + " Width: " + finalImageWidth + " - imageTopMargin : " + imageTopMargin);
                                    setGlamPosition(post);
                                }

//                    Log.d(TAG, "POSX " + posX/1.5 + " - POSY: " + posY/1.5);
                                return true;
                            }
                        });
                    }

                    @Override
                    public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                        progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                        progressBar.setVisibility(View.GONE);
                    }
                });
        Log.d(TAG, "POST FR: SELECTeD POST ID: " + post.getTitle());

        return rootView;
        //listView = (ListView) rootView.findViewById(R.id.galleryList);
    }

    private Post getPost(){
        Post post = null;
        switch (loaderId){
            case Constants.FEED_POSTS_LOADER_ID:
                post = app.getFeedPostArrayList().get(postIndex);
                break;
            case Constants.POPULAR_POSTS_LOADER_ID:
                post = app.getPopularPostArrayList().get(postIndex);
                break;
            case Constants.GALLERY_POSTS_LOADER_ID:
                post = app.getGalleryPostArrayList().get(postIndex);
                break;
        }
        return post;
    }

    private void setGlamPosition(Post post){
        LayoutInflater inflater = (LayoutInflater) getActivity()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        for (Tag tag : post.getTags()){
            Pivot pivot = tag.getPivot();
            //ImageView icon = new ImageView(getActivity());
            //icon.setBackgroundColor(getResources().getColor(R.color.blue));
            //RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(30, 30);
            //params.leftMargin = getRealX(pivot.getX());
            //params.topMargin = getRealY(pivot.getY());
            //layout.addView(icon, params);
            ExpandablePanel panel2 = new ExpandablePanel(getActivity(), getRealX(pivot.getX()), getRealY(pivot.getY()));
            panel2.setText(tag.getTag());
            panel2.setOnExpandListener(new ExpandablePanel.OnExpandListener() {
                @Override
                public void onExpand(View handle, View content) {

                }

                @Override
                public void onCollapse(View handle, View content) {

                }
            });
            //RelativeLayout layout2 = new RelativeLayout(getActivity());
            //layout2.setBackgroundResource(R.drawable.glam_shape);
/*            RelativeLayout.LayoutParams params2 = (RelativeLayout.LayoutParams)panel2.getLayoutParams();
            params2.leftMargin = getRealX(pivot.getX());
            params2.topMargin = getRealY(pivot.getY());*/
            layout.addView(panel2);
        }
    }

    private int getRealX(int x){
        return width*x/VIRTUAL_WIDTH;
    }

    //TODO it was height before, control the height
    private int getRealY(int y){
        return finalImageHeight*y/VIRTUAL_HEIGHT + imageTopMargin;
    }


}
