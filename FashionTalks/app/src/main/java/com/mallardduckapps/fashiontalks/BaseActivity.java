package com.mallardduckapps.fashiontalks;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.facebook.appevents.AppEventsLogger;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.mallardduckapps.fashiontalks.components.SlidingTabLayout;
import com.mallardduckapps.fashiontalks.fragments.NavigationDrawerFragment;
import com.mallardduckapps.fashiontalks.utils.FTUtils;

public class BaseActivity extends ActionBarActivity implements NavigationDrawerFragment.NavigationDrawerCallbacks, ActionBar.TabListener {

    protected NavigationDrawerFragment mNavigationDrawerFragment;
    ViewPager mViewPager;
    Toolbar mainToolbar;
    Toolbar tabToolbar;
    protected SlidingTabLayout mSlidingTabLayout;
    protected FashionTalksApp app;
    protected final String TAG = "BASE_ACTIVITY";
    ActionBar actionBar;
    FrameLayout mainLayout;
    SlidingMenu menu;
    View topDivider;
    FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        app = (FashionTalksApp) getApplication();
        mainLayout = (FrameLayout) findViewById(R.id.container);
        mainToolbar = (Toolbar)findViewById(R.id.mainToolbar);
        tabToolbar = (Toolbar)findViewById(R.id.secondToolbar);
        setSupportActionBar(mainToolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.hamburger_menu);
        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        topDivider = findViewById(R.id.divider);
        TextView tvName = (TextView) findViewById(R.id.toolbarName);
        tvName.setTypeface(FTUtils.loadFont(getAssets(),getString(R.string.font_avantgarde_bold)));
/*        if(TAG.equals("MAIN_ACTIVITY")){
            mViewPager.setAdapter(new GalleriesPagerAdapter(getSupportFragmentManager(), this));
        }else{
            mViewPager.setAdapter(new UsersPagerAdapter(getSupportFragmentManager(), this));
        }*/

        mNavigationDrawerFragment = new NavigationDrawerFragment();
        menu = new SlidingMenu(getApplicationContext());
        menu.attachToActivity(this, SlidingMenu.SLIDING_WINDOW | SlidingMenu.SLIDING_CONTENT);
        menu.setMode(SlidingMenu.LEFT);
        menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
        menu.setShadowWidthRes(R.dimen.shadow_width);
        menu.setShadowDrawable(R.drawable.drawer_shadow);
        menu.setSelectorEnabled(true);
        menu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        menu.setFadeDegree(0.35f);
        menu.setBackgroundColor(Color.WHITE);
        menu.setMenu(R.layout.menu_frame);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.menu_frame, mNavigationDrawerFragment)
                .commit();

        mainToolbar.setContentInsetsAbsolute(0,0);
        tabToolbar.setContentInsetsAbsolute(0,0);
        fragmentManager = getSupportFragmentManager();
        //mNavigationDrawerFragment.setUp(
        //        R.id.navigation_drawer, (FrameLayout)findViewById(R.id.container),
        //       (DrawerLayout) findViewById(R.id.drawer_layout));

        //if(app.menu == null){

      //  }
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

    protected void setSlidingStrips(){
        mSlidingTabLayout = (SlidingTabLayout) findViewById(R.id.sliding_tabs);
        mSlidingTabLayout.setDistributeEvenly(true);
        mSlidingTabLayout.setFittingChildren(true);
        mSlidingTabLayout.setViewPager(mViewPager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_base, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        if(menu.isMenuShowing()){
            app.exitDialog(this);

            //finish(); menu.toggle();
        }else{
            menu.toggle();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "EXIT MESSAGE: " + requestCode + " - resultCode: " + resultCode);
        if(resultCode == Activity.RESULT_OK){
            if(requestCode == 666){
                finish();
            }
        }
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        mViewPager.setCurrentItem(tab.getPosition());
        Log.d(TAG, "TAB SELECTED: " + tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onNavigationDrawerItemSelected(final int position, final String actionName) {

        if(menu != null && !actionName.equals("")){
            menu.toggle();
            menu.setOnClosedListener(new SlidingMenu.OnClosedListener() {
                @Override
                public void onClosed() {
                    if(actionName.equals("NO_ACTION")){
                        return;
                    }
                    BaseActivity.this.finish();
                    if(actionName.equals(getString(R.string.title_section2))){
                        Intent intent = new Intent(BaseActivity.this, UsersActivity.class);
                        //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        BaseActivity.this.startActivity(intent);
                        //overridePendingTransition(R.anim.activity_open_scale, R.anim.activity_close_translate);
                    }else if(actionName.equals(getString(R.string.title_section1))){

                        Intent intent = new Intent(BaseActivity.this, MainActivity.class);
                        BaseActivity.this.startActivity(intent);
                    }else if(actionName.equals(getString(R.string.title_section3))){
                        Intent intent = new Intent(BaseActivity.this, NotificationActivity.class);
                        BaseActivity.this.startActivity(intent);
                    }else if(actionName.equals("PROFILE")){
                        Intent intent = new Intent(BaseActivity.this, ProfileActivity.class);
                        BaseActivity.this.startActivity(intent);
                    }else if(actionName.equals(getString(R.string.title_section4))){
                        Intent intent = new Intent(BaseActivity.this, SettingsActivity.class);
                        BaseActivity.this.startActivity(intent);
                    }else if(actionName.equals("UPLOAD")){

                        if(app.getMe().getCanPost() == 1){
                            Intent intent = new Intent(BaseActivity.this, UploadNewStyleActivity.class);
                            BaseActivity.this.startActivity(intent);
                        }else{
                            app.openOKDialog(BaseActivity.this, null, "no_post_access");
                            return;
                        }

                    }
                    setTranslateAnimation(BaseActivity.this);
                }
            });
        }
    }

    public static void setTranslateAnimation(Activity activity){
        activity.overridePendingTransition(R.anim.enter_from_right, R.anim.exit_from_left);
        //(R.anim.activity_open_scale, R.anim.activity_close_translate);
        //fragmentTx.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_from_left, R.anim.enter_from_left, R.anim.exit_from_right);
    }

    public static void setBackwardsTranslateAnimation(Activity activity){
        activity.overridePendingTransition(R.anim.enter_from_left,R.anim.exit_from_right);//,);
        //(R.anim.activity_open_scale, R.anim.activity_close_translate);
        //fragmentTx.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_from_left, R.anim.enter_from_left, R.anim.exit_from_right);
    }
}
