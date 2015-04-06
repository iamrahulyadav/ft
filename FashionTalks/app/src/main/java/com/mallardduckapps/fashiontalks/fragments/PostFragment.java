package com.mallardduckapps.fashiontalks.fragments;


import android.app.Activity;
import android.app.Fragment;
import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.makeramen.RoundedImageView;
import com.mallardduckapps.fashiontalks.BaseActivity;
import com.mallardduckapps.fashiontalks.GalleryActivity;
import com.mallardduckapps.fashiontalks.PostsActivity;
import com.mallardduckapps.fashiontalks.ProfileActivity;
import com.mallardduckapps.fashiontalks.R;
import com.mallardduckapps.fashiontalks.components.ExpandablePanel;
import com.mallardduckapps.fashiontalks.loaders.PostLoader;
import com.mallardduckapps.fashiontalks.objects.Pivot;
import com.mallardduckapps.fashiontalks.objects.Post;
import com.mallardduckapps.fashiontalks.objects.Tag;
import com.mallardduckapps.fashiontalks.objects.User;
import com.mallardduckapps.fashiontalks.services.RestClient;
import com.mallardduckapps.fashiontalks.utils.Constants;
import com.mallardduckapps.fashiontalks.utils.FTUtils;
import com.mallardduckapps.fashiontalks.utils.TimeUtil;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import org.apache.commons.lang3.StringEscapeUtils;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class PostFragment extends BasicFragment implements LoaderManager.LoaderCallbacks<Post>{

    int postId;
    int postIndex = -1;
    int loaderId;
    int width;
    int height;
    RelativeLayout layout;
    RelativeLayout bottomBar;
    ProgressBar progressBarMain;
    LinearLayout shareMenu;
    ImageView postPhoto;
    boolean shareMenuVisible = false;
    boolean ownPost;

    int finalImageWidth;
    int finalImageHeight;
    int imageTopMargin = 0;
    PostLoader loader;
    User user;

    TextView tvUserName;
    TextView tvName;
    TextView tvGlamCount;
    TextView tvChatText;
    TextView tvPostTime;

    ProgressBar progressBar;
    ImageButton shareButton;
    LinearLayout chatLayout;
    LinearLayout glamLayout;

    ViewSwitcher switcher;
    boolean openComment = false;
    public final static int DELETE_POST = 667;

    RoundedImageView thumbnailView;

    int glamCount;
    public static int commentCount;
    Post post;

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

    private void showNextView(){
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                switcher.showNext();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.d(TAG, "LIFETIME ON CREATEVIEW");
        View rootView = inflater.inflate(R.layout.post_layout, container, false);
        switcher = (ViewSwitcher) rootView.findViewById(R.id.viewSwitcher);
        layout = (RelativeLayout) rootView.findViewById(R.id.mainPostLayout);
        bottomBar = (RelativeLayout) rootView.findViewById(R.id.bottomBar);
        thumbnailView = (RoundedImageView)rootView.findViewById(R.id.thumbnail);
        postPhoto = (ImageView)rootView.findViewById(R.id.postImage);
        tvUserName = (TextView) rootView.findViewById(R.id.userName);
        tvName = (TextView) rootView.findViewById(R.id.name);
        tvGlamCount = (TextView) rootView.findViewById(R.id.glamCount);
        tvChatText = (TextView) rootView.findViewById(R.id.chatText);
        tvPostTime = (TextView) rootView.findViewById(R.id.postTime);

        progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);
        shareButton = (ImageButton) rootView.findViewById(R.id.shareButton);
        chatLayout = (LinearLayout) rootView.findViewById(R.id.chatLayout);
        glamLayout = (LinearLayout) rootView.findViewById(R.id.glamLayout);

        Activity activity = getActivity();
        tvPostTime.setTypeface(FTUtils.loadFont(activity.getAssets(),activity.getString(R.string.font_helvatica_thin)));
        tvGlamCount.setTypeface(FTUtils.loadFont(activity.getAssets(),activity.getString(R.string.font_helvatica_md)));
        tvChatText.setTypeface(FTUtils.loadFont(activity.getAssets(),activity.getString(R.string.font_helvatica_lt)));
        tvUserName.setTypeface(FTUtils.loadFont(activity.getAssets(),activity.getString(R.string.font_helvatica_md)));
        tvName.setTypeface(FTUtils.loadFont(activity.getAssets(),activity.getString(R.string.font_helvatica_md)));

        shareMenu = (LinearLayout) rootView.findViewById(R.id.shareMenuLayout);
        progressBarMain = (ProgressBar) rootView.findViewById(R.id.progressBar1);
        postId = getArguments().getInt("POST_ID");
        postIndex = getArguments().getInt("POST_INDEX");
        loaderId = getArguments().getInt("LOADER_ID");
        openComment = getArguments().getBoolean("OPEN_COMMENT", false);

        bottomBar.setVisibility(View.VISIBLE);
       // Log.d(TAG, "POST FR: POST ID: " + postId);
        post = getPost();
        //Log.d(TAG, "GET POSTT COMMETN COUNT: " + post.getCommentCount());
        //openComment = post.getCanComment() == 1 ? true: false;

//        if(loaderId == Constants.MY_POSTS_LOADER_ID){
//            user = app.getMe();
//            ownPost = true;
//            //shareButton.setImageResource(R.drawable.delete_icon);
//        }else if(loaderId == Constants.USER_POSTS_LOADER_ID){
//            user = app.getOther();
//            ownPost = false;
//            //shareButton.setImageResource(R.drawable.report_icon);
//        }else if(loaderId == Constants.NOTIFICATION_MY_POST_LOADER_ID){
//            user = app.getMe();
//            ownPost = true;
//            //shareButton.setImageResource(R.drawable.delete_icon);
//        }
//        else if(loaderId == Constants.NOTIFICATION_OTHER_POST_LOADER_ID){
//            user = app.getOther();
//            ownPost = false;
//            //shareButton.setImageResource(R.drawable.report_icon);
//        }
//        else{
//            user = post.getUser();
//            ownPost = false;
//            //shareButton.setImageResource(R.drawable.report_icon);
//        }

        if(post != null){
            setPostUserDrawables();
            fillPost(post);
            showNextView();
        }else{
            progressBar.setVisibility(View.VISIBLE);
            shareButton.setImageResource(R.drawable.delete_icon);
            user = app.getMe();
            ownPost = true;
            useLoader();
        }
        return rootView;
        //listView = (ListView) rootView.findViewById(R.id.galleryList);
    }

    private void setPostUserDrawables(){
        if(post.getUser().getId() == app.getMe().getId()){
            shareButton.setImageResource(R.drawable.delete_icon);
            user = app.getMe();
            ownPost = true;
        }else{
            shareButton.setImageResource(R.drawable.report_icon);
            user = post.getUser();
            ownPost = false;
        }
        glamCount = post.getGlamCount();
        commentCount = post.getCommentCount();
    }

    private void shareMenuOnClick(){
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
                //Log.d(TAG,"ON CLICK - start animation");
                shareMenu.clearAnimation();
                shareMenu.startAnimation(slide);
                shareMenuVisible = !shareMenuVisible;
            }
        }
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
            case Constants.USER_POSTS_LOADER_ID:
                post = app.getUserPostArrayList().get(postIndex);
                break;
            case Constants.MY_POSTS_LOADER_ID:
                post = app.getMyPostArrayList().get(postIndex);
                break;
            case Constants.NOTIFICATIONS_LOADER_ID:
                post = null;
                break;
            case Constants.GALLERY_POSTS_BY_TAG_LOADER_ID:
                post = app.getBrandGalleryPostList().get(postIndex);
                break;
        }
        return post;
    }

    private void setPost(Post post){
        switch (loaderId){
            case Constants.FEED_POSTS_LOADER_ID:
                app.getFeedPostArrayList().set(postIndex, post);
                break;
            case Constants.POPULAR_POSTS_LOADER_ID:
                app.getPopularPostArrayList().set(postIndex, post);
                break;
            case Constants.GALLERY_POSTS_LOADER_ID:
                app.getGalleryPostArrayList().set(postIndex, post);
                break;
            case Constants.USER_POSTS_LOADER_ID:
                app.getUserPostArrayList().set(postIndex, post);
                break;
            case Constants.MY_POSTS_LOADER_ID:
                app.getMyPostArrayList().set(postIndex, post);
                break;
            case Constants.GALLERY_POSTS_BY_TAG_LOADER_ID:
                app.getBrandGalleryPostList().set(postIndex, post);
                break;
        }
    }

    public void incrementGlamCount(final int newGlamCount){
        //int glamCount = post.getGlamCount();

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "NEW GLAM COUNT : " + newGlamCount);
                post.setGlamCount(newGlamCount);
                tvGlamCount.setText(new StringBuilder(post.getGlamCountPattern()).append(getString(R.string.glam)).toString());
                setPost(post);
                tvGlamCount.invalidate();
            }
        });
    }

    public void incrementCommentCount(){
        int comment = post.getCommentCount();
        post.setCommentCount(comment++);
        if(tvChatText != null){
            tvChatText.setText(new StringBuilder(Integer.toString(post.getCommentCount())).append(getString(R.string.comment)).toString());
        }
        setPost(post);
    }

    @Override
    public void onPause() {
        super.onPause();
        //Log.d(TAG, "LIFE TIME ON PAUSE");
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        //Log.d(TAG, "LIFE TIME ON VIEW STATE RESTORED");
    }

    @Override
    public void onResume() {
        super.onResume();
        //Log.d(TAG, "LIFE TIME ON RESUME " +commentCount);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        //Log.d(TAG, "LIFE TIME ON ATTACH " + commentCount);
    }

    private void fillPost(final Post post){
        String path = new StringBuilder(Constants.CLOUD_FRONT_URL).append("/").append(width).append("x").append(width).append("/").append(post.getPhoto()).toString();
        //TODO change 40x40
        String thumbPath = new StringBuilder(Constants.CLOUD_FRONT_URL).append("/").append(80).append("x").append(80).append("/").append(user.getPhotoPath()).toString();
        tvUserName.setText(user.getUserName());
        tvName.setText(StringEscapeUtils.unescapeJson(post.getTitle()));
        tvGlamCount.setText(new StringBuilder(post.getGlamCountPattern()).append(getString(R.string.glam)).toString());
        //Log.d(TAG, "POST CREATED AT: " + post.getCreatedAt());
        tvPostTime.setText(TimeUtil.compareDateWithToday(post.getCreatedAt(), getResources()));
        glamLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GlammersFragment fragment = GlammersFragment.newInstance(Integer.toString(postId));
                FragmentTransaction fragmentTx = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTx.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_from_left, R.anim.enter_from_left, R.anim.exit_from_right);
                fragmentTx.replace(R.id.container, fragment).addToBackStack(fragment.getTag())
                        .commit();
            }
        });
        tvChatText.setText(new StringBuilder(Integer.toString(post.getCommentCount())).append(getString(R.string.comment)).toString());
        chatLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //if(post.getCommentCount() != 0){
                if(post.getCanComment() == 0){
                    return;
                }
                //Bundle bundle = new Bundle();
                //bundle.putString("POST_ID", Integer.toString(postId));
                //bundle.putInt("POST_LOADER_ID", loaderId);
                CommentsFragment fragment = CommentsFragment.newInstance(Integer.toString(postId), loaderId, postIndex);
                //fragment.setArguments(bundle);
                //bottomBar.setVisibility(View.GONE);
                //PopularUsersFragment fragment = PopularUsersFragment.newInstance("");
                FragmentTransaction fragmentTx = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTx.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_from_left, R.anim.enter_from_left, R.anim.exit_from_right);
                        fragmentTx.replace(R.id.container, fragment).addToBackStack(fragment.getTag())
                        .commit();
                //}
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
                BaseActivity.setTranslateAnimation(getActivity());
                //getActivity().overridePendingTransition(R.anim.activity_open_scale, R.anim.activity_close_translate);
            }
        };
        tvUserName.setOnClickListener(onClickListener);
        thumbnailView.setOnClickListener(onClickListener);
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
                                    Log.e("IMAGE","(GLAM) Image Height: " + finalImageHeight + " Width: " + finalImageWidth + " - imageTopMargin : " + imageTopMargin);
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
        //Log.d(TAG, "POST FR: SELECTeD POST ID: " + post.getTag());
        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,"ON CLICK");
                // Share Menu is not active now
                //shareMenuOnClick();
                if(user != app.getMe()){
                    // REPORT
                    FTUtils.sendMail(getString(R.string.email_send_report_content), getString(R.string.email_send_report_recipient), getString(R.string.email_send_report_subject), getActivity());
                }else{
                    //DELETE
                    app.openErasePicDialog(PostFragment.this.getActivity(), PostFragment.this);

                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == DELETE_POST && resultCode == 1){
            DeleteTask task = new DeleteTask();
            task.execute(Integer.toString(postId));
        }
    }

    private void setGlamPosition(Post post){

        final ArrayList<ExpandablePanel> panels = new ArrayList<>(post.getTags().size());
        for (Tag tag : post.getTags()){
            final Pivot pivot = tag.getPivot();
            int x = getRealX(pivot.getX());
            int y = getRealY(pivot.getY());
            int tagId = tag.getId();
            String brandName = tag.getTag();
            final ExpandablePanel panel = new ExpandablePanel(getActivity(), pivot, x , y, x > PostsActivity.width/2 ? false:true, ownPost, false);
            panel.setTagId(tagId);
            panel.setBrandName(brandName);
            if(panel.isLhsAnimation()){
                panel.setTagText(new StringBuilder(pivot.getGlamCountPattern()).append(" | ").append(tag.getTag()).append(" ").toString() ,false);
            }else{
                panel.setTagText(new StringBuilder(tag.getTag()).append(" | ").append(pivot.getGlamCountPattern()).toString() ,false);
            }

            //panel.setTypeface(FTUtils.loadFont(getActivity().getAssets(), getActivity().getString(R.string.font_helvatica_lt)));
            Log.d(TAG, "GLAM X: " + x + " - GLAM Y: " + y);
            panel.setOnExpandListener(new ExpandablePanel.OnExpandListener() {
                @Override
                public void onExpand(View handle) {
                }

                @Override
                public void onCollapse(View handle, int tagId, String brandName) {
                    Intent intent = new Intent(getActivity(), GalleryActivity.class);
                    intent.putExtra("GALLERY_ID", tagId);
                    intent.putExtra("GALLERY_NAME", brandName);
                    intent.putExtra("LOADER_ID", Constants.GALLERY_POSTS_BY_TAG_LOADER_ID);
                    //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    PostFragment.this.getActivity().startActivity(intent);
                    BaseActivity.setTranslateAnimation(getActivity());
                }

                @Override
                public void onTagGlammed(int glamCount, int totalGlamCount) {
                    Log.d(TAG, "ON GLAM COUNT INCREASED"+totalGlamCount);
                    incrementGlamCount(totalGlamCount);
                }
            });

           // if(ownPost){
                //panel.animateExpand();
           // }
            panels.add(panel);
            layout.addView(panel);
        }

        postPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Log.d(TAG, "ON CLICK TO LAYOUT SHRINK ALL ANIMS");
                for (ExpandablePanel panel : panels) {
                    if (panel.mExpanded) {
                        panel.runShrinkAnimation();
                    }
                }
            }
        });

        shareMenu.bringToFront();
        bottomBar.bringToFront();
    }

    private int getRealX(int x){
        return width*x/Constants.VIRTUAL_WIDTH;
    }

    //TODO it was height before, control the height
    private int getRealY(int y){
        return finalImageHeight*y/Constants.VIRTUAL_HEIGHT + imageTopMargin;
    }

    @Override
    public Loader<Post> onCreateLoader(int id, Bundle args) {
        loader = new PostLoader(getActivity().getApplicationContext(), Constants.NOTIFICATIONS_LOADER_ID, postId);
        return loader;
    }

    @Override
    public void onLoadFinished(Loader<Post> loader, Post data) {
        if(data != null){

            if(data.isInvalid()){
                Toast.makeText(PostFragment.this.getActivity().getApplicationContext(), getString(R.string.invalid_post_alert), Toast.LENGTH_SHORT).show();
                getActivity().finish();
                BaseActivity.setBackwardsTranslateAnimation(PostFragment.this.getActivity());
                return;
            }
            post = data;
            setPostUserDrawables();
            progressBar.setVisibility(View.GONE);
            fillPost(data);
            if(openComment){
                chatLayout.performClick();
            }else{
                showNextView();
            }

        }
        else{
            Log.d(TAG, "DATA IS NULL");
            Handler handler = new Handler();
            handler.post(new Runnable() {
                @Override
                public void run() {
                    app.openOKDialog(PostFragment.this.getActivity(), PostFragment.this, "no_connection");
                }
            });

            return;
        }
    }

    @Override
    public void onLoaderReset(Loader<Post> loader) {
    }

    private void useLoader() {
        if (loader == null) {
            loader = (PostLoader) getActivity().getLoaderManager()
                    .initLoader(loaderId, null, this);
            loader.forceLoad();
        }
    }

    public class DeleteTask extends AsyncTask<String, Void, String> {

        private final String TAG = "SendPhotoTask";
        private int status = -1;

        public DeleteTask(){
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBarMain.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... params) {
            String response = "";
            RestClient restClient = new RestClient();
            try {
                String url = new StringBuilder(Constants.POST_DELETE).append(params[0]).toString();
                response = restClient.doGetRequest(url, null);
                Log.d(TAG, "User REQUEST RESPONSE: " + response);
                JSONObject object = new JSONObject(response);
                status = object.getInt("status");

            } catch (Exception e) {
                response = "NO_CONNECTION";
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(String text) {
            super.onPostExecute(text);
            progressBarMain.setVisibility(View.GONE);
            if(status == 0){
                ProfileActivity.imageGalleryChanged = true;
                getActivity().finish();
                BaseActivity.setBackwardsTranslateAnimation(getActivity());
            }else{
                Toast.makeText(PostFragment.this.getActivity(),getString(R.string.problem_occured), Toast.LENGTH_SHORT).show();
            }

        }
    }


}
