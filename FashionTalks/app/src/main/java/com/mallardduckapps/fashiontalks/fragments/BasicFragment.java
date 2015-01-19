package com.mallardduckapps.fashiontalks.fragments;


import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.mallardduckapps.fashiontalks.FashionTalksApp;

/**
 * Created by oguzemreozcan on 25/12/14.
 */
public abstract class BasicFragment extends Fragment {

    public String TAG;
    FashionTalksApp app;

    public BasicFragment(){
        setTag();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        app = (FashionTalksApp) getActivity().getApplication();
    }

    public abstract void setTag();

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
        public void onToolbarThemeChange(int themeId);
    }
}
