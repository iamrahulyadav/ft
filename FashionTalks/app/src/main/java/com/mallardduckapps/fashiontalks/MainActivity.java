package com.mallardduckapps.fashiontalks;

import android.content.Context;
import android.graphics.Point;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.widget.DrawerLayout;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.mallardduckapps.fashiontalks.adapters.SectionsPagerAdapter;
import com.mallardduckapps.fashiontalks.components.SlidingTabLayout;
import com.mallardduckapps.fashiontalks.fragments.BasicFragment;
import com.mallardduckapps.fashiontalks.fragments.NavigationDrawerFragment;
import com.mallardduckapps.fashiontalks.fragments.PlaceHolderFragment;
import com.mallardduckapps.fashiontalks.services.RestClient;
import com.mallardduckapps.fashiontalks.tasks.GCMTask;
import com.mallardduckapps.fashiontalks.utils.Constants;
import com.mallardduckapps.fashiontalks.utils.FTUtils;

import org.json.JSONException;

import java.io.IOException;


public class MainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks, ActionBar.TabListener, BasicFragment.OnFragmentInteractionListener {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;
    //SectionsPagerAdapter mSectionsPagerAdapter;
    ViewPager mViewPager;
    Toolbar mainToolbar;
    private SlidingTabLayout mSlidingTabLayout;
    private FashionTalksApp app;
    String regId;
    private final String TAG = "MAIN_ACTIVITY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        app = (FashionTalksApp) getApplication();
        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mainToolbar = (Toolbar)findViewById(R.id.mainToolbar);
        Toolbar tabToolbar = (Toolbar)findViewById(R.id.secondToolbar);
        //mainToolbar.setTitle("FASHIONTALKS");
        //mTitle = getTitle();
        setSupportActionBar(mainToolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        mViewPager.setAdapter(new SectionsPagerAdapter(getSupportFragmentManager(), this));
        mSlidingTabLayout = (SlidingTabLayout) findViewById(R.id.sliding_tabs);
        mSlidingTabLayout.setDistributeEvenly(true);
        mSlidingTabLayout.setFittingChildren(true);
        mSlidingTabLayout.setViewPager(mViewPager);
        mainToolbar.setContentInsetsAbsolute(0,0);
        tabToolbar.setContentInsetsAbsolute(0,0);
        //actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        //setPager(actionBar);
        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

        regId = getRegistrationId(getApplicationContext());
        Log.d(TAG, "REGISTRATION ID: " + regId);

        if (regId.isEmpty()) {
            GCMTask task = new GCMTask(this, app.dataSaver);
            task.registerInBackground();
        }
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        // When the given tab is selected, switch to the corresponding page in
        // the ViewPager.
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, PlaceHolderFragment.newInstance(position + 1))
                .commit();
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
               // mTitle = getString(R.string.title_section1);
                break;
            case 2:
                //mTitle = getString(R.string.title_section2);
                break;
            case 3:
                //mTitle = getString(R.string.title_section3);
                break;
        }
    }

    public void restoreActionBar() {
        //ActionBar actionBar = getSupportActionBar();
        //actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        //actionBar.setDisplayShowTitleEnabled(true);
        //actionBar.setTitle(mTitle);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            //CALL TEST NOTIFICATION
            testNotification();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onToolbarThemeChange(int themeId) {

    }

    public void testNotification(){

        new AsyncTask() {

            @Override
            protected Object doInBackground(Object[] params) {

                String response = "";
                RestClient client = new RestClient();
                try {
                    response = client.doGetRequest(Constants.TEST_NOTIFICATIONS,null);
                    Log.d("TEST NOTIF", "RESPONSE: " + response);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return response;
            }
        }.execute(null,null,null);

    }

    private String getRegistrationId(Context context) {
        String registrationId = app.dataSaver.getString("REGISTRATION_ID");
        if (registrationId.isEmpty()) {
            Log.i(TAG, "Registration not found.");
            return "";
        }
        // Check if app was updated; if so, it must clear the registration ID
        // since the existing regID is not guaranteed to work with the new
        // app version.
        int registeredVersion = app.dataSaver.getInt("APP_VERSION");
        int currentVersion = FTUtils.getAppVersion(context);
        if (registeredVersion != currentVersion) {
            Log.i(TAG, "App version changed.");
            return "";
        }
        return registrationId;
    }
}
