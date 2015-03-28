package com.mallardduckapps.fashiontalks.fragments;


import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mallardduckapps.fashiontalks.R;
import com.mallardduckapps.fashiontalks.UploadNewStyleActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class UploadNewStyleTitleFragment extends BasicFragment {

    RelativeLayout layout;
    RelativeLayout bottomBar;
    String imagePath;
    Uri mImageUri;
    UploadNewStyleMainFragment.OnFragmentInteractionListener mListener;
    EditText titleEditText;

    public UploadNewStyleTitleFragment() {
        // Required empty public constructor
    }

    @Override
    public void setTag() {
        TAG = "UPLOAD_NEW_STYLE_TITLE_FRAGMENT";
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true);
        View rootView = inflater.inflate(R.layout.fragment_upload_new_style_title, container, false);
        layout = (RelativeLayout) rootView.findViewById(R.id.mainPostLayout);
        bottomBar = (RelativeLayout) rootView.findViewById(R.id.bottomBar);
        titleEditText = (EditText) rootView.findViewById(R.id.titleEditText);
        final ImageView postPhoto = (ImageView)rootView.findViewById(R.id.postImage);
        postPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        TextView takePicTv = (TextView)rootView.findViewById(R.id.takePicTv);
        TextView selectPicTv = (TextView)rootView.findViewById(R.id.choosePicTv);

        takePicTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onClickTakePicture();
            }
        });

        selectPicTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onClickGallery();
            }
        });

        imagePath = getArguments().getString("IMAGE_PATH");
        mImageUri = Uri.parse(imagePath);

        //Log.d(TAG, "IMAGE PATH FILE CONVERTED " + UploadNewStyleActivity.mFileTemp.getPath());
        Log.d(TAG, "IMAGE PATH URI CONVERTED " + mImageUri);

        Bitmap bitmap = BitmapFactory.decodeFile(mImageUri.getPath());
        postPhoto.setImageBitmap(bitmap);
        //grabImage(postPhoto);
        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //super.onCreateOptionsMenu(menu,inflater);
        inflater.inflate(R.menu.menu_continue, menu);
        menu.removeItem(R.id.action_null);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //super.onOptionsItemSelected(item);
        if(item.getItemId() == R.id.action_continue){
            Log.d(TAG, "CONTINUE");
            //title = titleEditText.getText().toString();
            hideKeyboard();
            mListener.onFragmentInteraction("CONTINUE", titleEditText.getText().toString());
        }
        return true;
    }

    private void hideKeyboard() {
        // Check if no view has focus:
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (UploadNewStyleMainFragment.OnFragmentInteractionListener) activity;
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
}
