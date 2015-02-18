package com.mallardduckapps.fashiontalks.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mallardduckapps.fashiontalks.FashionTalksApp;
import com.mallardduckapps.fashiontalks.R;
import com.mallardduckapps.fashiontalks.SettingsActivity;
import com.mallardduckapps.fashiontalks.StyleUploadActivity;

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
        View rootView = inflater.inflate(R.layout.fragment_settings_layout, container, false);
        RelativeLayout logoutLayout = (RelativeLayout) rootView.findViewById(R.id.logoutLayout);
        logoutLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //if(actionName.equals(getString(R.string.logout))){
                FashionTalksApp app = (FashionTalksApp)getActivity().getApplication();
                app.dataSaver.remove("ACCESS_TOKEN");

            }
        });

        RelativeLayout styleLayout = (RelativeLayout) rootView.findViewById(R.id.styleUploadLayout);
        styleLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), StyleUploadActivity.class);
                startActivity(intent);
            }
        });
        return rootView;
    }
}

