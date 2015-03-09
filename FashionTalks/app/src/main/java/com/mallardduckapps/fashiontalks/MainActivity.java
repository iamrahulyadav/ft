package com.mallardduckapps.fashiontalks;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.mallardduckapps.fashiontalks.adapters.GalleriesPagerAdapter;
import com.mallardduckapps.fashiontalks.fragments.BasicFragment;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewPager.setAdapter(new GalleriesPagerAdapter(getSupportFragmentManager(), this));
        setSlidingStrips();
        mViewPager.setCurrentItem(1);
        //menu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
        regId = getRegistrationId(getApplicationContext());
        Log.d(TAG, "REGISTRATION ID: " + regId);

        if (regId.isEmpty()) {
            GCMTask task = new GCMTask(this, app.dataSaver);
            task.registerInBackground();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "ON RESUME");
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        if(menu.isMenuShowing()){
            finish();
        }else{
            menu.toggle();
        }
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
