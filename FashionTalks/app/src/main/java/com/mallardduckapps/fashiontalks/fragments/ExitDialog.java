package com.mallardduckapps.fashiontalks.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

import com.mallardduckapps.fashiontalks.UploadNewStyleActivity;

/**
 * Created by oguzemreozcan on 31/03/15.
 */
public class ExitDialog extends DialogFragment {


    public ExitDialog(){
        //this.fragment = fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
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
                        //EXIT
                        onActivityResult(666, Activity.RESULT_OK, null);
                        getActivity().finish();

                    }
                })
                .setNegativeButton(negButton, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        onActivityResult(665, Activity.RESULT_CANCELED, null);
                    }
                })
                .create();
    }

}
