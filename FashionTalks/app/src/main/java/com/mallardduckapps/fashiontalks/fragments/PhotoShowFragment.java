package com.mallardduckapps.fashiontalks.fragments;


import android.app.Activity;
import android.app.Fragment;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.mallardduckapps.fashiontalks.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PhotoShowFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PhotoShowFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PhotoShowFragment extends BasicFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private ImageView mainImageView;
    private ImageView icon;
    private Button saveButton;
    private Button cancelButton;
    private RelativeLayout rl;
    int posX = 0;
    int posY = 0;
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PhotoShowFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PhotoShowFragment newInstance(int sectionNumber) {
        PhotoShowFragment fragment = new PhotoShowFragment();
        Bundle args = new Bundle();
        args.putInt("PAGER_SECTION_NUMBER", sectionNumber);
        //args.putString(ARG_PARAM1, param1);
        //args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public PhotoShowFragment() {
        // Required empty public constructor
    }

    @Override
    public void setTag() {
        TAG = "PhotoShowFragment";
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.fragment, container, false);
        rl = (RelativeLayout) rootView.findViewById(R.id.ImageContainerLayout);
        //posX = app.dataSaver.getInt("POSX");
        //posY = app.dataSaver.getInt("POSY");
        mainImageView = (ImageView)rootView.findViewById(R.id.mainImage);
        mainImageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        posX = (int) event.getX();
                        posY = (int) event.getY();
//                            Log.d(TAG, "Screen density: " + context.getResources().getDisplayMetrics().density);
//                            Log.d("POSITION", "POSITION X: " + posX + " - POSITION Y: " + posY);
//                            Log.d("POSITION", "POSITION X DP: " + ArmutUtils.dpFromPx(posX, context) + " - POSITION Y DP: " + ArmutUtils.dpFromPx(posY, context));
                }
                return false;
            }
        });
        Log.d(TAG, "IMAGEVIEW WIDTH: " + mainImageView.getWidth() + " - HEIGHT: " + mainImageView.getHeight());
        saveButton = (Button) rootView.findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                app.dataSaver.putInt("POSX", posX);
                app.dataSaver.putInt("POSY", posY);
                app.dataSaver.save();
                createIcon();

            }
        });
        cancelButton = (Button) rootView.findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                posX = 0;
                posY = 0;
                app.dataSaver.putInt("POSX", 0);
                app.dataSaver.putInt("POSY", 0);
                app.dataSaver.save();
                rl.removeView(icon);
                icon = null;
            }
        });

        createIcon();
        return rootView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (BasicFragment.OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void createIcon(){

        if(posX != 0 && posY != 0){
            if(icon == null){
                placeGlam();

            }else{

            }

        }else{

        }
    }

    public void placeGlam(){
        icon = new ImageView(getActivity());
        icon.setBackgroundColor(getResources().getColor(R.color.blue));
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(30, 40);
        params.leftMargin = posX;
        params.topMargin = posY;
//                params.leftMargin = (int)(92/1.5);
//                params.topMargin = (int)(244/1.5);
//            Log.d(TAG, "Screen density: " + this.getResources().getDisplayMetrics().density);
        //Log.d(TAG, "ImageView width: " + mainImageView.getWidth() + " - ImageView height: " + mainImageView.getHeight());
//            Log.d(TAG, "POSX " + posX + " - POSY: " + posY);

        // Log.d(TAG, "IMAGE LEFT MARGIN DP: " + ArmutUtils.dpFromPx(92, this.getActivity()) + " - TOP MARGIN DP: " + ArmutUtils.dpFromPx(244, this.getActivity()));
        //92 244
        rl.addView(icon, params);

        ViewTreeObserver vto = mainImageView.getViewTreeObserver();
        vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            public boolean onPreDraw() {
                int finalHeight = mainImageView.getHeight();
                int finalWidth = mainImageView.getWidth();
//                    Log.e("IMAGE","Height: " + finalHeight + " Width: " + finalWidth);
//                    Log.d(TAG, "POSX " + posX/1.5 + " - POSY: " + posY/1.5);
                return true;
            }
        });
    }


}
