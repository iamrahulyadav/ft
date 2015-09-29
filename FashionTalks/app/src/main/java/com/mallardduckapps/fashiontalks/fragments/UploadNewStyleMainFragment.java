package com.mallardduckapps.fashiontalks.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mallardduckapps.fashiontalks.R;
import com.mallardduckapps.fashiontalks.utils.FTUtils;

/**
 * A simple {@link Fragment} subclass.
 */
public class UploadNewStyleMainFragment extends BasicFragment {

    //private Uri mImageUri;
    OnFragmentInteractionListener mListener;
//    public static final Uri CONTENT_URI = Uri.parse("content://eu.janmuller.android.simplecropimage.example/");
//    public static final String TEMP_PHOTO_FILE_NAME = "temp_photo.jpg";

    public UploadNewStyleMainFragment() {
    }

    @Override
    public void setTag() {
        TAG = "UPLOAD_NEW_STYLE_FRAGMENT";
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.fragment_upload_new_style_main, container, false);
        TextView choiceTv = (TextView) rootView.findViewById(R.id.choosePicTv);
        TextView shootTv = (TextView) rootView.findViewById(R.id.takePicTv);

//        String state = Environment.getExternalStorageState();
//        if (Environment.MEDIA_MOUNTED.equals(state)) {
//            UploadNewStyleActivity.mFileTemp = new File(Environment.getExternalStorageDirectory(), TEMP_PHOTO_FILE_NAME);
//        }
//        else {
//            UploadNewStyleActivity.mFileTemp = new File(getActivity().getFilesDir(), TEMP_PHOTO_FILE_NAME);
//        }

        choiceTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onClickGallery();//openGallery();
            }
        });

        shootTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onClickTakePicture();//takePicture();
            }
        });
        mListener.setBackButton(false);
        FTUtils.setFont(rootView, FTUtils.loadFont(getActivity().getAssets(), getString(R.string.font_helvatica_lt)));
        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //super.onCreateOptionsMenu(menu,inflater);
        inflater.inflate(R.menu.menu_settings, menu);
    }

    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

//    public void openGallery() {
//        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
//        photoPickerIntent.setType("image/*");
//        getActivity().startActivityForResult(photoPickerIntent, UploadNewStyleActivity.REQ_PICK_IMAGE);
//    }
//
//    public void takePicture() {
//        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        try {
//            Uri mImageCaptureUri = null;
//            String state = Environment.getExternalStorageState();
//            if (Environment.MEDIA_MOUNTED.equals(state)) {
//                mImageCaptureUri = Uri.fromFile(UploadNewStyleActivity.mFileTemp);
//            }
//            else {
//	        	/*
//	        	 * The solution is taken from here: http://stackoverflow.com/questions/10042695/how-to-get-camera-result-as-a-uri-in-data-folder
//	        	 */
//                mImageCaptureUri = CONTENT_URI;
//            }
//            intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
//            intent.putExtra("return-data", true);
//            getActivity().startActivityForResult(intent, UploadNewStyleActivity.REQ_CAMERA);
//        } catch (ActivityNotFoundException e) {
//
//            Log.d(TAG, "cannot take picture", e);
//        }
//    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(String tag, String value);
        void setBackButton(boolean backButton);
        void onClickGallery();
        void onClickTakePicture();
    }

}
