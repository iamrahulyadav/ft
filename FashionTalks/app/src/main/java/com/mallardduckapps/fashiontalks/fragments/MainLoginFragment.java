package com.mallardduckapps.fashiontalks.fragments;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;

import android.support.annotation.Nullable;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.mallardduckapps.fashiontalks.FashionTalksApp;

import com.mallardduckapps.fashiontalks.R;
import com.mallardduckapps.fashiontalks.objects.User;
import com.mallardduckapps.fashiontalks.services.RestClient;
import com.mallardduckapps.fashiontalks.tasks.ConnectFBTask;
import com.mallardduckapps.fashiontalks.tasks.LoginFBTask;
import com.mallardduckapps.fashiontalks.tasks.LoginTask;
import com.mallardduckapps.fashiontalks.utils.Constants;
import com.mallardduckapps.fashiontalks.utils.FTUtils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;


public class MainLoginFragment extends BasicFragment implements LoginTask.LoginTaskCallback{

    TextView loginWithFbTv;
    TextView loginwithFbSubtitleTv;
    TextView loginWithEmailTv;
    TextView signUpTv;
    TextView termsAndPrivacyTv;
    FashionTalksApp app;
    ProgressBar progressBar;
    ViewSwitcher switcher;
    OnLoginFragmentInteractionListener mListener;
    boolean loggedInBefore = false;
    CallbackManager callbackManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        app = (FashionTalksApp) getActivity().getApplication();
        FacebookSdk.sdkInitialize(getActivity().getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        //showHashKey(getActivity());
        //TODO internet yokken back yapınca switcher geliyor
        if(app.dataSaver != null){
            String accessToken = app.dataSaver.getString(Constants.ACCESS_TOKEN_KEY);
            Log.d(TAG, "ACCESS TOKEN: " + accessToken);
            if(!accessToken.equals("")){
                //if(activity != null){
                loggedInBefore = true;
//                hideKeyboard();
                RestClient.setAccessToken(accessToken);
                LoginTask authTask = new LoginTask(this, getActivity());
                authTask.execute();
               // }
            }else{
                loggedInBefore = false;
            }
        }
    }

    public static void showHashKey(Context context) {
        //TODO
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.i("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.d("Exception", "NAME NOT FOUND EXCEPTION KEYHASH");
        } catch (NoSuchAlgorithmException e) {
            Log.d("EXception", "No such algorith EXCEPTION KEYHASH");
        }
    }

    @Override
    public void setTag() {
        TAG = "MainLogin";
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
             mListener = (OnLoginFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_main_login, container, false);
        final LoginButton loginButton = (LoginButton) rootView.findViewById(R.id.login_button);
        ArrayList<String> permissions = new ArrayList<>();
        permissions.add("user_friends");
        permissions.add("public_profile");
        permissions.add("email");
        permissions.add("user_birthday");
        permissions.add("user_location");

        loginButton.setReadPermissions(permissions);

        mListener.setToolbarVisibility(false);
        // If using in a fragment
        loginButton.setFragment(this);
        // Other app specific specialization
        // Callback registration
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
                String accessToken = loginResult.getAccessToken().getToken();
                Log.d(TAG, "FB ON SUCCESS + " + accessToken);
                LoginFBTask task = new LoginFBTask(getActivity(), MainLoginFragment.this, accessToken);
                //ConnectFBTask task = new ConnectFBTask(accessToken, true);
                task.execute();
            }

            @Override
            public void onCancel() {
                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
                Log.d(TAG, "FB ON ERROR");

            }
        });
        switcher = (ViewSwitcher) rootView.findViewById(R.id.switcher);
        if(loggedInBefore){
            switcher.setDisplayedChild(0);
            //activity.mainToolbar.setVisibility(View.GONE);
        }else{
            switcher.setDisplayedChild(1);
           // activity.mainToolbar.setVisibility(View.VISIBLE);
        }
        loginWithFbTv = (TextView) rootView.findViewById(R.id.loginWithFB);
        loginwithFbSubtitleTv = (TextView) rootView.findViewById(R.id.loginWithFBSubtitle);
        signUpTv = (TextView) rootView.findViewById(R.id.signUp);
        loginWithEmailTv = (TextView) rootView.findViewById(R.id.loginWithEmail);
        termsAndPrivacyTv = (TextView) rootView.findViewById(R.id.termsAndPrivacy);
        Activity activity = getActivity();
        loginWithFbTv.setTypeface(FTUtils.loadFont(activity.getAssets(), activity.getString(R.string.font_helvatica_thin)));
        loginwithFbSubtitleTv.setTypeface(FTUtils.loadFont(activity.getAssets(),activity.getString(R.string.font_helvatica_thin)));
        signUpTv.setTypeface(FTUtils.loadFont(activity.getAssets(),activity.getString(R.string.font_helvatica_bold)));
        loginWithEmailTv.setTypeface(FTUtils.loadFont(activity.getAssets(),activity.getString(R.string.font_helvatica_thin)));
        termsAndPrivacyTv.setTypeface(FTUtils.loadFont(activity.getAssets(),activity.getString(R.string.font_helvatica_thin)));

        termsAndPrivacyTv.setMovementMethod(LinkMovementMethod.getInstance());
        termsAndPrivacyTv.setText(Html.fromHtml(getResources().getString(R.string.terms_privacy_warning)));

        loginWithEmailTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onFragmentInteraction("Login");
            }
        });

        loginWithFbTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onFragmentInteraction("FBLogin");

                loginButton.performClick();
            }
        });

        signUpTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onFragmentInteraction("Register");
            }
        });

        return rootView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        if(requestCode == Constants.NO_CONNECTION && resultCode == 1){
            getActivity().finish();
        }
    }

    @Override
    public void getAuthStatus(int authStatus, User user, String... tokens) {
        //showProgress(false);
        switch (authStatus) {
            case Constants.NO_CONNECTION:
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d(TAG, "OK DIALOG");
                        //Toast.makeText(getActivity(), getString(R.string.no_connection), Toast.LENGTH_LONG).show();
                        app.openOKDialog(getActivity(), MainLoginFragment.this, "no_connection");
                        switcher.setDisplayedChild(1);
                    }
                });

                break;
            case Constants.AUTHENTICATION_CANCELED:
                //authTask = null;
                switcher.setDisplayedChild(1);
                break;
            case Constants.AUTHENTICATION_SUCCESSFUL:
                mListener.saveTokens(tokens);
                mListener.goToMainActivity();
                break;
        }
    }

    @Override
    public void getUser(int authStatus, User user) {

        //TODO handle errors
        if(user != null && authStatus == Constants.AUTHENTICATION_SUCCESSFUL){
            app.setMe(user);
            mListener.goToMainActivity();
            //activity.finish();
        }else{
            app.openOKDialog(getActivity(), MainLoginFragment.this, "no_connection");
            switcher.setDisplayedChild(1);
        }
    }
}
