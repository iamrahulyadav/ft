package com.mallardduckapps.fashiontalks.fragments;


import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.mallardduckapps.fashiontalks.BaseActivity;
import com.mallardduckapps.fashiontalks.FashionTalksApp;
import com.mallardduckapps.fashiontalks.LoginActivity;
import com.mallardduckapps.fashiontalks.R;
import com.mallardduckapps.fashiontalks.SendPostingCodeActivity;
import com.mallardduckapps.fashiontalks.StyleUploadActivity;
import com.mallardduckapps.fashiontalks.UserSettingsActivity;
import com.mallardduckapps.fashiontalks.WebActivity;
import com.mallardduckapps.fashiontalks.utils.Constants;
import com.mallardduckapps.fashiontalks.utils.FTUtils;

/**
 * A simple {@link Fragment} subclass.
 */

public class SettingsFragment extends android.support.v4.app.Fragment {

    public SettingsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.fragment_settings_layout, container, false);

        FTUtils.setFont(rootView, FTUtils.loadFont(getActivity().getAssets(), getString(R.string.font_helvatica_lt)));

        RelativeLayout userSettingsLayout = (RelativeLayout) rootView.findViewById(R.id.userSettingsLayout);
        userSettingsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), UserSettingsActivity.class);
                startActivity(intent);
                BaseActivity.setTranslateAnimation(getActivity());
            }
        });

        RelativeLayout logoutLayout = (RelativeLayout) rootView.findViewById(R.id.logoutLayout);
        logoutLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //if(actionName.equals(getString(R.string.logout))){
                FashionTalksApp app = (FashionTalksApp)getActivity().getApplication();
                app.dataSaver.putString(Constants.ACCESS_TOKEN_KEY, "");
                app.dataSaver.putString("REGISTRATION_ID", "");
                app.dataSaver.save();
                app.flushAllData();
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                getActivity().finish();
                BaseActivity.setTranslateAnimation(getActivity());

            }
        });

        RelativeLayout styleLayout = (RelativeLayout) rootView.findViewById(R.id.styleUploadLayout);
        styleLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), StyleUploadActivity.class);
                startActivity(intent);
                BaseActivity.setTranslateAnimation(getActivity());
            }
        });

        RelativeLayout sendPostingCodeLayout = (RelativeLayout) rootView.findViewById(R.id.sendPostingCodeLayout);
        sendPostingCodeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SendPostingCodeActivity.class);
                startActivity(intent);
                BaseActivity.setTranslateAnimation(getActivity());
            }
        });

        RelativeLayout privacyLayout = (RelativeLayout) rootView.findViewById(R.id.privacyLayout);
        privacyLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), WebActivity.class);
                intent.putExtra("URL", Constants.PRIVACY_URL);
                startActivity(intent);
                BaseActivity.setTranslateAnimation(getActivity());
            }
        });

        RelativeLayout termsOfServiceLayout = (RelativeLayout) rootView.findViewById(R.id.termsOfServiceLayout);
        termsOfServiceLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), WebActivity.class);
                intent.putExtra("URL", Constants.TERMS_OF_USE_URL);
                startActivity(intent);
                BaseActivity.setTranslateAnimation(getActivity());
            }
        });

        RelativeLayout contactLayout = (RelativeLayout) rootView.findViewById(R.id.contactLayout);
        contactLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FTUtils.sendMail("Email Template", getString(R.string.email_send_communication_recipient), getString(R.string.email_send_communication_subject), getActivity());
                BaseActivity.setTranslateAnimation(getActivity());
            }
        });
        return rootView;
    }




}

