package com.mallardduckapps.fashiontalks.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.mallardduckapps.fashiontalks.LoginActivity;
import com.mallardduckapps.fashiontalks.R;
import com.mallardduckapps.fashiontalks.objects.User;
import com.mallardduckapps.fashiontalks.tasks.RegisterTask;
import com.mallardduckapps.fashiontalks.utils.Constants;

import org.apache.http.message.BasicNameValuePair;

/**
 * Created by oguzemreozcan on 13/01/15.
 */
public class RegisterFragment extends BasicFragment implements RegisterTask.RegisterTaskCallback {

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
//    public static RegisterFragment newInstance() {
//        return new RegisterFragment();
//    }

    private Button registerButton;
    private EditText userNameEdit;
    private EditText emailEdit;
    private EditText passwordEdit;
    private EditText passwordAgainEdit;
    private EditText firstNameEdit;
    private EditText lastNameEdit;
    private EditText birthDateEdit; //Optional
    private EditText countryEdit;
    private EditText cityEdit;
    private EditText aboutEdit;
    Button genderMale;
    Button genderFemale;
    boolean isMale;
    View focusView = null;
    LoginActivity activity;

    public RegisterFragment() {
    }

    public void setActivity(LoginActivity activity){
        this.activity = activity;
    }

    @Override
    public void setTag() {
        TAG = "RegisterFragment";
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.register_layout, container, false);
        registerButton = (Button) rootView.findViewById(R.id.registerButton);
        userNameEdit = (EditText) rootView.findViewById(R.id.userName);
        emailEdit = (EditText) rootView.findViewById(R.id.email);
        passwordEdit = (EditText) rootView.findViewById(R.id.password);
        passwordAgainEdit = (EditText) rootView.findViewById(R.id.passwordAgain);
        firstNameEdit = (EditText) rootView.findViewById(R.id.firstName);
        lastNameEdit = (EditText) rootView.findViewById(R.id.lastName);
        birthDateEdit = (EditText) rootView.findViewById(R.id.birthDate);
        countryEdit = (EditText) rootView.findViewById(R.id.country);
        cityEdit = (EditText) rootView.findViewById(R.id.city);
        aboutEdit = (EditText) rootView.findViewById(R.id.about);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (controlFields()) {

                } else {
                    if (focusView != null) {
                        focusView.requestFocus();
                    }
                }
            }
        });

        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        //((LoginActivity) activity).onSectionAttached();
    }

    public boolean controlFields() {

        String userName = userNameEdit.getText().toString();
        String email = emailEdit.getText().toString();
        String password = passwordEdit.getText().toString();
        String passwordRepeat = passwordAgainEdit.getText().toString();
        String firstName = firstNameEdit.getText().toString();
        String lastName = lastNameEdit.getText().toString();
        String birthDate = birthDateEdit.getText().toString();
        String country = countryEdit.getText().toString();
        String city = cityEdit.getText().toString();
        String about = aboutEdit.getText().toString();


        if (!TextUtils.isEmpty(userName) && !TextUtils.isEmpty(email)
                && !TextUtils.isEmpty(password) && !TextUtils.isEmpty(passwordRepeat)
                && !TextUtils.isEmpty(firstName) && !TextUtils.isEmpty(lastName)) {
            if (!email.contains("@")) {
                focusView = emailEdit;
                return false;
            }
            if (!password.equals(passwordRepeat) || password.length() < 4) {
                focusView = passwordEdit;
                return false;
            }
            RegisterTask task = new RegisterTask(this);
            task.execute(new BasicNameValuePair("username", userName),
                    new BasicNameValuePair("email", email),
                    new BasicNameValuePair("password", password),
                    new BasicNameValuePair("birth_date", birthDate),
                    new BasicNameValuePair("first_name", firstName),
                    new BasicNameValuePair("last_name", lastName),
                    new BasicNameValuePair("gender", isMale ? "M" : "F"),
                    new BasicNameValuePair("client_id", Constants.CLIENT_ID),
                    new BasicNameValuePair("client_secret", Constants.CLIENT_SECRET));

            //TODO Control Other fields
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void getAuthStatus(int authStatus, User user, String... tokens) {
       // showProgress(false);
        switch (authStatus) {
            case Constants.WRONG_CREDENTIALS:
                //mPasswordView.setError(getString(R.string.error_incorrect_password));
                //mPasswordView.requestFocus();
                break;
            case Constants.AUTHENTICATION_FAILED:
                //mPasswordView.setError(getString(R.string.error_incorrect_password));
                //mPasswordView.requestFocus();
                break;
            case Constants.AUTHENTICATION_CANCELED:
                //authTask = null;
                break;
            case Constants.AUTHENTICATION_SUCCESSFUL:
                activity.saveTokens(tokens);
                activity.goToMainActivity();

                break;
        }
    }
}
