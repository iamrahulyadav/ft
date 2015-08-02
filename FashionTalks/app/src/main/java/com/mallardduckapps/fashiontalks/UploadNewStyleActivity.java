package com.mallardduckapps.fashiontalks;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mallardduckapps.fashiontalks.fragments.UploadNewStyleBrandFragment;
import com.mallardduckapps.fashiontalks.fragments.UploadNewStyleMainFragment;
import com.mallardduckapps.fashiontalks.fragments.UploadNewStyleTitleFragment;
import com.mallardduckapps.fashiontalks.utils.FTUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import eu.janmuller.android.simplecropimage.CropImage;

public class UploadNewStyleActivity extends BaseActivity implements UploadNewStyleMainFragment.OnFragmentInteractionListener {

    public final static int REQ_PICK_IMAGE = 0;
    public final static int REQ_CAMERA = 1;
    public final static int REQ_CROP = 2;
    public final static int REQ_PICK_IMAGE_CALL = 10;
    public final static int REQ_CAMERA_CALL = 11;
    public final static int REQ_CROP_CALL = 12;

    //Uri mImageUri;
    TextView tvName;
    public static int width;
    public static int height;


    public static File      mFileTemp;
    UploadNewStyleMainFragment mainFragment;
    public static final Uri CONTENT_URI = Uri.parse("content://eu.janmuller.android.simplecropimage.example/");
    public static final String TEMP_PHOTO_FILE_NAME = "temp_photo.jpg";
    static boolean fromGallery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //setContentView(R.layout.activity_settings);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true); //false
        tabToolbar.setVisibility(View.GONE);
        mViewPager.setVisibility(View.GONE);
        topDivider.setVisibility(View.VISIBLE);
        LinearLayout.LayoutParams param = (LinearLayout.LayoutParams)mainLayout.getLayoutParams();
        param.topMargin = 0;



        tvName = (TextView) findViewById(R.id.toolbarName);
        tvName.setTypeface(FTUtils.loadFont(getAssets(), getString(R.string.font_avantgarde_bold)));
        int[] size = FTUtils.getScreenSize(this);
        width = size[0];
        height = size[1];
        mainFragment = new UploadNewStyleMainFragment();
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            mFileTemp = new File(Environment.getExternalStorageDirectory(), TEMP_PHOTO_FILE_NAME);
        }
        else {
            mFileTemp = new File(getFilesDir(), TEMP_PHOTO_FILE_NAME);
        }
/*        mainToolbar = (Toolbar)findViewById(R.id.mainToolbar);
        setSupportActionBar(mainToolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);*/
        actionBar.setDefaultDisplayHomeAsUpEnabled(true);
       // actionBar.setDisplayHomeAsUpEnabled(true);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container,mainFragment )
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_settings, menu);

        return true;
    }

    private void onBackToTitleScreen(){
        tvName.setText(getResources().getString(R.string.upload_new_title));
        getSupportFragmentManager().popBackStack();
        //actionBar.setHomeAsUpIndicator(R.drawable.hamburger_menu);

        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDefaultDisplayHomeAsUpEnabled(true);
        //actionBar.setDisplayHomeAsUpEnabled(true);
        //actionBar.setDisplayShowHomeEnabled(true);
        //actionBar.setDefaultDisplayHomeAsUpEnabled(true);
    }

    private void onBackToMainScreen(){
        tvName.setText(getResources().getString(R.string.app_name_caps));
        getSupportFragmentManager().popBackStack();
        //actionBar.setHomeAsUpIndicator(R.drawable.hamburger_menu);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDefaultDisplayHomeAsUpEnabled(false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            if (getSupportFragmentManager().getBackStackEntryCount() == 2 ){
                onBackToTitleScreen();
            }else if(getSupportFragmentManager().getBackStackEntryCount() == 1 ){
                onBackToMainScreen();
            }
            else {
                menu.toggle();
                //finish();
            }
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        if (getSupportFragmentManager().getBackStackEntryCount() == 2 ){
            onBackToTitleScreen();
        }else if(getSupportFragmentManager().getBackStackEntryCount() == 1 ){
            onBackToMainScreen();
        }
        else {
            if(menu.isMenuShowing()){
                //app.exitDialog(this);
                finish(); //menu.toggle();
            }else{
                menu.toggle();
            }
            //finish();
        }
    }

    public void openGallery() {
        fromGallery = true;
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, UploadNewStyleActivity.REQ_PICK_IMAGE);
    }

    public void takePicture() {
        fromGallery = false;
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            Uri mImageCaptureUri = null;
            String state = Environment.getExternalStorageState();
            if (Environment.MEDIA_MOUNTED.equals(state)) {
                mImageCaptureUri = Uri.fromFile(UploadNewStyleActivity.mFileTemp);
            }
            else {
	        	/*
	        	 * The solution is taken from here: http://stackoverflow.com/questions/10042695/how-to-get-camera-result-as-a-uri-in-data-folder
	        	 */
                mImageCaptureUri = CONTENT_URI;
            }
            intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
            intent.putExtra("return-data", true);
            startActivityForResult(intent, UploadNewStyleActivity.REQ_CAMERA);
        } catch (ActivityNotFoundException e) {

            Log.d(TAG, "cannot take picture", e);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "ON ACTIVItY RESULT: result code: " + resultCode + " -requestCode: " + requestCode );
        if(resultCode == CropImage.RESULT_RETRY){
            if(mainFragment != null){
                if(!fromGallery){
                    takePicture();
                }else{
                    openGallery();
                }
            }
            return;
        }
        if (resultCode != RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case REQ_PICK_IMAGE:
                try {
                    InputStream inputStream = getContentResolver().openInputStream(data.getData());
                    FileOutputStream fileOutputStream = new FileOutputStream(mFileTemp);
                    copyStream(inputStream, fileOutputStream);
                    fileOutputStream.close();
                    inputStream.close();
                    startCropImage();

                } catch (Exception e) {
                    Log.e(TAG, "Error while creating temp file", e);
                }
                break;
            case REQ_CAMERA:
                startCropImage();
                break;
            case REQ_CROP:
                String path = data.getStringExtra(CropImage.IMAGE_PATH);
                if (path == null) {
                    return;
                }
                Bundle bundle = new Bundle();
                bundle.putString("IMAGE_PATH",mFileTemp.getPath() );//mImageUri.toString());
                UploadNewStyleTitleFragment fragment = new UploadNewStyleTitleFragment();
                fragment.setArguments(bundle);
                //Log.d(TAG, "DATA GET DATA IMAGE PATH: " + selectedImagePath);
                FragmentTransaction fragmentTx = getSupportFragmentManager().beginTransaction();
                fragmentTx.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_from_left, R.anim.enter_from_left, R.anim.exit_from_right);
                //fragmentTx.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_from_left);
                //fragmentTx.setCustomAnimations(android.R.anim.slide_in_left,android.R.anim.slide_out_right, android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                fragmentTx.replace(R.id.container, fragment).addToBackStack(fragment.TAG)
                        .commitAllowingStateLoss();
                tvName.setText(getResources().getString(R.string.upload_new_title));
                actionBar.setDisplayHomeAsUpEnabled(true);
                actionBar.setDisplayShowHomeEnabled(false);
                actionBar.setDefaultDisplayHomeAsUpEnabled(true);

                //bitmap = BitmapFactory.decodeFile(mFileTemp.getPath());
                //mImageView.setImageBitmap(bitmap);
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

