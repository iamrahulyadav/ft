package com.mallardduckapps.fashiontalks.fragments;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
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
import com.mallardduckapps.fashiontalks.ProfileActivity;
import com.mallardduckapps.fashiontalks.R;
import com.mallardduckapps.fashiontalks.UploadNewStyleActivity;
import com.mallardduckapps.fashiontalks.objects.User;
import com.mallardduckapps.fashiontalks.tasks.RegisterTask;
import com.mallardduckapps.fashiontalks.utils.Constants;
import com.mallardduckapps.fashiontalks.utils.FTUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.apache.http.message.BasicNameValuePair;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import eu.janmuller.android.simplecropimage.CropImage;

/**
 * Created by oguzemreozcan on 13/01/15.
 */
public class RegisterFragment extends BasicFragment implements RegisterTask.RegisterTaskCallback {

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
    public File mFileTemp;
    public final Uri CONTENT_URI = Uri.parse("content://eu.janmuller.android.simplecropimage.example/");
    public final String TEMP_PHOTO_FILE_NAME = "temp_photo.jpg";

    boolean isEditProfile;
    FashionTalksApp app;
    OnLoginFragmentInteractionListener mListener;
    boolean profileImageSaved;

    public RegisterFragment() {
    }

    @Override
    public void setTag() {
        TAG = "Kayıt";
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            mFileTemp = new File(Environment.getExternalStorageDirectory(), TEMP_PHOTO_FILE_NAME);
        } else {
            mFileTemp = new File(getActivity().getFilesDir(), TEMP_PHOTO_FILE_NAME);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.register_layout, container, false);
        FTUtils.setFont(container, FTUtils.loadFont(getActivity().getAssets(), getString(R.string.font_helvatica_thin)));

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
        genderMale.setTypeface(genderMale.getTypeface(), Typeface.BOLD);
        genderFemale.setTypeface(genderFemale.getTypeface(), Typeface.BOLD);
        isEditProfile = getArguments().getBoolean("EDIT_PROFILE", false);

