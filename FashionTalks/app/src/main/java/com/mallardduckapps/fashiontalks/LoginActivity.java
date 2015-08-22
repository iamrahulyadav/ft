package com.mallardduckapps.fashiontalks;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.facebook.appevents.AppEventsLogger;
import com.mallardduckapps.fashiontalks.fragments.BasicFragment;
import com.mallardduckapps.fashiontalks.fragments.ForgetPasswordFragment;
import com.mallardduckapps.fashiontalks.fragments.LoginFragment;
import com.mallardduckapps.fashiontalks.fragments.MainLoginFragment;
import com.mallardduckapps.fashiontalks.fragments.RegisterFragment;
import com.mallardduckapps.fashiontalks.services.RestClient;
import com.mallardduckapps.fashiontalks.utils.Constants;
import com.mallardduckapps.fashiontalks.utils.FTUtils;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements BasicFragment.OnLoginFragmentInteractionListener{

    //private LoginFragment loginFragment;
    //private RegisterFragment registerFragment;
    //private MainLoginFragment mainLoginFragment;
    //private ForgetPasswordFragment forgetPasswordFragment;
    FragmentManager fragmentManager;
    public Toolbar mainToolbar;
    FashionTalksApp app;
    TextView tvName;
    //DIRECTION is used for notification forwarding
    String direction = null;
    //CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        app = (FashionTalksApp) getApplication();
        fragmentManager = getSupportFragmentManager();
        //mainLoginFragment = new MainLoginFragment();
        //forgetPasswordFragment = new ForgetPasswordFragment();
        Bundle extras = getIntent().getExtras();

        if(extras != null){
            direction = extras.getString("DIRECTION");
            Log.d("LOGIN ACT", "INTENT" + direction);
        }
        //Log.d("LOGIN ACT", "DIRECTION: " + app.direction);
       // LoginButton loginButton = (LoginButton) view.findViewById(R.id.usersettings_fragment_login_button);
        //loginButton.registerCallback(callbackManager, new LoginButton.Callback() {  });
       // registerFragment.setActivity(this);
        mainToolbar = (Toolbar)findViewById(R.id.mainToolbar);
        tvName = (TextView) findViewById(R.id.toolbarName);
        tvName.setTypeface(FTUtils.loadFont(getAssets(), getString(R.string.font_avantgarde_bold)));
       // mainToolbar.setTag("");
        mainToolbar.setVisibility(View.GONE);
        //mTitle = getTag();
        setSupportActionBar(mainToolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        //actionBar.setDisplayShowTitleEnabled(true);
        //goLoginPage();
        goMainLoginPage();
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
    protected void onNewIntent(Intent intent) {
        //super.onNewIntent(intent);
        Log.d("LOGIN ACT", "ON new intent: " + intent.toString());
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
            onBackPressed();
        }
        return true;
    }

    private void hideKeyboard() {
        // Check if no view has focus:
        View view = getCurrentFocus();
        if (view != null) {
            InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    @Override
    public void onBackPressed() {
        hideKeyboard();
            if (getSupportFragmentManager().getBackStackEntryCount() > 0 ){
                getSupportFragmentManager().popBackStack();
            } else {
                finish();
            }
    }

    //TODO temporary
    public void goRegistrationPage(){
        RegisterFragment registerFragment = new RegisterFragment();
        FragmentTransaction fragmentTx = getSupportFragmentManager().beginTransaction();
        Bundle bundle = new Bundle();
        bundle.putBoolean("EDIT_PROFILE", false);
        registerFragment.setArguments(bundle);
        fragmentTx.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_from_left, R.anim.enter_from_left, R.anim.exit_from_right);
        fragmentTx.replace(R.id.container, registerFragment).addToBackStack(registerFragment.TAG)
                .commit();
        tvName.setText(getString(R.string.signup));
        mainToolbar.setVisibility(View.VISIBLE);
        //mainToolbar.setTag(registerFragment.TAG);
    }

    @Override
    public void setTitleName(String name) {
        tvName.setText(name);
    }

    public void goLoginPage(){
        FragmentTransaction fragmentTx = getSupportFragmentManager().beginTransaction();
        LoginFragment loginFragment = new LoginFragment();
        fragmentTx.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_from_left, R.anim.enter_from_left, R.anim.exit_from_right);
        fragmentTx.replace(R.id.container, loginFragment).addToBackStack(loginFragment.TAG)
                .commit();
        tvName.setText(getString(R.string.login_page));
        mainToolbar.setVisibility(View.VISIBLE);
        //mainToolbar.setTag(loginFragment.TAG);
    }

    public void goMainLoginPage(){
        MainLoginFragment mainLoginFragment = new MainLoginFragment();
        FragmentTransaction fragmentTx = getSupportFragmentManager().beginTransaction();
        fragmentTx.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_from_left, R.anim.enter_from_left, R.anim.exit_from_right);
        fragmentTx.replace(R.id.container, mainLoginFragment)
                .commit();
        //tvName.setText(loginFragment.TAG);
        mainToolbar.setVisibility(View.GONE);
        //mainToolbar.setTag(loginFragment.TAG);
    }

    public void goToResetPasswordPage(){
        ForgetPasswordFragment forgetPasswordFragment = new ForgetPasswordFragment();
        FragmentTransaction fragmentTx = getSupportFragmentManager().beginTransaction();
        fragmentTx.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_from_left, R.anim.enter_from_left, R.anim.exit_from_right);
        fragmentTx.replace(R.id.container, forgetPasswordFragment ).addToBackStack(forgetPasswordFragment.TAG)
                .commit();
        tvName.setText(getString(R.string.forget_password));
        //tvName.setText(loginFragment.TAG);
        mainToolbar.setVisibility(View.VISIBLE);
    }

    public void goToMainActivity(){
        Log.d("LOGIN_ACTIVITY", "GOTO MAIN ACTIVITY");

        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        if(direction != null)//app.direction != null
        if(direction.equals("NOTIFICATION")){
            Bundle bundle = new Bundle();
            bundle.putString("DIRECTION", "NOTIFICATION");
            intent.putExtras(bundle);
           // Log.d("LOGIN_ACTIVITY", "GOTO NOTIFICATION ACTIVITY" + app.direction);
            //intent = new Intent(LoginActivity.this, NotificationActivity.class);
        }
        //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        this.startActivity(intent);
        this.finish();
        BaseActivity.setTranslateAnimation(this);
    }

    public void saveTokens(boolean normalLogin, String...tokens){
        app.dataSaver.putString(Constants.ACCESS_TOKEN_KEY, tokens[0]);
        app.dataSaver.putString(Constants.REFRESH_TOKEN_KEY, tokens[1]);
        if(!normalLogin){
            app.dataSaver.putString(Constants.FB_ACCESS_TOKEN_KEY, tokens[0]);
        }
        RestClient.setAccessToken(tokens[0]);
        app.dataSaver.save();
    }

    @Override
    public void setToolbarVisibility(boolean visible) {
        if(visible){
            mainToolbar.setVisibility(View.VISIBLE);
        }else{
            mainToolbar.setVisibility(View.GONE);
        }
    }

    @Override
    public void onFragmentInteraction(String tag) {
        if(tag.equals("Login")){
            goLoginPage();
        }else if(tag.equals("Register")){
            goRegistrationPage();
        }else if(tag.equals("FBLogin")){

        }else if(tag.equals("password")){
            goToResetPasswordPage();
        }
    }


}



