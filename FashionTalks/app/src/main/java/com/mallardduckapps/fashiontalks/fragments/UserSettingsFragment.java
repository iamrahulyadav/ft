package com.mallardduckapps.fashiontalks.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mallardduckapps.fashiontalks.R;

/**
 * Created by oguzemreozcan on 23/02/15.
 */
public class UserSettingsFragment extends BasicFragment
{
    @Override
    public void setTag() {
        TAG = "USER_SETTINGS_FRAGMENT";
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.fragment_user_settings, container, false);
        return rootView;
    }
}
