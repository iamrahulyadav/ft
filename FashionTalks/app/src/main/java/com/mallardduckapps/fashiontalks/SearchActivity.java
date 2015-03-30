package com.mallardduckapps.fashiontalks;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.facebook.appevents.AppEventsLogger;
import com.mallardduckapps.fashiontalks.fragments.SearchBrandFragment;
import com.mallardduckapps.fashiontalks.fragments.SearchUserFragment;
import com.mallardduckapps.fashiontalks.utils.FTUtils;

public class SearchActivity extends ActionBarActivity {

    Toolbar mainToolbar;
    FashionTalksApp app;
    ActionBar actionBar;
    final String TAG = "SearchActivity";
    boolean userSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        userSearch = getIntent().getBooleanExtra("OPENS_USER_SEARCH", false);
        mainToolbar = (Toolbar)findViewById(R.id.mainToolbar);
        setSupportActionBar(mainToolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        TextView tvName = (TextView) findViewById(R.id.toolbarName);
        tvName.setTypeface(FTUtils.loadFont(getAssets(), getString(R.string.font_avantgarde_bold)));
        if (savedInstanceState == null) {
            Fragment fragment;
            if(userSearch){
                 fragment = SearchUserFragment.newInstance();
            }else{
                fragment = SearchBrandFragment.newInstance();
            }
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, fragment)
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_settings, menu);
        return true;
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
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home){
            if (getSupportFragmentManager().getBackStackEntryCount() > 0 ){
                getSupportFragmentManager().popBackStack();
            } else {
                finish();
                BaseActivity.setBackwardsTranslateAnimation(this);
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
