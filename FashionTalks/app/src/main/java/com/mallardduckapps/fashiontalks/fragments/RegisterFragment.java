package com.mallardduckapps.fashiontalks.fragments;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.makeramen.RoundedImageView;
import com.mallardduckapps.fashiontalks.BaseActivity;
import com.mallardduckapps.fashiontalks.FashionTalksApp;
import com.mallardduckapps.fashiontalks.LoginActivity;
import com.mallardduckapps.fashiontalks.R;
import com.mallardduckapps.fashiontalks.objects.User;
import com.mallardduckapps.fashiontalks.tasks.RegisterTask;
import com.mallardduckapps.fashiontalks.utils.Constants;
import com.mallardduckapps.fashiontalks.utils.FTUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.apache.http.message.BasicNameValuePair;

/**
 * Created by oguzemreozcan on 13/01/15.
 */
public class RegisterFragment extends BasicFragment implements RegisterTask.RegisterTaskCallback {

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
    RoundedImageView profilePic;
    Button genderMale;
    Button genderFemale;
    boolean isMale;
    View focusView = null;
    LoginActivity activity;
    boolean isEditProfile;
    FashionTalksApp app;

    public RegisterFragment() {
    }

    public void setActivity(LoginActivity activity){
        this.activity = activity;
    }

    @Override
    public void setTag() {
        TAG = "Register";
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.register_layout, container, false);
        FTUtils.setFont(container,FTUtils.loadFont(getActivity().getAssets(), getString(R.string.font_helvatica_thin)));
        isEditProfile = getArguments().getBoolean("EDIT_PROFILE", false);
        profilePic = (RoundedImageView) rootView.findViewById(R.id.profileThumbnail);
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
        genderMale = (Button) rootView.findViewById(R.id.maleButton);
        genderFemale = (Button) rootView.findViewById(R.id.femaleButton);
        genderMale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isEditProfile){
                    return;
                }
                isMale = true;
                genderMale.setTypeface(genderMale.getTypeface(), Typeface.BOLD);
                genderFemale.setTypeface(genderFemale.getTypeface(), Typeface.NORMAL);
            }
        });
        genderFemale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isEditProfile){
                    return;
                }
                isMale = false;
                genderMale.setTypeface(genderMale.getTypeface(), Typeface.NORMAL);
                genderFemale.setTypeface(genderFemale.getTypeface(), Typeface.BOLD);
            }
        });

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
        app = (FashionTalksApp)getActivity().getApplication();
        if(isEditProfile){
            registerButton.setText("Kaydet");
            fillValues();
        }
        registerButton.setTypeface(FTUtils.loadFont(getActivity().getAssets(), getString(R.string.font_helvatica_md)));

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
                && !TextUtils.isEmpty(firstName) && !TextUtils.isEmpty(lastName) && !isEditProfile) {
            if (!email.contains("@")) {
                focusView = emailEdit;
                return false;
            }

                if (!password.equals(passwordRepeat) || password.length() < 4) {
                    focusView = passwordEdit;
                    return false;
                }

            RegisterTask task = new RegisterTask(this, false);

                task.execute(new BasicNameValuePair("username", userName),
                        new BasicNameValuePair("email", email),
                        new BasicNameValuePair("password", password),
                        new BasicNameValuePair("birth_date", birthDate),
                        new BasicNameValuePair("first_name", firstName),
                        new BasicNameValuePair("last_name", lastName),
                        new BasicNameValuePair("gender", isMale ? "M" : "F"),
                        new BasicNameValuePair("country", country),
                        new BasicNameValuePair("city", city),
                        new BasicNameValuePair("about", about),
                        new BasicNameValuePair("client_id", Constants.CLIENT_ID),
                        new BasicNameValuePair("client_secret", Constants.CLIENT_SECRET));

            //TODO Control Other fields
            return true;
        } else if(isEditProfile &&!TextUtils.isEmpty(firstName) && !TextUtils.isEmpty(lastName) && !TextUtils.isEmpty(userName)){
            RegisterTask task = new RegisterTask(this, true);
            task.execute(new BasicNameValuePair("username", userName),
                    //new BasicNameValuePair("password", password),
                    new BasicNameValuePair("birth_date", birthDate),
                    new BasicNameValuePair("first_name", firstName),
                    new BasicNameValuePair("last_name", lastName),
                    new BasicNameValuePair("country", country),
                    new BasicNameValuePair("city", city),
                    new BasicNameValuePair("about", about));
            //new BasicNameValuePair("gender", isMale ? "M" : "F"),
            //new BasicNameValuePair("client_id", Constants.CLIENT_ID),
            //new BasicNameValuePair("client_secret", Constants.CLIENT_SECRET));
            return true;
        }else{
            return false;
        }
    }

    private void fillValues(){

        User me = app.getMe();
        if(me == null){
            return;
        }

        userNameEdit.setText(me.getUserName());
        emailEdit.setText(me.getEmail());
        emailEdit.setEnabled(false);
        isMale = me.getGender().equals("M") ? true :false;
        if(isMale){
            genderMale.setTypeface(genderMale.getTypeface(), Typeface.BOLD);
            genderFemale.setTypeface(genderFemale.getTypeface(), Typeface.NORMAL);
        }else{
            genderMale.setTypeface(genderMale.getTypeface(), Typeface.NORMAL);
            genderFemale.setTypeface(genderFemale.getTypeface(), Typeface.BOLD);
        }

        firstNameEdit.setText(me.getFirstName());
        lastNameEdit.setText(me.getLastName());
        countryEdit.setText(me.getCountry());
        cityEdit.setText(me.getCity());
        aboutEdit.setText(me.getAbout());
        birthDateEdit.setText(me.getBirthDateTxt());

        if(me.getPhotoPath() != null){
            Log.d(TAG, "PHOTO PATH : " + me.getPhotoPath());
            if(!me.getPhotoPath().equals("") && !me.getPhotoPath().equals("placeholders/profile.png"))
            ImageLoader.getInstance().displayImage(me.getPhotoPath(), profilePic,app.options);
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
            case Constants.PROFILE_EDIT_SUCCESSFUL:
                getActivity().finish();
                BaseActivity.setBackwardsTranslateAnimation(getActivity());
        }
    }
}
