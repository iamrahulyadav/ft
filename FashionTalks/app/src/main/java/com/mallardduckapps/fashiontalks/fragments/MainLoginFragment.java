package com.mallardduckapps.fashiontalks.fragments;


import android.app.Activity;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.mallardduckapps.fashiontalks.FashionTalksApp;

import com.mallardduckapps.fashiontalks.R;
import com.mallardduckapps.fashiontalks.objects.User;
import com.mallardduckapps.fashiontalks.services.RestClient;
import com.mallardduckapps.fashiontalks.tasks.LoginTask;
import com.mallardduckapps.fashiontalks.utils.Constants;
import com.mallardduckapps.fashiontalks.utils.FTUtils;

/**
 * A simple {@link Fragment} subclass.
 */
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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        app = (FashionTalksApp) getActivity().getApplication();
        if(app.dataSaver != null){
            String accessToken = app.dataSaver.getString(Constants.ACCESS_TOKEN_KEY);
            Log.d(TAG, "ACCESS TOKEN: " + accessToken);
            if(!accessToken.equals("")){
                //if(activity != null){
                loggedInBefore = true;
//                hideKeyboard();
                RestClient.setAccessToken(accessToken);
                LoginTask authTask = new LoginTask(this);
                authTask.execute();
               // }
            }else{
                loggedInBefore = false;
            }
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
    public void getAuthStatus(int authStatus, User user, String... tokens) {
        //showProgress(false);
        switch (authStatus) {
            case Constants.NO_CONNECTION:
                Toast.makeText(getActivity(), getString(R.string.no_connection), Toast.LENGTH_LONG).show();
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
        }
    }
}
