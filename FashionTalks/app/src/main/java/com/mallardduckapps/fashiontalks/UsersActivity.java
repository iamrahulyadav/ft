package com.mallardduckapps.fashiontalks;

import android.net.Uri;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.mallardduckapps.fashiontalks.adapters.GalleriesPagerAdapter;
import com.mallardduckapps.fashiontalks.adapters.UsersPagerAdapter;
import com.mallardduckapps.fashiontalks.components.SlidingTabLayout;
import com.mallardduckapps.fashiontalks.fragments.BasicFragment;
import com.mallardduckapps.fashiontalks.fragments.NavigationDrawerFragment;

public class UsersActivity extends BaseActivity implements
        BasicFragment.OnFragmentInteractionListener{

    protected final String TAG = "USERS_ACTIVITY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //setContentView(R.layout.activity_main);
        super.onCreate(savedInstanceState);
        mViewPager.setAdapter(new UsersPagerAdapter(getSupportFragmentManager(), this));
        setSlidingStrips();
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);
        //actionBar.setHomeButtonEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.hamburger_menu);

        //menu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
/*        app = (FashionTalksApp) getApplication();
        mNavigationDrawerFragment = new NavigationDrawerFragment();
        mainToolbar = (Toolbar)findViewById(R.id.mainToolbar);
        Toolbar tabToolbar = (Toolbar)findViewById(R.id.secondToolbar);
        setSupportActionBar(mainToolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_drawer);
        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        mViewPager.setAdapter(new SectionsPagerAdapter(getSupportFragmentManager(), this));
        mSlidingTabLayout = (SlidingTabLayout) findViewById(R.id.sliding_tabs);
        mSlidingTabLayout.setDistributeEvenly(true);
        mSlidingTabLayout.setFittingChildren(true);
        mSlidingTabLayout.setViewPager(mViewPager);
        mainToolbar.setContentInsetsAbsolute(0,0);
        tabToolbar.setContentInsetsAbsolute(0,0);
        fragmentManager = getSupportFragmentManager();
        //mNavigationDrawerFragment.setUp(
        //        R.id.navigation_drawer, (FrameLayout)findViewById(R.id.container),
        //       (DrawerLayout) findViewById(R.id.drawer_layout));
        menu = app.menu;
        menu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
        menu.setMenu(R.layout.menu_frame);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.menu_frame, mNavigationDrawerFragment)
                .commit();*/
    }

    @Override
    public void onNavigationDrawerItemSelected(int position, String actionName) {
        super.onNavigationDrawerItemSelected(position, actionName);

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
        }else {
            menu.toggle();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onToolbarThemeChange(int themeId) {

    }
}
