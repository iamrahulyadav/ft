package com.mallardduckapps.fashiontalks.fragments;

/**
 * Created by oguzemreozcan on 10/01/15.
 */

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mallardduckapps.fashiontalks.MainActivity;
import com.mallardduckapps.fashiontalks.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class PlaceHolderFragment extends BasicFragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static PlaceHolderFragment newInstance(int sectionNumber) {
        PlaceHolderFragment fragment = new PlaceHolderFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public PlaceHolderFragment() {
    }

    @Override
    public void setTag() {
        TAG = "PLACEHOLDER_FRAGMENT_" + ARG_SECTION_NUMBER;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
       // ((MainActivity) activity).onSectionAttached(
        //        getArguments().getInt(ARG_SECTION_NUMBER));
    }
}
