package com.mallardduckapps.fashiontalks.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.mallardduckapps.fashiontalks.R;
import com.mallardduckapps.fashiontalks.utils.FTUtils;

/**
 * Created by oguzemreozcan on 02/08/15.
 */
public class ClassificationDialog extends DialogFragment {

    public final static int CLASSIFICATION_DIALOG = 0;
    public final static int GET_MORE_GLAMS_DIALOG = 1;
    int dialogNo;

    public ClassificationDialog(){
        //this.fragment = fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dialogNo = getArguments().getInt("DIALOG_NO");
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        //Dialog dialog = new Dialog(getActivity(),android.R.style.Theme_Translucent_NoTitleBar);
        View rootView = inflater.inflate(dialogNo == CLASSIFICATION_DIALOG ? R.layout.classification_popup_layout : R.layout.getmoreglam_popup_layout, container, false);
        Dialog dialog = getDialog();
        LinearLayout parentLayout = (LinearLayout) rootView.findViewById(dialogNo == CLASSIFICATION_DIALOG ? R.id.classificationLayout : R.id.getMoreGlamLayout);
        FTUtils.setFont(parentLayout,FTUtils.loadFont(getActivity().getAssets(),getString(R.string.font_helvatica_neue)));

        //FTUtils.setFont(container, FTUtils.loadFont(getActivity().getAssets(), getString(R.string.font_helvatica_thin)));
        // getDialog().setTitle(getResources().getString(R.string.report_subject_title));
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawableResource(R.color.transparent);
        dialog.setCanceledOnTouchOutside(true);
        ImageView closeButton = (ImageView) rootView.findViewById(R.id.closeButton);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClassificationDialog.this.dismiss();
            }
        });


        return rootView;
    }

}