//    public String getPath(Uri uri) {
//        String[] projection = { MediaStore.Images.Media.DATA };
//        Cursor cursor = managedQuery(uri, projection, null, null, null);
//        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
//        cursor.moveToFirst();
//        return cursor.getString(column_index);
//    }

//    public File createTemporaryFile(String part, String ext) throws Exception
//    {
//        File tempDir = Environment.getExternalStorageDirectory();
//        tempDir = new File(tempDir.getAbsolutePath()+"/.temp/");
//        if(!tempDir.exists())
//        {
//            tempDir.mkdir();
//        }
//        return File.createTempFile(part, ext, tempDir);
//    }

    public static void copyStream(InputStream input, OutputStream output)
            throws IOException {
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = input.read(buffer)) != -1) {
            output.write(buffer, 0, bytesRead);
        }
    }

    private void startCropImage() {

        Intent intent = new Intent(this, CropImage.class);
        intent.putExtra(CropImage.IMAGE_PATH, mFileTemp.getPath());
        intent.putExtra(CropImage.SCALE, false);

        intent.putExtra(CropImage.ASPECT_X, 1);
        intent.putExtra(CropImage.ASPECT_Y, 1);

        startActivityForResult(intent, UploadNewStyleActivity.REQ_CROP);
    }

    @Override
    public void onFragmentInteraction(String tag, String value) {

        String title = getResources().getString(R.string.upload_new_brand);
        if(tag.equals("CONTINUE")){
            Bundle bundle = new Bundle();
            bundle.putString("IMAGE_PATH",mFileTemp.getPath() );//mImageUri.toString());
            bundle.putString("POST_TITLE",value );//mImageUri.toString());
            UploadNewStyleTitleFragment fragment = new UploadNewStyleBrandFragment();
            fragment.setArguments(bundle);
            FragmentTransaction fragmentTx = getSupportFragmentManager().beginTransaction();
            fragmentTx.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_from_left, R.anim.enter_from_left, R.anim.exit_from_right);
            //fragmentTx.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_from_left);//android.R.anim.slide_out_right, android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            fragmentTx.replace(R.id.container, fragment).addToBackStack(fragment.TAG).commit();
            //actionBar.setHomeAsUpIndicator();
            //actionBar.setHomeAsUpIndicator(android.R.drawable.ic_a);
            Log.d(TAG, "CONTINUE BUTTON CLICKED");
            //actionBar.setHomeButtonEnabled(true);

            //actionBar.setDisplayHomeAsUpEnabled(true);
            //actionBar.setDisplayShowHomeEnabled(false);
            //actionBar.setDefaultDisplayHomeAsUpEnabled(false);

        }else if(tag.equals("SEND_BACK")){
            Log.d(TAG, "CONTINUE BUTTON CLICKED - send back");
            title = getResources().getString(R.string.upload_new_title);
            //getSupportFragmentManager().popBackStack();

            //actionBar.setDisplayHomeAsUpEnabled(true);
            //actionBar.setDisplayShowHomeEnabled(true);
        }else{
            Log.d(TAG, "CONTINUE BUTTON CLICKED -else");
            title = getResources().getString(R.string.app_name_caps);

            //actionBar.setDisplayHomeAsUpEnabled(true);

            //actionBar.setDisplayShowHomeEnabled(true);
        }

        tvName.setText(title);

        //actionBar.setDefaultDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void setBackButton(boolean backButton) {
        if(actionBar != null){
            if(backButton){
                actionBar.setHomeAsUpIndicator(R.drawable.ic_action_back);
            }else{
                actionBar.setHomeAsUpIndicator(R.drawable.hamburger_menu);
            }
        }
    }

    @Override
    public void onClickGallery() {
        openGallery();
    }

    @Override
    public void onClickTakePicture() {
        takePicture();
    }
}
