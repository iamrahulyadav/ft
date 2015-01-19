package com.mallardduckapps.fashiontalks;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.mallardduckapps.fashiontalks.fragments.LoginFragment;
import com.mallardduckapps.fashiontalks.fragments.PlaceHolderFragment;
import com.mallardduckapps.fashiontalks.fragments.RegisterFragment;
import com.mallardduckapps.fashiontalks.objects.User;
import com.mallardduckapps.fashiontalks.tasks.LoginTask;
import com.mallardduckapps.fashiontalks.utils.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends ActionBarActivity{

    private LoginFragment loginFragment;
    private RegisterFragment registerFragment;
    FragmentManager fragmentManager;
    Toolbar mainToolbar;
    //CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        fragmentManager = getSupportFragmentManager();
        loginFragment = new LoginFragment();
        registerFragment = new RegisterFragment();
        loginFragment.setActivity(this);
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
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        this.startActivity(intent);
        this.finish();
    }
}



