package com.mallardduckapps.fashiontalks.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;
import com.makeramen.roundedimageview.RoundedImageView;
import com.mallardduckapps.fashiontalks.BaseActivity;
import com.mallardduckapps.fashiontalks.FashionTalksApp;
import com.mallardduckapps.fashiontalks.PostsActivity;
import com.mallardduckapps.fashiontalks.ProfileActivity;
import com.mallardduckapps.fashiontalks.R;
import com.mallardduckapps.fashiontalks.adapters.GalleryGridAdapter;
import com.mallardduckapps.fashiontalks.components.BounceListView;
import com.mallardduckapps.fashiontalks.components.GridListOnScrollListener;
import com.mallardduckapps.fashiontalks.loaders.PostsLoader;
import com.mallardduckapps.fashiontalks.objects.GalleryItem;
import com.mallardduckapps.fashiontalks.objects.Post;
import com.mallardduckapps.fashiontalks.objects.User;
import com.mallardduckapps.fashiontalks.tasks.BlockTask;
import com.mallardduckapps.fashiontalks.tasks.FollowTask;
import com.mallardduckapps.fashiontalks.utils.Constants;
import com.mallardduckapps.fashiontalks.utils.FTUtils;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.twitter.sdk.android.tweetcomposer.TweetComposer;

import org.apache.commons.lang3.StringEscapeUtils;

import java.util.ArrayList;

/**
 * Created by oguzemreozcan on 15/02/15.
 */
