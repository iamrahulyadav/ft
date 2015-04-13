package com.mallardduckapps.fashiontalks.fragments;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
import android.provider.ContactsContract;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.mallardduckapps.fashiontalks.R;
import com.mallardduckapps.fashiontalks.services.RestClient;
import com.mallardduckapps.fashiontalks.utils.Constants;
import com.mallardduckapps.fashiontalks.utils.FTUtils;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ForgetPasswordFragment extends BasicFragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private AutoCompleteTextView mEmailView;
    private ProgressBar progressBar;
    boolean disableButton = false;

    public ForgetPasswordFragment() {

    }

    @Override
    public void setTag() {
        TAG = "FORGET_PASSWORD";
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_forget_password, container, false);
        FTUtils.setFont(container, FTUtils.loadFont(getActivity().getAssets(), getString(R.string.font_helvatica_lt)));
        final Button mEmailSignInButton = (Button) rootView.findViewById(R.id.send_password_button);
        mEmailSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!disableButton){
                    hideKeyboard();
                    //TODO
                    attemptSend();
                }

            }
        });
        mEmailView = (AutoCompleteTextView) rootView.findViewById(R.id.email);
        progressBar = (ProgressBar)rootView.findViewById(R.id.progressBar);
        mEmailSignInButton.setTypeface(FTUtils.loadFont(getActivity().getAssets(), getString(R.string.font_helvatica_md)));
        populateAutoComplete();
        Handler handler = new Handler();
        final Runnable r = new Runnable() {
            public void run() {
                Log.d(TAG, "SHOW KEYBOARD");
                showKeyboard(mEmailView);
            }
        };
        handler.postDelayed(r, 300);
        return rootView;
    }

    public void attemptSend(){
        if(!checkEmail()){
            String email = mEmailView.getText().toString().trim();
            ResetPasswordTask task = new ResetPasswordTask(getActivity(),email );
            task.execute();
        }
    }

    private void populateAutoComplete() {
        getLoaderManager().initLoader(0, null, this);
    }

    private void showKeyboard(EditText editText) {
        editText.requestFocus();
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
    }

    private void hideKeyboard() {
        // Check if no view has focus:
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    public boolean checkEmail() {
        boolean cancel = false;
        // Check for a valid email address.
        String email = mEmailView.getText().toString();
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            cancel = true;
        }
        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            //switcher.setDisplayedChild(1);
            //mListener.setToolbarVisibility(true);
            //focusView.requestFocus();
            showKeyboard(mEmailView);
        }

        return cancel;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this.getActivity().getApplicationContext(),
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,
                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<String>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }
        addEmailsToAutoComplete(emails);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };
        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }

    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(getActivity(),
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);
        mEmailView.setAdapter(adapter);
    }

    public class ResetPasswordTask extends AsyncTask<BasicNameValuePair, Void, String> {

        String email;

        public ResetPasswordTask(Context context, String email) {
            this.email = email;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            disableButton = true;
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(BasicNameValuePair... params) {
            String response = "";
            RestClient restClient = new RestClient();
            try {
                response = restClient.doPostRequestWithJSON(Constants.RESET_PASS, null, new BasicNameValuePair("email", email));
                Log.d(TAG, "RESPONSE FROM API: " + response);
            } catch (Exception e) {
                response = "NO_CONNECTION";
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            JSONObject object = null;
            disableButton = false;
            progressBar.setVisibility(View.GONE);
            int status = -1;
            try {
                object = new JSONObject(s);
                status = object.getInt("status");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            if(status == 0 ){
                Handler handler = new Handler();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        app.openOKDialog(ForgetPasswordFragment.this.getActivity(), ForgetPasswordFragment.this, "send_password_success");
                    }
                });
            }else{
                //DIALOG
                Handler handler = new Handler();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        app.openOKDialog(ForgetPasswordFragment.this.getActivity(), ForgetPasswordFragment.this, "send_password_unsuccessful");
                    }
                });
            }
        }
    }

}
