package com.mallardduckapps.fashiontalks.fragments;


import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.makeramen.RoundedImageView;
import com.mallardduckapps.fashiontalks.MainActivity;
import com.mallardduckapps.fashiontalks.PostsActivity;
import com.mallardduckapps.fashiontalks.ProfileActivity;
import com.mallardduckapps.fashiontalks.R;
import com.mallardduckapps.fashiontalks.components.ExpandablePanel;
import com.mallardduckapps.fashiontalks.objects.Pivot;
import com.mallardduckapps.fashiontalks.objects.Post;
import com.mallardduckapps.fashiontalks.objects.Tag;
import com.mallardduckapps.fashiontalks.objects.User;
import com.mallardduckapps.fashiontalks.tasks.GlamTask;
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
    RelativeLayout bottomBar;
    LinearLayout shareMenu;
    boolean shareMenuVisible = false;

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
        bottomBar = (RelativeLayout) rootView.findViewById(R.id.bottomBar);
        final RoundedImageView thumbnailView = (RoundedImageView)rootView.findViewById(R.id.thumbnail);
        final ImageView postPhoto = (ImageView)rootView.findViewById(R.id.postImage);
        final TextView tvUserName = (TextView) rootView.findViewById(R.id.userName);
        final TextView tvName = (TextView) rootView.findViewById(R.id.name);
        final TextView tvGlamCount = (TextView) rootView.findViewById(R.id.glamCount);
        final TextView tvChatText = (TextView) rootView.findViewById(R.id.chatText);
        final ProgressBar progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);
        final ImageButton shareButton = (ImageButton) rootView.findViewById(R.id.shareButton);
        final LinearLayout chatLayout = (LinearLayout) rootView.findViewById(R.id.chatLayout);
        final LinearLayout glamLayout = (LinearLayout) rootView.findViewById(R.id.glamLayout);

        shareMenu = (LinearLayout) rootView.findViewById(R.id.shareMenuLayout);
        postId = getArguments().getInt("POST_ID");
        postIndex = getArguments().getInt("POST_INDEX");
        loaderId = getArguments().getInt("LOADER_ID");

        Log.d(TAG, "POST FR: POST ID: " + postId);
        final Post post = getPost();
        final User user = post.getUser();
        String path = new StringBuilder(Constants.CLOUD_FRONT_URL).append("/").append(width).append("x").append(width).append("/").append(post.getPhoto()).toString();
        String thumbPath = new StringBuilder(Constants.CLOUD_FRONT_URL).append("/").append(40).append("x").append(40).append("/").append(user.getPhotoPath()).toString();
        tvUserName.setText(user.getUserName());
        tvName.setText(post.getTitle());
        tvGlamCount.setText(new StringBuilder(Integer.toString(post.getGlamCount())).append(" Glam").toString());
        glamLayout.setOnClickListener(new View.OnClickListener() {
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
        chatLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(post.getCommentCount() != 0){
                    Bundle bundle = new Bundle();
                    bundle.putString("POST_ID", Integer.toString(postId));
                    CommentsFragment fragment = new CommentsFragment();
                    fragment.setArguments(bundle);

                    //PopularUsersFragment fragment = PopularUsersFragment.newInstance("");
                    getActivity().getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.container, fragment).addToBackStack(fragment.getTag())
                            .commit();
                }

            }
        });

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ProfileActivity.class);
                app.setOther(user);
                intent.putExtra("PROFILE_ID", user.getId());
                //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        };
        tvUserName.setOnClickListener(onClickListener);
        tvName.setOnClickListener(onClickListener);
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

        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,"ON CLICK");
                Animation slide;
                if(shareMenuVisible){
                    slide = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_down);
                    Log.d(TAG,"ON CLICK - sldie down");
                }else{
                    shareMenu.setVisibility(View.VISIBLE);
                    slide = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_up);
                    Log.d(TAG,"ON CLICK - slide up");
                }
                if(slide != null){
                    slide.reset();
                    if(shareMenu != null){
                        Log.d(TAG,"ON CLICK - start animation");
                        shareMenu.clearAnimation();
                        shareMenu.startAnimation(slide);
                        shareMenuVisible = !shareMenuVisible;
                    }
                }
            }
        });

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
            final Pivot pivot = tag.getPivot();
            int x = getRealX(pivot.getX());
            ExpandablePanel panel = new ExpandablePanel(getActivity(), pivot, x , getRealY(pivot.getY()), x > PostsActivity.width/2 ? true:false);
            panel.setTagText(new StringBuilder("").append(pivot.getGlamCount()).append(" | ").append(tag.getTag()).toString());
            panel.setOnExpandListener(new ExpandablePanel.OnExpandListener() {
                @Override
                public void onExpand(View handle) {

                }

                @Override
                public void onCollapse(View handle) {

                }
            });
            layout.addView(panel);
        }

        shareMenu.bringToFront();
        bottomBar.bringToFront();
    }

    private int getRealX(int x){
        return width*x/VIRTUAL_WIDTH;
    }

    //TODO it was height before, control the height
    private int getRealY(int y){
        return finalImageHeight*y/VIRTUAL_HEIGHT + imageTopMargin;
    }


}
