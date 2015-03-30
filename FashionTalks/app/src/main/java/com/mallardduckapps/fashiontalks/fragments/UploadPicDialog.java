package com.mallardduckapps.fashiontalks.fragments;

/**
 * Created by oguzemreozcan on 29/03/15.
 */

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

import com.mallardduckapps.fashiontalks.UploadNewStyleActivity;

public class UploadPicDialog extends DialogFragment {

    BasicFragment fragment;

    public UploadPicDialog(){
        //this.fragment = fragment;
    }

    public void setTargetFragment(BasicFragment fragment){
        this.fragment = fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        Bundle args = getArguments();
        String title = args.getString("title", "");
        String message = args.getString("message", "");
        String posButton = args.getString("positive_button", "");
        String negButton = args.getString("negative_button", "");

        return new AlertDialog.Builder(getActivity())
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(posButton, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        fragment.onActivityResult( UploadNewStyleActivity.REQ_CAMERA_CALL,99, null);

                    }
                })
                .setNegativeButton(negButton, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        fragment.onActivityResult(UploadNewStyleActivity.REQ_PICK_IMAGE_CALL,99, null);
                    }
                })
                .create();
    }
}



