package com.mallardduckapps.fashiontalks;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.mallardduckapps.fashiontalks.adapters.UsersPagerAdapter;
import com.mallardduckapps.fashiontalks.fragments.BasicFragment;

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
        int id = item.getItemId();
        if (id == R.id.action_search) {
            Intent intent = new Intent(UsersActivity.this, SearchActivity.class);
            intent.putExtra("OPENS_USER_SEARCH", true);
            startActivity(intent);
            BaseActivity.setTranslateAnimation(this);
            return true;
        }else {
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
}
