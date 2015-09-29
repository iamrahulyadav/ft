package com.mallardduckapps.fashiontalks;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.mallardduckapps.fashiontalks.adapters.GalleriesPagerAdapter;
import com.mallardduckapps.fashiontalks.fragments.BasicFragment;
import com.mallardduckapps.fashiontalks.fragments.NavigationDrawerFragment;
import com.mallardduckapps.fashiontalks.services.RestClient;
import com.mallardduckapps.fashiontalks.tasks.GCMTask;
import com.mallardduckapps.fashiontalks.utils.Constants;
import com.mallardduckapps.fashiontalks.utils.FTUtils;

import org.json.JSONException;

import java.io.IOException;


public class MainActivity extends BaseActivity
        implements
        BasicFragment.OnFragmentInteractionListener {

    String regId;
    protected final String TAG = "MAIN_ACTIVITY";
    boolean paused;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewPager.setAdapter(new GalleriesPagerAdapter(getSupportFragmentManager(), this));
        setSlidingStrips();
        int postUploaded = getIntent().getIntExtra("POST_UPLOADED", 0);
        if(postUploaded == Constants.STATUS_CODE_POST_UPLOADED){
            mViewPager.setCurrentItem(0);
        }else{
            mViewPager.setCurrentItem(1);
        }

        //menu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
        regId = getRegistrationId(getApplicationContext());
        Log.d(TAG, "REGISTRATION ID: " + regId);
        getDensity();
        if (regId.equals("")) {
            GCMTask task = new GCMTask(this, app.dataSaver);
            task.registerInBackground();
        }
/*        Bundle extras = getIntent().getExtras();
        if(extras != null){
            String direction = extras.getString("DIRECTION");
            Log.d(TAG, "MAIN ACTIVITY DIRECTION: " + direction);
            if(direction != null){
                Intent intent = new Intent(MainActivity.this, NotificationActivity.class);
                this.startActivity(intent);
                this.finish();
                BaseActivity.setTranslateAnimation(this);
            }
        }*/
    }

    private void getDensity(){
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        switch(metrics.densityDpi){
            case DisplayMetrics.DENSITY_LOW:
                Log.e(TAG, "DENSITY LOW");
                break;
            case DisplayMetrics.DENSITY_MEDIUM:
                Log.e(TAG, "DENSITY MEDIUM");
                break;
            case DisplayMetrics.DENSITY_HIGH:
                Log.e(TAG, "DENSITY HIGH");
                break;
            case DisplayMetrics.DENSITY_XHIGH:
                Log.e(TAG, "DENSITY XHIGH");
                break;
            case DisplayMetrics.DENSITY_XXHIGH:
                Log.e(TAG, "DENSITY XXHIGH");
                break;
            case DisplayMetrics.DENSITY_XXXHIGH:
                Log.e(TAG, "DENSITY XXXHIGH");
                break;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        paused = true;
        //Log.d(TAG, "ON PAUSE MAIN");
    }

    @Override
    protected void onNewIntent(Intent intent) {
        //super.onNewIntent(intent);
        //Log.d(TAG, "ON MAIN ACTIVITY DIRECTION (on NEW INTENT): ");
        Bundle extras = intent.getExtras();
        if(extras != null){
            String direction = extras.getString("DIRECTION");
            Log.d(TAG, "ON MAIN ACTIVITY DIRECTION (on NEW INTENT): " + direction);
            this.finish();
            Intent intent1 = new Intent(MainActivity.this, NotificationActivity.class);
            this.startActivity(intent1);
            NavigationDrawerFragment.mCurrentSelectedPosition = 2;
            //app.direction = "";
            BaseActivity.setTranslateAnimation(this);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "ON RESUME MAIN");
        Bundle extras = getIntent().getExtras();
        if(extras != null){
            int postUploaded = getIntent().getIntExtra("POST_UPLOADED", 0);
            if(postUploaded == Constants.STATUS_CODE_POST_UPLOADED) {
                mViewPager.setCurrentItem(0);
                return;
            }
            String direction = extras.getString("DIRECTION");
            Log.d(TAG, "MAIN ACTIVITY DIRECTION (on RESUME): " + direction);
            this.finish();
            Intent intent1 = new Intent(MainActivity.this, NotificationActivity.class);
            this.startActivity(intent1);
            NavigationDrawerFragment.mCurrentSelectedPosition = 2;
            //app.direction = "";
            BaseActivity.setTranslateAnimation(this);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        if(menu.isMenuShowing()){
//            finish();
//        }else{
//            menu.toggle();
//        }
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
/*        if (id == R.id.action_settings) {
            //CALL TEST NOTIFICATION
            testNotification();
            return true;
        }else*/
        if(id == R.id.action_search){
            Intent intent = new Intent(MainActivity.this, SearchActivity.class);
            //testNotification();
            intent.putExtra("OPENS_USER_SEARCH", false);
            startActivity(intent);
            BaseActivity.setTranslateAnimation(this);
        }
        else {
            Log.d(TAG,"MENU TOGGLE");
            menu.toggle();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFragmentInteraction(String tag) {

    }

    @Override
    public void onToolbarThemeChange(int themeId) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == Constants.STATUS_CODE_POST_UPLOADED) {
                Log.d(TAG, "PAST TO MY FEED TAB - MAIN ACTIVITY");
                mViewPager.setCurrentItem(0);
            }
        }
    }

    /*   public void testNotification(){

        new AsyncTask() {

            @Override
            protected Object doInBackground(Object[] params) {

                String response = "";
                RestClient client = new RestClient();
                try {
                    response = client.doGetRequestTest(Constants.TEST_NOTIFICATIONS,null);
                    Log.d("TEST NOTIF", "RESPONSE: " + response);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return response;
            }
        }.execute(null,null,null);
    }*/

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
