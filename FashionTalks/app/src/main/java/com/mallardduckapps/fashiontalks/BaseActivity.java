package com.mallardduckapps.fashiontalks;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.mallardduckapps.fashiontalks.R;
import com.mallardduckapps.fashiontalks.adapters.GalleriesPagerAdapter;
import com.mallardduckapps.fashiontalks.adapters.UsersPagerAdapter;
import com.mallardduckapps.fashiontalks.components.SlidingTabLayout;
import com.mallardduckapps.fashiontalks.fragments.NavigationDrawerFragment;

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
        actionBar.setHomeAsUpIndicator(R.drawable.ic_drawer);
        mViewPager = (ViewPager) findViewById(R.id.view_pager);

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
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }



        return super.onOptionsItemSelected(item);
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
    public void onNavigationDrawerItemSelected(int position, String actionName) {

        if(menu != null && !actionName.equals("")){
            menu.toggle();
        }
        if(actionName.equals("NO_ACTION")){
            return;
        }
        if(actionName.equals(getString(R.string.logout))){
            app.dataSaver.remove("ACCESS_TOKEN");
        }else if(actionName.equals(getString(R.string.title_section2))){
            Intent intent = new Intent(BaseActivity.this, UsersActivity.class);
            //this.finish();
            //menu.det
            //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            this.startActivity(intent);

        }else if(actionName.equals(getString(R.string.title_section1))){
            this.finish();
        }else if(actionName.equals(getString(R.string.title_section3))){
            Intent intent = new Intent(BaseActivity.this, NotificationActivity.class);
            this.startActivity(intent);
        }
    }
}
