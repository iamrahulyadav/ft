package com.mallardduckapps.fashiontalks;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.mallardduckapps.fashiontalks.fragments.BasicFragment;
import com.mallardduckapps.fashiontalks.fragments.CommentsFragment;
import com.mallardduckapps.fashiontalks.fragments.PostFragment;
import com.mallardduckapps.fashiontalks.objects.Post;
import com.mallardduckapps.fashiontalks.utils.Constants;
import com.mallardduckapps.fashiontalks.utils.FTUtils;

public class PostActivity extends ActionBarActivity implements BasicFragment.OnFragmentInteractionListener, CommentsFragment.CommentIsMade{

    Toolbar mainToolbar;
    FashionTalksApp app;
    ActionBar actionBar;
    public static String activeFragmentTag = "";
   // int galleryId; // can be 0 if opened from post.. if not then the caller is galleries
    // can be 0 if opened from gallery.. if not then the caller is post
    // 3 post fragments can be distinguished from loaderID's. Look at Constants.java
    int postId;
   // int positionIndex;
    //int loaderId;
    private final String TAG = "POST ACTIVITY";
    Post post;
    //boolean openComment = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posts);
        int[] size = FTUtils.getScreenSize(this);
        PostsActivity.width = size[0];
        PostsActivity.height = size[1];
        //galleryId = getIntent().getIntExtra("GALLERY_ID", 0);
        postId = getIntent().getIntExtra("POST_ID", 0);
        //loaderId = getIntent().getIntExtra("LOADER_ID", 0);
        //positionIndex = getIntent().getIntExtra("POST_INDEX", -1);
        //openComment = getIntent().getBooleanExtra("OPEN_COMMENT", false);
        //Log.d(TAG, "GALLERY ID: " + galleryId + "- loaderId: " + loaderId);
        app = (FashionTalksApp) getApplication();
        mainToolbar = (Toolbar)findViewById(R.id.mainToolbar);
        setSupportActionBar(mainToolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        TextView tvName = (TextView) findViewById(R.id.toolbarName);
        tvName.setTypeface(FTUtils.loadFont(getAssets(), getString(R.string.font_avantgarde_bold)));
        post = app.getUserFavoritePost();

        PostFragment postFragment = new PostFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("LOADER_ID", Constants.USER_FAVORITE_POST_LOADER_ID);
        bundle.putInt("POST_ID", postId);
        bundle.putInt("POST_INDEX", -1);
        //bundle.putBoolean("OPEN_COMMENT", openComment);
        postFragment.setArguments(bundle);
        replaceFragment(postFragment);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
//        if(post.getUser().getU){
            menu.removeItem(R.id.action_home);
//        }else{
//            menu.removeItem(R.id.action_user_info);
//        }
        return true;
    }

    public void replaceFragment(Fragment fragment){
        FragmentTransaction fragmentTx = this.getSupportFragmentManager().beginTransaction();
        fragmentTx.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_from_left, R.anim.enter_from_left, R.anim.exit_from_right);
        //fragmentTx.setCustomAnimations(android.R.anim.slide_in_left,android.R.anim.slide_out_right, android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        fragmentTx.replace(R.id.container, fragment, Integer.toString(postId)); //Integer.toString(post.getId())
        //fragmentTx.addToBackStack(Integer.toString(post.getId()));
        fragmentTx.commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //Log.d(TAG, "ON BACK MENU BUTTON PRESSED");
        int id = item.getItemId();
        //super.onOptionsItemSelected(item);
        //noinspection SimplifiableIfStatement
        if(id == android.R.id.home){
            close();
            return true;
        }else if(id == R.id.action_home){
            Log.d(TAG, "ACTION HOME return false");
            return false;
        }
        return false;
    }

    public static void hide_keyboard_from(Context context, View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void close(){

//        if(loaderId == Constants.NOTIFICATION_MY_POST_LOADER_ID || loaderId == Constants.NOTIFICATION_OTHER_POST_LOADER_ID){
//            finish();
//            BaseActivity.setBackwardsTranslateAnimation(this);
//            return;
//        }
        if (getSupportFragmentManager().getBackStackEntryCount() > 0 ){ // && !openComment
            Log.d(TAG, "ON CLOSE HIDE KEYBOARD - " + getSupportFragmentManager().getBackStackEntryCount());
            if(mainToolbar != null){
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
    public void onNewComment(int postLoaderId,int postId, int postIndex, boolean increment) {
        try{
            Post post = app.getUserFavoritePost();//getPost(postLoaderId, postIndex);
            int commentCount = post.getCommentCount();
            if(increment){
                commentCount ++;
            }else{
                commentCount --;
            }

            Log.d(TAG, "INCREMENT COMMENT. " + commentCount);
            post.setCommentCount(commentCount);
            app.setUserFavoritePost(post);
            PostFragment.commentCount = commentCount;
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
