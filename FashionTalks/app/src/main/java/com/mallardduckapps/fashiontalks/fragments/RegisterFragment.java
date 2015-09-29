package com.mallardduckapps.fashiontalks.fragments;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
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
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.makeramen.roundedimageview.RoundedImageView;
import com.mallardduckapps.fashiontalks.FashionTalksApp;
import com.mallardduckapps.fashiontalks.R;
import com.mallardduckapps.fashiontalks.UploadNewStyleActivity;
import com.mallardduckapps.fashiontalks.adapters.CountryAdapter;
import com.mallardduckapps.fashiontalks.loaders.Exclude;
import com.mallardduckapps.fashiontalks.objects.BasicNameValuePair;
import com.mallardduckapps.fashiontalks.objects.Country;
import com.mallardduckapps.fashiontalks.objects.User;
import com.mallardduckapps.fashiontalks.tasks.GetCountries;
import com.mallardduckapps.fashiontalks.tasks.RegisterTask;
import com.mallardduckapps.fashiontalks.utils.Constants;
import com.mallardduckapps.fashiontalks.utils.FTUtils;
import com.mallardduckapps.fashiontalks.utils.TimeUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.Collator;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;

import eu.janmuller.android.simplecropimage.CropImage;

/**
 * Created by oguzemreozcan on 13/01/15.
 */
public class RegisterFragment extends BasicFragment implements RegisterTask.RegisterTaskCallback, GetCountries.GetCountriesCallback {

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
    ArrayList<Country> countryList;
    boolean isEditProfile;
    FashionTalksApp app;
    OnLoginFragmentInteractionListener mListener;
    boolean profileImageSaved;
    boolean attached = false;
    Spinner countrySpinner;
    Calendar myCalendar = Calendar.getInstance();

    public RegisterFragment() {
    }

    @Override
    public void setTag() {
        TAG = "Kayıt";
    }

    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        }

    };

    private void updateLabel(){
        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, TimeUtil.localeTr);
        birthDateEdit.setText(sdf.format(myCalendar.getTime()));
    }
    int initSpinner = 0;
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
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
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
        countrySpinner = (Spinner) rootView.findViewById(R.id.countrySpinner);
        //countrySpinner.setVisibility(View.GONE);
        cityEdit = (EditText) rootView.findViewById(R.id.city);
        aboutEdit = (EditText) rootView.findViewById(R.id.about);
        genderMale = (Button) rootView.findViewById(R.id.maleButton);
        genderFemale = (Button) rootView.findViewById(R.id.femaleButton);
        genderMale.setTypeface(genderMale.getTypeface(), Typeface.BOLD);
        genderFemale.setTypeface(genderFemale.getTypeface(), Typeface.BOLD);
        isEditProfile = getArguments().getBoolean("EDIT_PROFILE", false);
        initSpinner = 0;
        birthDateEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(getActivity(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        countryEdit.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                countrySpinner.performClick();
            }
        });

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
        countryList = generateCountryList();

        countrySpinner.setAdapter(new CountryAdapter(getActivity(), countryList));
        countrySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //Log.d(TAG, "INIT SPINNER " + initSpinner);
                if(initSpinner > 0){
                    countryEdit.setText(countryList.get(position).getName());
                  //  Log.d(TAG, "INIT SPINNER TEXT SELECTED: " + countryEdit.getText().toString());
                }
                initSpinner ++;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

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
    public void onAttach(Context activity) {
        super.onAttach(activity);
        attached = true;
        try {
            mListener = (OnLoginFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnLoginFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        Log.d(TAG, "ON DETACH");
        hideKeyboard();
        super.onDetach();
        attached = false;
    }

    private void hideKeyboard() {
        // Check if no view has focus:
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
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
        Log.d(TAG, "SIGN UP CONTROL FIELDS");
        String userName = userNameEdit.getText().toString();
        String email = emailEdit.getText().toString();
        String password = passwordEdit.getText().toString();
        String passwordRepeat = passwordAgainEdit.getText().toString();
        String firstName = firstNameEdit.getText().toString();
        String lastName = lastNameEdit.getText().toString();
        String birthDate = birthDateEdit.getText().toString();
        String country = countryEdit.getText().toString();

        //String country1 = ((Country)countrySpinner.getSelectedItem()).getName();
        //Log.d(TAG, "Selected Country Name: " + country1);
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
            Log.d(TAG, "SIGN UP CONTROL FIELDS - REGISTER TASK");
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

    public ArrayList<Country> generateCountryList(){
        String countriesJson = loadJSONFromAsset();
        ArrayList<Country> countryList = new ArrayList<>();
        JsonArray dataObjects = new JsonParser().parse(countriesJson).getAsJsonObject().getAsJsonArray("data");
        //Gson gson = new Gson();
        Exclude ex = new Exclude();
        Gson gson = new GsonBuilder().addDeserializationExclusionStrategy(ex).addSerializationExclusionStrategy(ex).create();
        for (JsonElement item : dataObjects) {
            Country country = gson.fromJson(item, Country.class);
            countryList.add(country);
           // Log.d(TAG, "COUNTRY: " + country.getName());
        }
        final Collator trCollator = Collator.getInstance(TimeUtil.localeTr); //Your locale here
        trCollator.setStrength(Collator.PRIMARY);
        Collections.sort(countryList, new Comparator<Country>() {
            @Override
            public int compare(Country lhs, Country rhs) {
                return trCollator.compare(lhs.getName(), rhs.getName());//lhs.getName().compareTo(rhs.getName());
            }
        });
        return countryList;
    }

    private String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = getActivity().getAssets().open("countries.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    @Override
    public void getAuthStatus(int authStatus, User user, String... tokens) {
        // showProgress(false);
        if(mListener == null || !attached){
            return;
        }

        switch (authStatus) {
            case Constants.DUBLICATE_EMAIL:
                //mPasswordView.setError(getString(R.string.error_incorrect_password));
                //mPasswordView.requestFocus();
                emailEdit.setError(getString(R.string.error_invalid_email));
                emailEdit.requestFocus();
                Toast.makeText(this.getActivity(), getString(R.string.email_in_use), Toast.LENGTH_LONG).show();
                break;
            case Constants.DUBLICATE_USERNAME:
                //mPasswordView.setError(getString(R.string.error_incorrect_password));
                //mPasswordView.requestFocus();
                userNameEdit.setError(getString(R.string.error_invalid_username));
                userNameEdit.requestFocus();
                Toast.makeText(this.getActivity(), getString(R.string.user_name_in_use), Toast.LENGTH_LONG).show();
                break;
            case Constants.AUTHENTICATION_FAILED:
                //mPasswordView.setError(getString(R.string.error_incorrect_password));
                //mPasswordView.requestFocus();
                Toast.makeText(this.getActivity(), getString(R.string.problem_occured), Toast.LENGTH_LONG).show();
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

    @Override
    public void getCountries(ArrayList<Country> countries) {

    }
}
