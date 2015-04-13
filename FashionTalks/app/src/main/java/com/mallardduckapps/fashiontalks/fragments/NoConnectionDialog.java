package com.mallardduckapps.fashiontalks.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;


/**
 * Created by oguzemreozcan on 27/03/15.
 */
public class NoConnectionDialog extends DialogFragment {
    Fragment targetFragment;
    int requestCode;

    public NoConnectionDialog() {

    }

    public void setTargetFragment(Fragment fragment, int requestCode) {
        targetFragment = fragment;
        this.requestCode = requestCode;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle args = getArguments();
        String title = args.getString("title", "");
        String message = args.getString("message", "");

        return new AlertDialog.Builder(getActivity())
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (targetFragment != null) {
                            targetFragment.onActivityResult(requestCode, 1, null);
                        }
                    }
                })
                .create();
    }
}