public class ProfileFragment extends BasicFragment implements LoaderManager.LoaderCallbacks<ArrayList<Post>>
        ,GridListOnScrollListener.OnScrolledToBottom, GalleryGridAdapter.PostItemClicked, FollowTask.FollowCallback
        ,BlockTask.IsBlocked{

    boolean myProfile = false;
    private static final String PROFILE_ID = "PROFILE_ID";
    private int profileId;
    private RoundedImageView profileImage;
    Button followButton;
    Button increaseFollowersButton;
    int itemCountPerLoad = 0;
    private BounceListView listView;
    private ArrayList<GalleryItem> dataList;
    private GalleryGridAdapter listAdapter;
    private RelativeLayout progressBar;
    protected View loadMoreFooterView;
    User user;
    int index = 0;
    private int MAX_CARDS = 2;
    PostsLoader loader;
    int loaderId;
    boolean loading;
    boolean isFollowing;
    boolean isBlocked;
    LinearLayout shareMenu;
    LinearLayout shareFb;
    LinearLayout shareTwitter;
    LinearLayout shareInstagram;
    boolean shareMenuVisible = false;
    View rootView;

    public static ProfileFragment newInstance(int param1) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putInt(PROFILE_ID, param1);
        fragment.setArguments(args);
        return fragment;
    }

    public ProfileFragment() {
    }

    @Override
    public void setTag() {
        TAG = "Profile_Fragment";
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(app == null){
            app = (FashionTalksApp)getActivity().getApplication();
        }

        if (getArguments() != null) {
            profileId = getArguments().getInt(PROFILE_ID);
            Log.d(TAG, "PROFILE ID: " + profileId);
            if(app.getMe().getId() == profileId || profileId == 0){
                myProfile = true;
                loaderId = Constants.MY_POSTS_LOADER_ID;
                app.setMyPostArrayList(null);
                user = app.getMe();
            }else{
                myProfile = false;
                app.setUserPostArrayList(null);
                user = app.getOther();
                loaderId = Constants.USER_POSTS_LOADER_ID;
            }
        }else{
            myProfile = true;
            app.setMyPostArrayList(null);
            user = app.getMe();
            loaderId = Constants.MY_POSTS_LOADER_ID;
        }
        if(profileId == 0){
            profileId = user.getId();
        }

        listAdapter = new GalleryGridAdapter(getActivity(), this, MAX_CARDS, true);
        useLoader();
    }

    public View getProfileLayout(LayoutInflater inflater){
        View profileView = inflater.inflate(R.layout.profile_user_info_layout, null);
        Activity activity = getActivity();
        followButton = (Button) profileView.findViewById(R.id.followButton);
        increaseFollowersButton = (Button) profileView.findViewById(R.id.increaseFollowersButton);
        Button followingButton = (Button) profileView.findViewById(R.id.followingButton);
        Button followersButton = (Button) profileView.findViewById(R.id.followersButton);
        TextView nameTv = (TextView) profileView.findViewById(R.id.nameTv);
        TextView userNameTv = (TextView) profileView.findViewById(R.id.userNameTv);
        TextView glamCountTv = (TextView) profileView.findViewById(R.id.glamCountTv);
        TextView classificationTv = (TextView) profileView.findViewById(R.id.classificationTv);
        TextView aboutMeTv = (TextView) profileView.findViewById(R.id.aboutMeText);
        profileImage = (RoundedImageView) profileView.findViewById(R.id.profileThumbnail);
        ImageView moreButton = (ImageView) profileView.findViewById(R.id.moreButton);

        nameTv.setTypeface(FTUtils.loadFont(activity.getAssets(), activity.getString(R.string.font_helvatica_lt)));
        userNameTv.setTypeface(FTUtils.loadFont(activity.getAssets(), activity.getString(R.string.font_helvatica_lt)));
        classificationTv.setTypeface(FTUtils.loadFont(activity.getAssets(), activity.getString(R.string.font_helvatica_lt)));
        followButton.setTypeface(FTUtils.loadFont(activity.getAssets(), activity.getString(R.string.font_helvatica_thin)));
        followingButton.setTypeface(FTUtils.loadFont(activity.getAssets(), activity.getString(R.string.font_helvatica_lt)));
        followersButton.setTypeface(FTUtils.loadFont(activity.getAssets(), activity.getString(R.string.font_helvatica_lt)));
        aboutMeTv.setTypeface(FTUtils.loadFont(activity.getAssets(), activity.getString(R.string.font_helvatica_lt)));
        glamCountTv.setTypeface(FTUtils.loadFont(activity.getAssets(), activity.getString(R.string.font_helvatica_bold)));
        if(myProfile){
            followButton.setVisibility(View.INVISIBLE);
            increaseFollowersButton.setVisibility(View.VISIBLE);
            moreButton.setVisibility(View.GONE);
            increaseFollowersButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Log.d(TAG,"ON CLICK");
                    shareMenuOnClick();
                }
            });
            Log.d(TAG, "INCREASE FOLLOWERS BUTTTON");
        }else{
            //TODO user should be updated
            isFollowing = (user.getIsFollowing() == 1) ? true : false;
            isBlocked = (user.getIsBlocked() == 1) ? true : false;
            if(isFollowing){
                followButton.setText(getString(R.string.unfollow));
                followButton.setBackgroundResource(R.drawable.unfollow_button_drawable);
            }
            moreButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    BlockTask task = new BlockTask(ProfileFragment.this, !isBlocked, profileId);
                    task.execute();
                }
            });
            if(isBlocked){
                listView.setVisibility(View.INVISIBLE);
            }else{
                listView.setVisibility(View.VISIBLE);
            }

            followButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "BUTTON FOLLOW is clicked ");
                    progressBar.setVisibility(View.VISIBLE);

