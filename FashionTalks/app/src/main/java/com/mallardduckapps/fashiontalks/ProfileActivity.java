package com.mallardduckapps.fashiontalks;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.mallardduckapps.fashiontalks.fragments.FollowFragment;
import com.mallardduckapps.fashiontalks.fragments.ProfileFragment;
import com.mallardduckapps.fashiontalks.utils.FTUtils;

public class ProfileActivity extends ActionBarActivity {

    Toolbar mainToolbar;
    FashionTalksApp app;
    ActionBar actionBar;
    int userId;
    final String TAG = "ProfileActivity";
    boolean onNewIntent = false;
    public static boolean imageGalleryChanged;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onNewIntent = false;
        setContentView(R.layout.activity_profile);
        userId = getIntent().getIntExtra("PROFILE_ID", 0);
        mainToolbar = (Toolbar)findViewById(R.id.mainToolbar);
        setSupportActionBar(mainToolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        TextView tvName = (TextView) findViewById(R.id.toolbarName);
        tvName.setTypeface(FTUtils.loadFont(getAssets(), getString(R.string.font_avantgarde_bold)));
        if (savedInstanceState == null) {
            ProfileFragment fragment = ProfileFragment.newInstance(userId);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, fragment)
                    .commit();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        //Log.d(TAG, "ON NEW INTENT: ");
        //userId = intent.getIntExtra("PROFILE_ID", 0);
        //onNewIntent = true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(onNewIntent){
            ProfileFragment fragment = ProfileFragment.newInstance(userId);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, fragment)
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_profile, menu);
        if(userId == 0){
            menu.removeItem(R.id.action_home);
        }else{
            menu.removeItem(R.id.action_edit_profile);
        }
        return true;
    }

    public void openFollowListScreen(boolean followers){
        String name;
        if(followers){
            name = "followers/"+userId;
        }else{
            name = "following/"+userId;
        }
        FollowFragment fragment = FollowFragment.newInstance(userId, followers);
        FragmentTransaction fragmentTx = getSupportFragmentManager().beginTransaction();
        fragmentTx.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_from_left, R.anim.enter_from_left, R.anim.exit_from_right);
        fragmentTx.add(R.id.container, fragment).addToBackStack(name).commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home){
            if (getSupportFragmentManager().getBackStackEntryCount() > 0 ){
                getSupportFragmentManager().popBackStack();
            } else {
                if(userId == 0 ){ // || app.getMe().getId() == userId
                    Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
                    startActivity(intent);
                    //BaseActivity.setTranslateAnimation(this);
                }
                finish();
                BaseActivity.setBackwardsTranslateAnimation(this);
            }
        }else if(id == R.id.action_home){
            if(userId == 0){
                Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
                startActivity(intent);
                //BaseActivity.setTranslateAnimation(this);
            }
            finish();
            BaseActivity.setBackwardsTranslateAnimation(this);
        }else if(id == R.id.action_edit_profile){
            Intent intent = new Intent(ProfileActivity.this, EditProfileActivity.class);
            startActivity(intent);
            BaseActivity.setTranslateAnimation(this);
        }
        return false; //super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        if (getSupportFragmentManager().getBackStackEntryCount() > 0 ){
            getSupportFragmentManager().popBackStack();
        } else {
            if (userId == 0 ) { // || app.getMe().getId() == userId
                Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
                startActivity(intent);

            }
            finish();
            BaseActivity.setBackwardsTranslateAnimation(this);
        }
    }
}
