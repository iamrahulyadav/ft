package com.mallardduckapps.fashiontalks.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mallardduckapps.fashiontalks.R;
import com.mallardduckapps.fashiontalks.utils.FTUtils;

/**
 * A simple {@link Fragment} subclass.
 */
public class UploadNewStyleMainFragment extends Fragment {


    public UploadNewStyleMainFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.fragment_upload_new_style_main, container, false);

        FTUtils.setFont(rootView, FTUtils.loadFont(getActivity().getAssets(), getString(R.string.font_helvatica_lt)));
        return rootView;
    }


}
