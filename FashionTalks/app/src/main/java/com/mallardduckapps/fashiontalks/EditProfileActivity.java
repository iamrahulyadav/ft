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

import com.facebook.appevents.AppEventsLogger;
import com.mallardduckapps.fashiontalks.fragments.BasicFragment;
import com.mallardduckapps.fashiontalks.fragments.RegisterFragment;
import com.mallardduckapps.fashiontalks.utils.FTUtils;

public class EditProfileActivity extends ActionBarActivity implements BasicFragment.OnLoginFragmentInteractionListener {

    Toolbar mainToolbar;
    FashionTalksApp app;
    ActionBar actionBar;
    int userId;
    final String TAG = "EditProfileActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        userId = getIntent().getIntExtra("PROFILE_ID", 0);
        mainToolbar = (Toolbar)findViewById(R.id.mainToolbar);
        setSupportActionBar(mainToolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        TextView tvName = (TextView) findViewById(R.id.toolbarName);
        tvName.setTypeface(FTUtils.loadFont(getAssets(), getString(R.string.font_avantgarde_bold)));
        tvName.setText(getString(R.string.edit_profile));
        if (savedInstanceState == null) {
            RegisterFragment editProfileFragment = new RegisterFragment();
            Bundle bundle = new Bundle();
            bundle.putBoolean("EDIT_PROFILE", true);
            editProfileFragment.setArguments(bundle);
            FragmentTransaction fragmentTx = getSupportFragmentManager().beginTransaction();
            fragmentTx.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_from_left, R.anim.enter_from_left, R.anim.exit_from_right);
            fragmentTx.add(R.id.container, editProfileFragment)
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
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if(id == android.R.id.home){
            finish();
            BaseActivity.setBackwardsTranslateAnimation(this);
        }

        return true;
    }

    @Override
    public void onBackPressed() {
        finish();
        BaseActivity.setBackwardsTranslateAnimation(this);
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
    public void onFragmentInteraction(String tag) {

    }

    @Override
    public void goToMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        //intent.putExtra("URL", Constants.TERMS_OF_USE_URL);
        startActivity(intent);
        this.finish();
        BaseActivity.setBackwardsTranslateAnimation(this);
    }

    @Override
    public void saveTokens(boolean normalLogin,String... tokens) {

    }

    @Override
    public void setToolbarVisibility(boolean visible) {

    }

    @Override
    public void goRegistrationPage() {

    }

    @Override
    public void setTitleName(String name) {

    }
}
