package com.mallardduckapps.fashiontalks.fragments;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.mallardduckapps.fashiontalks.R;
import com.mallardduckapps.fashiontalks.objects.User;
import com.mallardduckapps.fashiontalks.services.RestClient;
import com.mallardduckapps.fashiontalks.tasks.LoginTask;
import com.mallardduckapps.fashiontalks.utils.Constants;
import com.mallardduckapps.fashiontalks.utils.FTUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by oguzemreozcan on 13/01/15.
 */
public class LoginFragment extends BasicFragment implements LoaderManager.LoaderCallbacks<Cursor>, LoginTask.LoginTaskCallback {

    private LoginTask authTask = null;
    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    OnLoginFragmentInteractionListener mListener;
    public ViewSwitcher switcher;
    boolean loggedInBefore = false;
//    private View container;

    @Override
    public void setTag() {
        TAG = "Giriş";
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(app.dataSaver != null){
            String accessToken = app.dataSaver.getString(Constants.ACCESS_TOKEN_KEY);
            Log.d(TAG, "ACCESS TOKEN: " + accessToken);
            if(!accessToken.equals("")){
                if(mListener != null){
                    //showProgress(true);
                    loggedInBefore = true;
                    hideKeyboard();
                    RestClient.setAccessToken(accessToken);
                    LoginTask authTask = new LoginTask(this, getActivity());
                    authTask.execute();
                }
            }else{
                loggedInBefore = false;
            }
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        hideKeyboard();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnLoginFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnLoginFragmentInteractionListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_login, container, false);
        FTUtils.setFont(container, FTUtils.loadFont(getActivity().getAssets(), getString(R.string.font_helvatica_lt)));
        mListener.setTitleName(TAG);
        switcher = (ViewSwitcher) rootView.findViewById(R.id.switcher);
//        if(loggedInBefore){
//            switcher.setDisplayedChild(0);
//            activity.mainToolbar.setVisibility(View.GONE);
//        }else{
            switcher.setDisplayedChild(1);
//            activity.mainToolbar.setVisibility(View.VISIBLE);
//        }
        // Set up the login form.
        mEmailView = (AutoCompleteTextView) rootView.findViewById(R.id.email);
        populateAutoComplete();
        mPasswordView = (EditText) rootView.findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });
//        container = (View)rootView.findViewById(R.id.container);

        final Button mEmailSignInButton = (Button) rootView.findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyboard();
                attemptLogin();
            }
        });
        final Button forgetPasswordButton = (Button) rootView.findViewById(R.id.forgetPassword);
        forgetPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onFragmentInteraction("password");
            }
        });

        mEmailSignInButton.setTypeface(FTUtils.loadFont(getActivity().getAssets(), getString(R.string.font_helvatica_md)));
        forgetPasswordButton.setTypeface(FTUtils.loadFont(getActivity().getAssets(), getString(R.string.font_helvatica_md)));
        mLoginFormView = rootView.findViewById(R.id.login_form);
        mProgressView = rootView.findViewById(R.id.login_progress);
        mListener.setToolbarVisibility(true);
        Handler handler = new Handler();
        final Runnable r = new Runnable() {
            public void run() {
                Log.d(TAG, "SHOW KEYBOARD");
                showKeyboard(mEmailView);
            }
        };
        if(!loggedInBefore)
             handler.postDelayed(r, 300);
        return rootView;
    }

    private void populateAutoComplete() {
        getLoaderManager().initLoader(0, null, this);
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    public void attemptLogin() {
        switcher.setDisplayedChild(0);
        mListener.setToolbarVisibility(false);
        hideKeyboard();
        if (authTask != null) {
            return;
        }
        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);
        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();
        boolean cancel = false;
        EditText focusView = null;
        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            showKeyboard(mPasswordView);
            focusView = mPasswordView;
            cancel = true;
        }
        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            switcher.setDisplayedChild(1);
            mListener.setToolbarVisibility(true);
            //focusView.requestFocus();
            showKeyboard(focusView);

        } else {
            showProgress(true);
            authTask = new LoginTask(getActivity(),this, email, password);
            authTask.execute();
        }
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    private void hideKeyboard() {
        // Check if no view has focus:
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    private void showKeyboard(EditText editText){
        editText.requestFocus();
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
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

    private void showToastMessage(final String text){
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getActivity(), text, Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void getAuthStatus(int authStatus, User user, String... tokens) {
        showProgress(false);
        switch (authStatus) {
            case Constants.WRONG_CREDENTIALS:
                mPasswordView.setError(getString(R.string.error_incorrect_password));
                showKeyboard(mPasswordView);
                switcher.setDisplayedChild(1);
                showToastMessage(getString(R.string.error_incorrect_password));
                mListener.setToolbarVisibility(true);
                break;
            case Constants.AUTHENTICATION_FAILED:

                showToastMessage(getString(R.string.connection_failed));
                mListener.setToolbarVisibility(true);
//                mPasswordView.setError(getString(R.string.error_incorrect_password));
//                showKeyboard(mPasswordView);
//                mPasswordView.requestFocus();
                switcher.setDisplayedChild(1);
                break;
            case Constants.NO_CONNECTION:
                //Toast.makeText(getActivity(), getString(R.string.no_connection), Toast.LENGTH_LONG).show();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d(TAG, "OK DIALOG");
                        app.openOKDialog(getActivity(), LoginFragment.this, "no_connection");
                        switcher.setDisplayedChild(1);
                    }
                });

                break;
            case Constants.AUTHENTICATION_CANCELED:
                authTask = null;
                switcher.setDisplayedChild(1);
                mListener.setToolbarVisibility(true);
                break;
            case Constants.AUTHENTICATION_SUCCESSFUL:
                mListener.saveTokens(true,tokens);
                mListener.goToMainActivity();
                break;
        }
    }

    @Override
    public void getUser(int authStatus, User user) {
        //TODO handle errors
        if(user != null && authStatus == Constants.AUTHENTICATION_SUCCESSFUL){
            app.setMe(user);
            mListener.goToMainActivity();
            //activity.finish();
        }
    }
}