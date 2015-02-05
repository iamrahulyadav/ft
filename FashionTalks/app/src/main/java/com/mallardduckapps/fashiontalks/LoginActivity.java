package com.mallardduckapps.fashiontalks;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.mallardduckapps.fashiontalks.fragments.LoginFragment;
import com.mallardduckapps.fashiontalks.fragments.RegisterFragment;
import com.mallardduckapps.fashiontalks.services.RestClient;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends ActionBarActivity{

    private LoginFragment loginFragment;
    private RegisterFragment registerFragment;
    FragmentManager fragmentManager;
    Toolbar mainToolbar;
    FashionTalksApp app;
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
        mainToolbar.setTitle("");

        //mTitle = getTitle();
        setSupportActionBar(mainToolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayShowTitleEnabled(true);
        goLoginPage();
    }

    //TODO temporary
    public void goRegistrationPage(){
        fragmentManager.beginTransaction()
                .replace(R.id.container, registerFragment)
                .commit();
        mainToolbar.setTitle(registerFragment.TAG);
    }

    public void goLoginPage(){
        fragmentManager.beginTransaction()
                .replace(R.id.container, loginFragment)
                .commit();
        mainToolbar.setTitle(loginFragment.TAG);
    }

    public void goToMainActivity(){

        Log.d("LOGIN_ACTIVITY", "GOTO MAIN ACTIVITY");
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        this.startActivity(intent);
        this.finish();
    }

    public void saveTokens(String...tokens){
        app.dataSaver.putString("accessToken", tokens[0]);
        app.dataSaver.putString("refreshToken", tokens[1]);
        RestClient.setAccessToken(tokens[0]);
        app.dataSaver.save();
    }
}



