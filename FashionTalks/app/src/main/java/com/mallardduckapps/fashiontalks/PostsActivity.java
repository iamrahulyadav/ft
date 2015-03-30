package com.mallardduckapps.fashiontalks;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.facebook.appevents.AppEventsLogger;
import com.mallardduckapps.fashiontalks.adapters.VerticalPagerAdapter;
import com.mallardduckapps.fashiontalks.fragments.BasicFragment;
import com.mallardduckapps.fashiontalks.fragments.PopularPostsFragment;
import com.mallardduckapps.fashiontalks.fragments.PostFragment;
import com.mallardduckapps.fashiontalks.objects.Post;
import com.mallardduckapps.fashiontalks.utils.Constants;
import com.mallardduckapps.fashiontalks.utils.FTUtils;

import java.util.ArrayList;

import fr.castorflex.android.verticalviewpager.VerticalViewPager;

public class PostsActivity extends ActionBarActivity implements BasicFragment.OnFragmentInteractionListener {

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
    private final String TAG = "POSTS ACTIVITY";
    private static final float MIN_SCALE = 0.95f;
    private static final float MIN_ALPHA = 0.70f;
    public static int width;
    public static int height;
    boolean openComment = false;

    //TODO Changed so it doesnt open galleries now

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
        Log.d(TAG, "GALLERY ID: " + galleryId + "- loaderId: " + loaderId);
        app = (FashionTalksApp) getApplication();
        mainToolbar = (Toolbar)findViewById(R.id.mainToolbar);
        setSupportActionBar(mainToolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        TextView tvName = (TextView) findViewById(R.id.toolbarName);
        tvName.setTypeface(FTUtils.loadFont(getAssets(), getString(R.string.font_avantgarde_bold)));

        if(positionIndex != -1){
            VerticalViewPager verticalViewPager = (VerticalViewPager) findViewById(R.id.verticalviewpager);
            verticalViewPager.setAdapter(new VerticalPagerAdapter(getSupportFragmentManager(), getPostsArrayList(), loaderId));
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
        }else{
            //Opens post fragment from notifications, in here post is not downloaded from server yet
            //so need to use loader to get the post from server
            openPostFragment(postId, positionIndex, loaderId, true);
        }

        if(galleryId != 0){
            PopularPostsFragment galleryFragment = new PopularPostsFragment();
            //galleryFragment.setActivity(activity);
            Bundle bundle = new Bundle();
            bundle.putInt("LOADER_ID", Constants.GALLERY_POSTS_LOADER_ID);
            bundle.putInt("GALLERY_ID", galleryId);
            galleryFragment.setArguments(bundle);
            replaceFragment(galleryFragment, false);
        }else{
            //openPostFragment(postId, positionIndex, loaderId, false);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
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
        Log.d(TAG, "ON NEW INSTANCE");
        galleryId = intent.getIntExtra("GALLERY_ID", 0);
        postId = intent.getIntExtra("POST_ID", 0);
        loaderId = intent.getIntExtra("LOADER_ID", 0);
        positionIndex = intent.getIntExtra("POST_INDEX", -1);
    }

    private void replaceFragment(BasicFragment fragment, boolean addToBackStack){
        FragmentTransaction fragmentTx = getSupportFragmentManager().beginTransaction();
        fragmentTx.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_from_left, R.anim.enter_from_left, R.anim.exit_from_right);
        //fragmentTx.setCustomAnimations(android.R.anim.slide_in_left,android.R.anim.slide_out_right, android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        fragmentTx.replace(R.id.container, fragment, fragment.TAG);
        if(addToBackStack){
            fragmentTx.addToBackStack(fragment.TAG);
        }
        fragmentTx.commit();
        activeFragmentTag = fragment.TAG;
    }

    public void openPostFragment(int postId, int positionIndex, int loaderId, boolean addToBackStack){
        this.postId = postId;
        this.positionIndex = positionIndex;
        this.loaderId = loaderId;
        PostFragment postFragment = new PostFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("LOADER_ID", loaderId);
        bundle.putInt("POST_ID", postId);
        bundle.putInt("POST_INDEX", positionIndex);
        bundle.putBoolean("OPEN_COMMENT", openComment);
        postFragment.setArguments(bundle);
        replaceFragment(postFragment, addToBackStack);
    }

    public ArrayList getPostsArrayList(){
        ArrayList list = null;
        switch (loaderId){
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
        }
        return list;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d(TAG, "ON BACK MENU BUTTON PRESSED");
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        super.onOptionsItemSelected(item);
        //noinspection SimplifiableIfStatement
        if(id == android.R.id.home){
            //if (getSupportFragmentManager().getBackStackEntryCount() > 0 ){
            //    getSupportFragmentManager().popBackStack();
            //} else {
                finish();
            BaseActivity.setBackwardsTranslateAnimation(this);
            //}
        }else if(id == R.id.action_home){
            finish();
            BaseActivity.setBackwardsTranslateAnimation(this);
        }

        return true;
    }

    @Override
    public void onBackPressed() {
        Log.d(TAG, "On BACK PRESSED - getFragmentManager().getBackStackEntryCount(): " + getFragmentManager().getBackStackEntryCount());
        if (getSupportFragmentManager().getBackStackEntryCount() > 0 ){
            getSupportFragmentManager().popBackStack();
        } else {
            finish();
            BaseActivity.setBackwardsTranslateAnimation(this);
        }
    }

    @Override
    public void onFragmentInteraction(String tag) {

    }

    @Override
    public void onToolbarThemeChange(int themeId) {

    }
}