        genderMale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isEditProfile) {
                    return;
                }
                isMale = true;
                genderMale.setTextColor(getResources().getColor(R.color.black));
                genderFemale.setTextColor(getResources().getColor(R.color.gray));

            }
        });
        genderFemale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isEditProfile) {
                    return;
                }
                isMale = false;
                genderMale.setTextColor(getResources().getColor(R.color.gray));
                genderFemale.setTextColor(getResources().getColor(R.color.black));
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
        app = (FashionTalksApp) getActivity().getApplication();
        if (isEditProfile) {
            Log.d(TAG, "FILL VALUES");
            registerButton.setText(getString(R.string.save));
            fillValues();
        }
        registerButton.setTypeface(FTUtils.loadFont(getActivity().getAssets(), getString(R.string.font_helvatica_md)));

        profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                app.openUploadPicDialog(getActivity(), RegisterFragment.this);
            }
        });

        return rootView;
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

    public String getEncodedImage() {
        if (mFileTemp == null || !profileImageSaved) {
            return null;
        }
        Bitmap bm = BitmapFactory.decodeFile(mFileTemp.getAbsolutePath());
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object
        byte[] byteArrayImage = baos.toByteArray();
        return Base64.encodeToString(byteArrayImage, Base64.DEFAULT);
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
        String file = getEncodedImage();
        BasicNameValuePair filePair = null;
        if (file != null) {
            filePair = new BasicNameValuePair("file", file);
        }
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
                    new BasicNameValuePair("client_secret", Constants.CLIENT_SECRET),
                    filePair);

            //TODO Control Other fields
            return true;
        } else if (isEditProfile && !TextUtils.isEmpty(firstName) && !TextUtils.isEmpty(lastName) && !TextUtils.isEmpty(userName)) {
            RegisterTask task = new RegisterTask(this, true);
            task.execute(new BasicNameValuePair("username", userName),
                    //new BasicNameValuePair("password", password),
                    new BasicNameValuePair("birth_date", birthDate),
                    new BasicNameValuePair("first_name", firstName),
                    new BasicNameValuePair("last_name", lastName),
                    new BasicNameValuePair("country", country),
                    new BasicNameValuePair("city", city),
                    new BasicNameValuePair("about", about),
                    filePair);
            //new BasicNameValuePair("gender", isMale ? "M" : "F"),
            //new BasicNameValuePair("client_id", Constants.CLIENT_ID),
            //new BasicNameValuePair("client_secret", Constants.CLIENT_SECRET));
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "UPLOAD PIC RESULT: requestCode: " + requestCode + " - resultCode: " + resultCode);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == UploadNewStyleActivity.REQ_PICK_IMAGE) {
                Log.d(TAG, "REQUEST PICK");
                try {
                    InputStream inputStream = getActivity().getContentResolver().openInputStream(data.getData());
                    FileOutputStream fileOutputStream = new FileOutputStream(mFileTemp);
                    copyStream(inputStream, fileOutputStream);
                    fileOutputStream.close();
                    inputStream.close();
                    startCropImage();

                } catch (Exception e) {
                    Log.e(TAG, "Error while creating temp file", e);
                }
            } else if (requestCode == UploadNewStyleActivity.REQ_CAMERA) {
                Log.d(TAG, "REQUEST CAMERA");
                startCropImage();
            } else if (requestCode == UploadNewStyleActivity.REQ_CROP) {
                String path = data.getStringExtra(CropImage.IMAGE_PATH);
                if (path == null) {
                    return;
                }
                Bundle bundle = new Bundle();
                bundle.putString("IMAGE_PATH", mFileTemp.getPath());
                Log.d(TAG, "FILE SOURCE: " + mFileTemp.getPath());
                // String url = "file://"+mFileTemp.getPath();
                profilePic.setImageBitmap(BitmapFactory.decodeFile(mFileTemp.getAbsolutePath()));
                profileImageSaved = true;
                //ImageLoader.getInstance().displayImage(url, profilePic,app.options);
            }
        } else if (resultCode == 99) {
            if (requestCode == UploadNewStyleActivity.REQ_CAMERA_CALL) {
                takePicture();
            } else if (requestCode == UploadNewStyleActivity.REQ_PICK_IMAGE_CALL) {
                openGallery();
            }
        }
    }

    private void fillValues() {
        User me = app.getMe();
        if (me == null) {
            return;
        }
        userNameEdit.setText(me.getUserName());
        emailEdit.setText(me.getEmail());
        emailEdit.setEnabled(false);
        isMale = me.getGender().equals("M") ? true : false;
        if (isMale) {
            genderMale.setTextColor(getResources().getColor(R.color.black));
            genderFemale.setTextColor(getResources().getColor(R.color.gray));
        } else {
            genderMale.setTextColor(getResources().getColor(R.color.gray));
            genderFemale.setTextColor(getResources().getColor(R.color.black));
        }

        firstNameEdit.setText(me.getFirstName());
        lastNameEdit.setText(me.getLastName());
        countryEdit.setText(me.getCountry());
        cityEdit.setText(me.getCity());
        aboutEdit.setText(me.getAbout());
        Log.d(TAG, "ABOUT ME: " + me.getAbout());
        birthDateEdit.setText(me.getBirthDateTxt());

        if (me.getPhotoPath() != null) {
            Log.d(TAG, "PHOTO PATH : " + me.getPhotoPath());
            if (!me.getPhotoPath().equals("") && !me.getPhotoPath().equals("placeholders/profile.png")) {
                String url = new StringBuilder(Constants.CLOUD_FRONT_URL).append("/100x100/").append(me.getPhotoPath()).toString();
                ImageLoader.getInstance().displayImage(url, profilePic, app.options);
                profileImageSaved = false;
            }
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
                if(user != null)
                    app.setMe(user);
                mListener.saveTokens(true,tokens);
                mListener.goToMainActivity();
                break;
            case Constants.PROFILE_EDIT_SUCCESSFUL:
                if(user != null){
                    app.setMe(user);
                    //ProfileActivity.userInfoChanged = true;
                    Log.d(TAG, "USER UPDATED: " + app.getMe().getAbout());
                }

                mListener.goToMainActivity();
                //getActivity().finish();
                //BaseActivity.setBackwardsTranslateAnimation(getActivity());
        }
    }

    public static void copyStream(InputStream input, OutputStream output)
            throws IOException {
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = input.read(buffer)) != -1) {
            output.write(buffer, 0, bytesRead);
        }
    }

    private void startCropImage() {

        Intent intent = new Intent(this.getActivity(), CropImage.class);
        intent.putExtra(CropImage.IMAGE_PATH, mFileTemp.getPath());
        intent.putExtra(CropImage.SCALE, false);

        intent.putExtra(CropImage.ASPECT_X, 1);
        intent.putExtra(CropImage.ASPECT_Y, 1);

        RegisterFragment.this.startActivityForResult(intent, UploadNewStyleActivity.REQ_CROP);
    }

    public void openGallery() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        RegisterFragment.this.startActivityForResult(photoPickerIntent, UploadNewStyleActivity.REQ_PICK_IMAGE);
    }

    public void takePicture() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            Uri mImageCaptureUri = null;
            String state = Environment.getExternalStorageState();
            if (Environment.MEDIA_MOUNTED.equals(state)) {
                mImageCaptureUri = Uri.fromFile(mFileTemp);
            } else {
                /*
	        	 * The solution is taken from here: http://stackoverflow.com/questions/10042695/how-to-get-camera-result-as-a-uri-in-data-folder
	        	 */
                mImageCaptureUri = CONTENT_URI;
            }
            intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
            intent.putExtra("return-data", true);
            RegisterFragment.this.startActivityForResult(intent, UploadNewStyleActivity.REQ_CAMERA);
        } catch (ActivityNotFoundException e) {

            Log.d(TAG, "cannot take picture", e);
        }
    }
}
