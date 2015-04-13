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
import android.widget.Button;

import com.mallardduckapps.fashiontalks.R;
import com.mallardduckapps.fashiontalks.utils.FTUtils;

/**
 * Created by oguzemreozcan on 08/04/15.
 */
public class ReportDialog extends DialogFragment {

    public ReportDialog(){
        //this.fragment = fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.dialog_fragment_report, container, false);
        //FTUtils.setFont(container, FTUtils.loadFont(getActivity().getAssets(), getString(R.string.font_helvatica_thin)));
       // getDialog().setTitle(getResources().getString(R.string.report_subject_title));
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().setCanceledOnTouchOutside(true);
        Button button1 = (Button) rootView.findViewById(R.id.reportSubject1);
        Button button2 = (Button) rootView.findViewById(R.id.reportSubject2);
        Button button3 = (Button) rootView.findViewById(R.id.reportSubject3);
        Button button4 = (Button) rootView.findViewById(R.id.reportSubject4);
        Button button5 = (Button) rootView.findViewById(R.id.reportSubject5);
        //button1.setTypeface();
        button1.setOnClickListener(clickListener);
        button2.setOnClickListener(clickListener);
        button3.setOnClickListener(clickListener);
        button4.setOnClickListener(clickListener);
        button5.setOnClickListener(clickListener);

        return rootView;
    }

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String subject = ((Button) v).getText().toString();
            FTUtils.sendMail(getString(R.string.email_send_report_content), getString(R.string.email_send_report_recipient), subject, getActivity());
            ReportDialog.this.dismiss();
        }
    };
}
