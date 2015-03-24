package com.mallardduckapps.fashiontalks.fragments;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mallardduckapps.fashiontalks.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainLoginFragment extends Fragment {


    public MainLoginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main_login, container, false);
    }


}