/*                    if(isFollowing){
                        followButton.setBackgroundResource(R.drawable.follow_button_drawable);
                        followButton.setText(getString(R.string.follow));
                    }else{
                        followButton.setBackgroundResource(R.drawable.unfollow_button_drawable);
                        followButton.setText(getString(R.string.unfollow));
                    }*/
                    FollowTask task = new FollowTask(ProfileFragment.this,getActivity(),!isFollowing, user.getId(), followButton);
                    app.executeAsyncTask(task, null);
                    //task.execute();
                }
            });
        }

        followersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProfileActivity act = (ProfileActivity)getActivity();
                act.openFollowListScreen(true);
            }
        });

        followingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProfileActivity act = (ProfileActivity)getActivity();
                act.openFollowListScreen(false);
            }
        });
        //TODO REVISIT 100x100
        String url = new StringBuilder(Constants.CLOUD_FRONT_URL).append("/100x100/").append(user.getPhotoPath()).toString();
        ImageLoader.getInstance().displayImage(url, profileImage, app.options);
        String tvNameTxt = user.getFirstName() +" " + user.getLastName();
        String aboutTxt = user.getAbout();
        try{
            nameTv.setText(StringEscapeUtils.unescapeJson(tvNameTxt));
            aboutMeTv.setText(StringEscapeUtils.unescapeJson(aboutTxt));
        }catch(Exception e){
            nameTv.setText(tvNameTxt);
            aboutMeTv.setText(aboutTxt);
        }
        //nameTv.setText(StringEscapeUtils.unescapeJson(user.getFirstName() +" " + user.getLastName()));
        userNameTv.setText(user.getUserName());
        glamCountTv.setText(user.getGlamCountPattern().concat(getString(R.string.glam)));
        classificationTv.setText(classifyUser(user.getGlamCount()));
        classificationTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClassificationDialog dialog = new ClassificationDialog();
                Bundle bundle = new Bundle();
                bundle.putInt("DIALOG_NO", ClassificationDialog.CLASSIFICATION_DIALOG);
                dialog.setArguments(bundle);
                dialog.show(getActivity().getSupportFragmentManager(), "ClassificationDialog");
            }
        });
        return profileView;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_profile, container, false);
        shareMenu = (LinearLayout) rootView.findViewById(R.id.shareMenuLayout);
        shareFb = (LinearLayout) rootView.findViewById(R.id.facebookShareButton);
        shareTwitter = (LinearLayout) rootView.findViewById(R.id.twitterShareButton);
        shareInstagram = (LinearLayout) rootView.findViewById(R.id.instagramShareButton);

        progressBar = (RelativeLayout) rootView.findViewById(R.id.progressBar);
        listView = (BounceListView) rootView.findViewById(R.id.uploadsList);
        View profileView = getProfileLayout(inflater);
        listView.setOnScrollListener(new GridListOnScrollListener(this));
        listView.addHeaderView(profileView);

        if(dataList != null){
            listAdapter.addItemsInGrid(dataList);
            listView.setAdapter(listAdapter);
        }

        loadMoreFooterView = getLoadMoreView(inflater);
        Log.d(TAG, "ON CREATE VIEW: galleryChanged " + ProfileActivity.imageGalleryChanged);
        if(ProfileActivity.imageGalleryChanged ){ // || ProfileActivity.userInfoChanged
            useLoader();
        }
        sendEventToGoogleAnalytics();
        initShare();

        return rootView;
    }

    private String classifyUser(int glamCount){
        if(glamCount <10000){
            return getActivity().getString(R.string.fashionTalker);
        }else if(glamCount >= 10000 && glamCount < 50000){
            return getActivity().getString(R.string.fashionLover);
        }else if(glamCount >= 50000 && glamCount < 100000){
            return getActivity().getString(R.string.raisingStar);
        }else if(glamCount >= 100000 && glamCount < 1000000){
            return getActivity().getString(R.string.fashionista);
        }else{
            return getActivity().getString(R.string.fashionIcon);
        }
    }

    private void sendEventToGoogleAnalytics(){
        app.sendAnalyticsEvent("Profile View", "UX", "PROFILE_ID", profileId + "");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "ON RESUME galleryChanged " + ProfileActivity.imageGalleryChanged);
        if(ProfileActivity.imageGalleryChanged ){ //|| ProfileActivity.userInfoChanged
            dataList = null;
            useLoader();
        }
    }

    private void initShare(){
        shareFb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //shrinkAllPanels(panels);
                increaseFollowersButton.setVisibility(View.GONE);
                Bitmap image = FTUtils.screenShot(rootView);
                increaseFollowersButton.setVisibility(View.VISIBLE);
                Log.d(TAG, "TAKE SCREEN SHOT");
                SharePhoto photo = new SharePhoto.Builder()
                        .setBitmap(image)
                        .build();
                if (ShareDialog.canShow(SharePhotoContent.class)) {
                    SharePhotoContent content = new SharePhotoContent.Builder()
                            .addPhoto(photo)
                            .build();
                    Log.d(TAG, "SHARE PHOTO");
                    ShareDialog dialog = new ShareDialog(ProfileFragment.this);
                    dialog.show(content);
                }
            }
        });

        shareTwitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                increaseFollowersButton.setVisibility(View.GONE);
                Bitmap image = FTUtils.screenShot(rootView);
                increaseFollowersButton.setVisibility(View.VISIBLE);
                Log.d(TAG, "TAKE SCREEN SHOT");
                TweetComposer.Builder builder = new TweetComposer.Builder(getActivity())
                        .text(getString(R.string.share_pic_from_twitter))
                        .image(FTUtils.getImageUri(getActivity(), image, "tmpImage"));
                builder.show();
            }
        });
        shareInstagram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                increaseFollowersButton.setVisibility(View.GONE);
                Bitmap image = FTUtils.screenShot(rootView);
                increaseFollowersButton.setVisibility(View.VISIBLE);
                Log.d(TAG, "TAKE SCREEN SHOT");
                createInstagramIntent("image/*", FTUtils.getImageUri(getActivity(), image, "tmpImage"));
            }
        });
    }

    private void createInstagramIntent(String type, Uri uri){ // String mediaPath
        // Create the new Intent using the 'Send' action.
        Intent share = new Intent(Intent.ACTION_SEND);
        // Set the MIME type
        share.setType(type);
        // Create the URI from the media
        //File media = new File(mediaPath);
        //Uri uri = Uri.fromFile(media);
        // Add the URI to the Intent.
        share.putExtra(Intent.EXTRA_STREAM, uri);
        // Broadcast the Intent.
        startActivity(Intent.createChooser(share, "Share to"));
    }

    private void shareMenuOnClick(){
        Animation slide;
        if(shareMenuVisible){
            slide = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_down);
            Log.d(TAG,"ON CLICK - slide down");
        }else{
            shareMenu.setVisibility(View.VISIBLE);
            shareMenu.bringToFront();
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

    @Override
    public Loader<ArrayList<Post>> onCreateLoader(int id, Bundle args) {
        loader = new PostsLoader(getActivity().getApplicationContext(), id, profileId);
        return loader;
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<Post>> loader, final ArrayList<Post> data) {
        if(data == null){
            Log.d(TAG, "DATA IS NULL");
            Handler handler = new Handler();
            handler.post(new Runnable() {
                @Override
                public void run() {
                    app.openOKDialog(ProfileFragment.this.getActivity(), ProfileFragment.this, "no_connection");
                }
            });

            return;
        }
        Log.d(TAG, "ON LOAD FINISHED: " + data.size() + " - profileId: " + profileId);
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                itemCountPerLoad = 0;
                if (dataList == null) {
                    index = 0;
                    loadData(data);
                    if (listView != null)
                        listView.setAdapter(listAdapter);
                    if (canLoadMoreData()) {
                        listView.addFooterView(loadMoreFooterView);
                        loadMoreFooterView.setVisibility(View.VISIBLE);
                    }
                } else {
                    //Log.d(TAG, "LOAD MORE DATA TO THE ADAPTER: ");
                    loadData(data);
                    listAdapter.notifyDataSetChanged();
                }

                if (!canLoadMoreData()) {
                    if (listView != null) {
                        try {
                            listView.removeFooterView(loadMoreFooterView);
                        } catch (Exception e) {

                        }
                        loadMoreFooterView.setVisibility(View.INVISIBLE);
                    }
                }
            }
        });

        loading = false;
    }

    private void loadData(ArrayList<Post> data){
        dataList = new ArrayList<GalleryItem>();
        for (Post post : data){
            //Log.d(TAG, "COVER PATH: " + post.getPhoto());
            //ImagePathTask task = new ImagePathTask("galleries/1419693538.203064jpg");
            //String path = new StringBuilder(Constants.CLOUD_FRONT_URL).append("/300x300/").append(post.getPhoto()).toString();
            GalleryItem galleryItem = new GalleryItem(index, post.getId(), "", post.getPhoto());
            Log.d(TAG, "DATA PHOTO URL: " + post.getPhoto());
            index ++;
            dataList.add(galleryItem);
        }
        itemCountPerLoad = data.size();
        listAdapter.addItemsInGrid(dataList);
        if(loaderId == Constants.USER_POSTS_LOADER_ID){
            app.addUserPostArrayList(data);
        }else{
            app.addMyPostArrayList(data);
        }
        //addToGlobalLists(data);
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<Post>> loader) {
        dataList = null;
        index = 0;
        loading = false;
    }

    @Override
    public void reachedToEnd() {
        //Load More Data
        loadMoreFooterView.setVisibility(View.VISIBLE);
        Log.d(TAG, "ON REACHED TO END");
        useLoader();
    }

    public boolean canLoadMoreData() {
        if(loader == null || ProfileActivity.imageGalleryChanged ) // || ProfileActivity.userInfoChanged
            return true;

        return loader.perPage > itemCountPerLoad  ? false : true;//listData.size() < getMaxAllowedItems();
    }

    public void calculateLoadValues(){
        if(dataList == null){
            loader.startIndex = 0;
        }else{
            loader.startIndex += loader.perPage;
        }
    }

    @SuppressLint("InflateParams")
    protected View getLoadMoreView(LayoutInflater inflater) {
        ProgressBar loadMoreProgress = (ProgressBar) inflater.inflate(
                R.layout.auto_load_more_view, null);
        //loadMoreProgress.setBackgroundColor(Color.LTGRAY);
        return loadMoreProgress;
    }

    @Override
    public void postOnItemClicked(int postId, int postItemPosition) {
        Intent intent = new Intent(getActivity(), PostsActivity.class);
        intent.putExtra("LOADER_ID", loaderId);
        intent.putExtra("POST_ID", postId);
        intent.putExtra("POST_INDEX", postItemPosition);
        //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        this.startActivity(intent);
        BaseActivity.setTranslateAnimation(getActivity());
        //this.getActivity().overridePendingTransition(R.anim.activity_open_scale, R.anim.activity_close_translate);
    }

    public void useLoader() {
        if (this.canLoadMoreData() && !loading) {
            Log.d(TAG, "USE LOADER FRAGMENT Profile Fragment");
            loading = true;
            if(loader == null ){
                loader = (PostsLoader) getActivity().getLoaderManager()
                        .initLoader(loaderId, null, this);
                loader.forceLoad();

            }else{
                Log.d(TAG, "USE LOADER FRAGMENT Profile Fragment - ON CONTENT CHANGEDD");
                if(ProfileActivity.imageGalleryChanged ){ //|| ProfileActivity.userInfoChanged
                    loader = (PostsLoader) getActivity().getLoaderManager()
                            .restartLoader(loaderId, null, this);
                    //TODO Control this
                    dataList = null;
                    listAdapter = new GalleryGridAdapter(getActivity(), this, MAX_CARDS, true);
                    if(ProfileActivity.imageGalleryChanged){
                        ProfileActivity.imageGalleryChanged = false;
                    }
//                    if(ProfileActivity.userInfoChanged){
//                        ProfileActivity.userInfoChanged = false;
//                    }
                }
                //loader.startLoading(); //= (PopularPostsLoader) getActivity().getLoaderManager()
                // .restartLoader(Constants.POPULAR_POSTS_LOADER_ID, null, this);
                // loader.onContentChanged();
                loader.forceLoad();
            }
            calculateLoadValues();
        }else{
            listView.removeFooterView(loadMoreFooterView);
        }
    }

    @Override
    public void isFollowed(boolean success, int userId) {
        progressBar.setVisibility(View.GONE);
        if(success){
            isFollowing = true;
            followButton.setBackgroundResource(R.drawable.unfollow_button_drawable);
            followButton.setText(getString(R.string.unfollow));
        }else{
            //showErrorMessage();
        }
    }

    @Override
    public void isUnfollowed(boolean success, int userId) {
        progressBar.setVisibility(View.GONE);
        if(success){
            isFollowing = false;
            followButton.setBackgroundResource(R.drawable.follow_button_drawable);
            followButton.setText(getString(R.string.follow));
        }else{
           // showErrorMessage();
        }
    }

    @Override
    public void isUserBlocked(final boolean success, final boolean blocked) {
        progressBar.setVisibility(View.GONE);
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String text = "";
                if(success){
                    text = blocked ? getString(R.string.block_user): getString(R.string.unblock_user);
                    isBlocked = !isBlocked;
                }else{
                    text = getString(R.string.problem_occured);
                }
                Toast.makeText(getActivity(), text, Toast.LENGTH_LONG).show();
            }
        });
    }
}
