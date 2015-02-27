package com.mallardduckapps.fashiontalks;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.mallardduckapps.fashiontalks.fragments.LoginFragment;
import com.mallardduckapps.fashiontalks.fragments.RegisterFragment;
import com.mallardduckapps.fashiontalks.services.RestClient;
import com.mallardduckapps.fashiontalks.utils.Constants;
import com.mallardduckapps.fashiontalks.utils.FTUtils;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends ActionBarActivity{

    private LoginFragment loginFragment;
    private RegisterFragment registerFragment;
    FragmentManager fragmentManager;
    public Toolbar mainToolbar;
    FashionTalksApp app;
    TextView tvName;
    //CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        app = (FashionTalksApp) getApplication();
        fragmentManager = getSupportFragmentManager();
        loginFragment = new LoginFragment();
        registerFragment = new RegisterFragment();
        loginFragment.setActivity(this);
        registerFragment.setActivity(this);
        mainToolbar = (Toolbar)findViewById(R.id.mainToolbar);
        tvName = (TextView) findViewById(R.id.toolbarName);
        tvName.setTypeface(FTUtils.loadFont(getAssets(), getString(R.string.font_avantgarde_bold)));
       // mainToolbar.setTitle("");
        mainToolbar.setVisibility(View.GONE);
        //mTitle = getTitle();
        setSupportActionBar(mainToolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        //actionBar.setDisplayShowTitleEnabled(true);
        goLoginPage();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            //menu.toggle();
        }

        return true;
    }

    //TODO temporary
    public void goRegistrationPage(){
        fragmentManager.beginTransaction()
                .replace(R.id.container, registerFragment)
                .commit();
        tvName.setText(registerFragment.TAG);
        mainToolbar.setVisibility(View.VISIBLE);
        //mainToolbar.setTitle(registerFragment.TAG);
    }

    public void goLoginPage(){
        fragmentManager.beginTransaction()
                .replace(R.id.container, loginFragment)
                .commit();
        tvName.setText(loginFragment.TAG);
        mainToolbar.setVisibility(View.VISIBLE);
        //mainToolbar.setTitle(loginFragment.TAG);
    }

    public void goToMainActivity(){

        Log.d("LOGIN_ACTIVITY", "GOTO MAIN ACTIVITY");
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        this.startActivity(intent);
        this.finish();
    }

    public void saveTokens(String...tokens){
        app.dataSaver.putString(Constants.ACCESS_TOKEN_KEY, tokens[0]);
        app.dataSaver.putString(Constants.REFRESH_TOKEN_KEY, tokens[1]);
        RestClient.setAccessToken(tokens[0]);
        app.dataSaver.save();
    }
}



