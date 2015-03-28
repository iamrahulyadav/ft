package com.mallardduckapps.fashiontalks.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;


/**
 * Created by oguzemreozcan on 27/03/15.
 */
public class NoConnectionDialog extends DialogFragment {

    public NoConnectionDialog(){

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        Bundle args = getArguments();
        String title = args.getString("title", "");
        String message = args.getString("message", "");

        return new AlertDialog.Builder(getActivity())
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        //getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, null);
                    }
                })
//                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener()
//                {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which)
//                    {
//                        getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_CANCELED, null);
//                    }
//                })
                .create();
    }
}


