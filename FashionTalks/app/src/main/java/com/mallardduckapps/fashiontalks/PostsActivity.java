package com.mallardduckapps.fashiontalks;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.facebook.appevents.AppEventsLogger;
import com.mallardduckapps.fashiontalks.adapters.GalleryGridAdapter.RefreshPagerCallback;
import com.mallardduckapps.fashiontalks.adapters.VerticalPagerAdapter;
import com.mallardduckapps.fashiontalks.fragments.BasicFragment;
import com.mallardduckapps.fashiontalks.fragments.CommentsFragment;
import com.mallardduckapps.fashiontalks.fragments.PopularPostsFragment;
import com.mallardduckapps.fashiontalks.fragments.PostFragment;
import com.mallardduckapps.fashiontalks.objects.Post;
import com.mallardduckapps.fashiontalks.tasks.PopularPostTask;
import com.mallardduckapps.fashiontalks.utils.Constants;
import com.mallardduckapps.fashiontalks.utils.FTUtils;

import java.util.ArrayList;

import fr.castorflex.android.verticalviewpager.VerticalViewPager;

public class PostsActivity extends AppCompatActivity implements BasicFragment.OnFragmentInteractionListener, CommentsFragment.CommentIsMade,
        VerticalPagerAdapter.LoadMorePostToPager, PopularPostTask.NewPostsLoaded, RefreshPagerCallback {

    Toolbar mainToolbar;
    FashionTalksApp app;
    ActionBar actionBar;
    public static String activeFragmentTag = "";
    int galleryId; // can be 0 if opened from post.. if not then the caller is galleries
    // can be 0 if opened from gallery.. if not then the caller is post
    // 3 post fragments can be distinguished from loaderID's. Look at Constants.java
    int postId;
    int positionIndex;
    int loaderId;
    boolean isMyPosts;
    private final String TAG = "POSTS ACTIVITY";
    private static final float MIN_SCALE = 0.95f;
    private static final float MIN_ALPHA = 0.70f;
    public static int width;
    public static int height;
    boolean openComment = false;
    VerticalViewPager verticalViewPager;
    ArrayList<Post> postArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posts);
        int[] size = FTUtils.getScreenSize(this);
        width = size[0];
        height = size[1];
        galleryId = getIntent().getIntExtra("GALLERY_ID", 0);
        postId = getIntent().getIntExtra("POST_ID", 0);
        loaderId = getIntent().getIntExtra("LOADER_ID", 0);
        positionIndex = getIntent().getIntExtra("POST_INDEX", -1);
        openComment = getIntent().getBooleanExtra("OPEN_COMMENT", false);
        isMyPosts = getIntent().getBooleanExtra("MY_PROFILE", false);
        //String listJson = getIntent().getStringExtra("POST_LIST");
        Log.d(TAG, "GALLERY ID: " + galleryId + "- loaderId: " + loaderId + " - positionIndex: " + positionIndex);
        app = (FashionTalksApp) getApplication();
        mainToolbar = (Toolbar) findViewById(R.id.mainToolbar);
        setSupportActionBar(mainToolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        TextView tvName = (TextView) findViewById(R.id.toolbarName);
        tvName.setTypeface(FTUtils.loadFont(getAssets(), getString(R.string.font_avantgarde_bold)));

        if (positionIndex != -1) {
            verticalViewPager = (VerticalViewPager) findViewById(R.id.verticalviewpager);

            //ArrayList<Post> defaultArray = new ArrayList<>(getPostsArrayList());
            //app.setDefaultPostArray(defaultArray);

            postArrayList = new ArrayList<>(); //FTUtils.convertStringToPostArray(listJson)
            postArrayList = getIntent().getParcelableArrayListExtra("POST_LIST");
            Log.d(TAG, "GALLERY :postarraylist size: " + postArrayList.size());
            VerticalPagerAdapter adapter = new VerticalPagerAdapter(getSupportFragmentManager(), postArrayList, this, loaderId);//getPostsArrayList()
            verticalViewPager.setAdapter(adapter);
            //verticalViewPager.setPageMargin(getResources().getDimensionPixelSize(R.dimen.page_margin));
            //verticalViewPager.setPageMarginDrawable(new ColorDrawable(getResources().getColor(android.R.color.holo_green_dark)));
            verticalViewPager.setCurrentItem(positionIndex);
            verticalViewPager.setPageTransformer(true, new ViewPager.PageTransformer() {
                @Override
                public void transformPage(View view, float position) {
                    int pageWidth = view.getWidth();
                    int pageHeight = view.getHeight();

                    if (position < -1) { // [-Infinity,-1)
                        // This page is way off-screen to the left.
                        view.setAlpha(0);
                    } else if (position <= 1) { // [-1,1]
                        // Modify the default slide transition to shrink the page as well
                        float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));
                        float vertMargin = pageHeight * (1 - scaleFactor) / 2;
                        float horzMargin = pageWidth * (1 - scaleFactor) / 2;
                        if (position < 0) {
                            view.setTranslationY(vertMargin - horzMargin / 2);
                        } else {
                            view.setTranslationY(-vertMargin + horzMargin / 2);
                        }
                        // Scale the page down (between MIN_SCALE and 1)
                        view.setScaleX(scaleFactor);
                        view.setScaleY(scaleFactor);
                        // Fade the page relative to its size.
                        view.setAlpha(MIN_ALPHA +
                                (scaleFactor - MIN_SCALE) /
                                        (1 - MIN_SCALE) * (1 - MIN_ALPHA));
                    } else { // (1,+Infinity]
                        // This page is way off-screen to the right.
                        view.setAlpha(0);
                    }
                }
            });
        } else {
            //Opens post fragment from notifications, in here post is not downloaded from server yet
            //so need to use loader to get the post from server
            openPostFragment(postId, positionIndex, loaderId, true);
        }

        if (galleryId != 0) {
            PopularPostsFragment galleryFragment = new PopularPostsFragment();
            //galleryFragment.setActivity(activity);
            Bundle bundle = new Bundle();
            bundle.putInt("LOADER_ID", Constants.GALLERY_POSTS_LOADER_ID);
            bundle.putInt("GALLERY_ID", galleryId);
            galleryFragment.setArguments(bundle);
            replaceFragment(galleryFragment, false);
        } else {
            //openPostFragment(postId, positionIndex, loaderId, false);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "TEST ON RESUME: gallerID: " + galleryId + "loaderId: " + loaderId);
        AppEventsLogger.activateApp(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        AppEventsLogger.deactivateApp(this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.d(TAG, "TEST ON NEW INSTANCE");
        galleryId = intent.getIntExtra("GALLERY_ID", 0);
        postId = intent.getIntExtra("POST_ID", 0);
        loaderId = intent.getIntExtra("LOADER_ID", 0);
        positionIndex = intent.getIntExtra("POST_INDEX", -1);
    }

    private void replaceFragment(BasicFragment fragment, boolean addToBackStack) {
        FragmentTransaction fragmentTx = getSupportFragmentManager().beginTransaction();
        fragmentTx.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_from_left, R.anim.enter_from_left, R.anim.exit_from_right);
        //fragmentTx.setCustomAnimations(android.R.anim.slide_in_left,android.R.anim.slide_out_right, android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        fragmentTx.replace(R.id.container, fragment, fragment.TAG);
        if (addToBackStack) {
            fragmentTx.addToBackStack(fragment.TAG);
        }
        fragmentTx.commit();
        activeFragmentTag = fragment.TAG;
    }

    public void openPostFragment(int postId, int positionIndex, int loaderId, boolean addToBackStack) {
        this.postId = postId;
        this.positionIndex = positionIndex;
        this.loaderId = loaderId;
        PostFragment postFragment = new PostFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("LOADER_ID", loaderId);
        bundle.putInt("POST_ID", postId);
        bundle.putInt("POST_INDEX", positionIndex);
        bundle.putBoolean("OPEN_COMMENT", openComment);
        //TODO newly added might be wrong cause fragment sometimes wants null object to retrieve post again

        if(positionIndex != -1){
            Post post = postArrayList.get(positionIndex);//194 nullpointer
            bundle.putParcelable("POST", post); // FTUtils.convertPostToString(postArrayList.get(positionIndex))
        }
//        int userId = 0;
//        try{
//            userId = post.getUserId();
//        }catch(Exception e){
//            e.printStackTrace();
//        }
//        intent.putExtra("MY_PROFILE",app.isUserMe(userId));


        postFragment.setArguments(bundle);
        replaceFragment(postFragment, addToBackStack);
    }

 /*   public ArrayList getPostsArrayList() {
        ArrayList list = new ArrayList();
        switch (loaderId) {
            case Constants.FEED_POSTS_LOADER_ID:
                list = app.getFeedPostArrayList();
                break;
            case Constants.POPULAR_POSTS_LOADER_ID:
                list = app.getPopularPostArrayList();
                break;
            case Constants.GALLERY_POSTS_LOADER_ID:
                list = app.getGalleryPostArrayList();
                // Log.d(TAG, "data 0 name: " + ((Post)list.get(0)).getUser().getUserName() );
                break;
            case Constants.USER_POSTS_LOADER_ID:
                list = app.getUserPostArrayList();
                break;
            case Constants.MY_POSTS_LOADER_ID:
                list = app.getMyPostArrayList();
                break;
            case Constants.GALLERY_POSTS_BY_TAG_LOADER_ID:
                list = app.getBrandGalleryPostList();
                break;
        }
        if (list == null) {
            return new ArrayList();
        }

        return list;
    }*/

/*    public void setPostsArrayList() {
        switch (loaderId) {
            case Constants.FEED_POSTS_LOADER_ID:
                app.setFeedPostArrayList(app.defaultPostArray);
                break;
            case Constants.POPULAR_POSTS_LOADER_ID:
                app.setPopularPostArrayList(app.defaultPostArray);
                break;
            case Constants.GALLERY_POSTS_LOADER_ID:
                Log.d(TAG, "TEST ON SET GALLERY POST ARRAY: " + app.defaultPostArray.get(0).getUser().getUserName());
                app.setGalleryPostArrayList(app.defaultPostArray);
                // Log.d(TAG, "data 0 name: " + ((Post)list.get(0)).getUser().getUserName() );
                break;
            case Constants.USER_POSTS_LOADER_ID:
                app.setUserPostArrayList(app.defaultPostArray);
                break;
            case Constants.MY_POSTS_LOADER_ID:
                app.setMyPostArrayList(app.defaultPostArray);
                break;
            case Constants.GALLERY_POSTS_BY_TAG_LOADER_ID:
                app.setBrandGalleryPostList(app.defaultPostArray);
                break;
        }
    }*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        if(isMyPosts){
            menu.removeItem(R.id.action_home);
        }else{
            menu.removeItem(R.id.action_user_info);
        }
        return true;
    }

    //TODO
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //Log.d(TAG, "ON BACK MENU BUTTON PRESSED");
        int id = item.getItemId();
        super.onOptionsItemSelected(item);
        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            close();
            return true;
        } else if (id == R.id.action_home) {
          //  setPostsArrayList();
//            Intent intent = new Intent(PostsActivity.this, MainActivity.class);
//            //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//            this.startActivity(intent);
//            finish();
//            BaseActivity.setBackwardsTranslateAnimation(this);
            return false;
        }else if(id == R.id.action_user_info){
            return false;
        }
        return true;
    }

    public static void hide_keyboard_from(Context context, View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void close() {

        // verticalViewPager.setAdapter(new VerticalPagerAdapter(getSupportFragmentManager(), getPostsArrayList(),this, loaderId));
        if (loaderId == Constants.NOTIFICATION_MY_POST_LOADER_ID || loaderId == Constants.NOTIFICATION_OTHER_POST_LOADER_ID) {
            finish();
            BaseActivity.setBackwardsTranslateAnimation(this);
            return;
        }
        //setPostsArrayList();
        if (getSupportFragmentManager().getBackStackEntryCount() > 0 && !openComment) {
            Log.d(TAG, "ON CLOSE HIDE KEYBOARD ");
            if (mainToolbar != null) {
                hide_keyboard_from(this, mainToolbar);
            }
            getSupportFragmentManager().popBackStack();
        } else {
            finish();
            BaseActivity.setBackwardsTranslateAnimation(this);
        }
    }

    @Override
    public void onBackPressed() {
        //Log.d(TAG, "On BACK PRESSED - getFragmentManager().getBackStackEntryCount(): " + getFragmentManager().getBackStackEntryCount());
        close();
    }

    @Override
    public void onFragmentInteraction(String tag) {

    }

    @Override
    public void onToolbarThemeChange(int themeId) {

    }

    @Override
    public void onNewComment(int postLoaderId, int postId, int postIndex, boolean increment) {
        try {
            Post post = postArrayList.get(postIndex);//getPost(postLoaderId, postIndex);
            int commentCount = post.getCommentCount();
            if (increment) {
                commentCount++;
            } else {
                commentCount--;
            }

            Log.d(TAG, "INCREMENT COMMENT. " + commentCount);
            post.setCommentCount(commentCount);

            postArrayList.set(postIndex, post);
            //setPost(post, postIndex);
            PostFragment.commentCount = commentCount;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

 /*   private Post getPost(int loaderId, int postIndex) {
        Post post = null;
        switch (loaderId) {
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

    private void setPost(Post post, int postIndex) {
        switch (loaderId) {
            case Constants.FEED_POSTS_LOADER_ID:
                app.getFeedPostArrayList().set(postIndex, post);
                break;
            case Constants.POPULAR_POSTS_LOADER_ID:
                //Log.d(TAG, "** SET POPULAR POST LIST : " );
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
    }*/

    @Override
    public void loadMorePost(int position, int count, int loaderId) {
        Log.d(TAG, "**LOAD MORE POSTS GET CALLED: " + galleryId + " - perpage: " + count);
        int tempGallery = 0;
        if (galleryId == 0) {
            Log.d(TAG, "ATTENSITON: ** GalleryID 0");
            tempGallery = app.getGalleryId();
        } else {
            tempGallery = galleryId;
        }
        try {
            int userId = app.me.getId();
            if (loaderId == Constants.USER_POSTS_LOADER_ID) {
                userId = app.getOther().getId();
            }
            PopularPostTask task = new PopularPostTask(this, loaderId, tempGallery, position, userId, count);
            if(app != null){
                app.executeAsyncTask(task, null);
            }else{
                task.execute();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void getNewPosts(final ArrayList<Post> posts, final boolean isInnerFragmentLoad) {
        try {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    for (Post post : posts) {
                        //postArrayList.add(post);
                        //Log.d(TAG, "POST LIST SIZE 0: " + postArrayList.size() + " - loadedItemSize: " + posts.size());

                        if(isInnerFragmentLoad){
                            //postArrayList.add(post);
                            ((VerticalPagerAdapter) verticalViewPager.getAdapter()).addNewItem(post);
                        }else{ //TODO can be erased?
                            postArrayList.add(post);
                        }
                       // ((VerticalPagerAdapter) verticalViewPager.getAdapter()).addNewItem(post);
                    }
                    //TODO can be erased?
                    if(!isInnerFragmentLoad){
                        ((VerticalPagerAdapter) verticalViewPager.getAdapter()).addNewItems(posts);
                    }
                /*    if(posts.size() > 1){
                        VerticalPagerAdapter adapter = new VerticalPagerAdapter(PostsActivity.this.getSupportFragmentManager(), postArrayList, PostsActivity.this, loaderId);//getPostsArrayList()
                        verticalViewPager.setAdapter(adapter);
                    }*/

                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void refreshPager(final boolean refresh) {
        Log.d(TAG, "TEST ON **REFRESH PAGER IS CALLED");
        try {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (refresh) {
                        //ArrayList<Post> defaultArray = new ArrayList<>(getPostsArrayList());
                        //app.setDefaultPostArray(defaultArray);
                        VerticalPagerAdapter adapter = new VerticalPagerAdapter(getSupportFragmentManager(), postArrayList, PostsActivity.this, loaderId);
                        verticalViewPager.setAdapter(adapter);

                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
