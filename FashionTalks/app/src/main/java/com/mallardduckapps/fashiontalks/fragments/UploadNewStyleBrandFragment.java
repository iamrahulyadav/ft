package com.mallardduckapps.fashiontalks.fragments;


import android.app.Fragment;
import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mallardduckapps.fashiontalks.BaseActivity;
import com.mallardduckapps.fashiontalks.MainActivity;
import com.mallardduckapps.fashiontalks.R;
import com.mallardduckapps.fashiontalks.UploadNewStyleActivity;
import com.mallardduckapps.fashiontalks.components.ExpandablePanel;
import com.mallardduckapps.fashiontalks.loaders.SearchBrandLoader;
import com.mallardduckapps.fashiontalks.objects.Glam;
import com.mallardduckapps.fashiontalks.objects.Tag;
import com.mallardduckapps.fashiontalks.services.RestClient;
import com.mallardduckapps.fashiontalks.utils.Constants;
import com.mallardduckapps.fashiontalks.utils.FTUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

/**
 * A simple {@link Fragment} subclass.
 */
public class UploadNewStyleBrandFragment extends UploadNewStyleTitleFragment implements LoaderManager.LoaderCallbacks<ArrayList<Tag>> {

    RelativeLayout topBar;
    EditText brandEdit;
    TextView okTV;
    TextView noBrand;
    ProgressBar progressBar;
    ProgressBar progressBarMain;
    boolean sendActive = true;
    int posX;
    int posY;
    //ExpandablePanel glam;
    ExpandablePanel currentGlam;
    boolean keyboardOpen;
    boolean newGlamReadyToAdd = true;
    boolean glamDeleteState = false;
    boolean editTextVisible = true;
    private ArrayAdapter<String> adapter;
    private ArrayList<Tag> tags;
    //private ArrayList<String> testData;
    private ArrayList<String> listData;
    private ListView lv;
    private Timer timer;
    private final long DELAY = 500; // in ms
    String encodedImage;
    private int imageWidth;
    private int imageHeight;
    private ArrayList<Glam> glamList;
    String postTitle;
    //SearchTask task;
    SearchBrandLoader loader;

    ImageView postPhoto;

    public UploadNewStyleBrandFragment() {
        setTag();
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tags = new ArrayList<>();
        listData = new ArrayList<>();
        timer = new Timer();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View rootView = inflater.inflate(R.layout.fragment_upload_new_style_brand, container, false);
        lv = (ListView) rootView.findViewById(R.id.tagsListView);
        noBrand = (TextView) rootView.findViewById(R.id.noBrandTv);
        lv.setVisibility(View.GONE);
        layout = (RelativeLayout) rootView.findViewById(R.id.mainPostLayout);
        bottomBar = (RelativeLayout) rootView.findViewById(R.id.bottomBar);
        topBar = (RelativeLayout) rootView.findViewById(R.id.topBar);
        progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);
        progressBarMain = (ProgressBar) rootView.findViewById(R.id.progressBar1);
        progressBarMain.setVisibility(View.GONE);
        toggleTopBar();
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, "LIST SELECTED TEXT: " + listData.get(position));
                String extraField = " ";
                if(listData.get(position).equals("")){
                    extraField = "";
                }
                //resetLoader(listData.get(position));
//                if(task != null){
//                    task.cancel(true);
//                }
                currentGlam.setText(listData.get(position) + extraField);//" ".concat(listData.get(position)).concat("  ")
                glamReady(listData.get(position));
            }
        });
        okTV = (TextView) rootView.findViewById(R.id.okTv);
        brandEdit = (EditText) rootView.findViewById(R.id.brandEditText);

        brandEdit.addTextChangedListener(new TextWatcher() {
            String text = "";
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(timer != null){
                    timer.cancel();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                //String text =
                String t = s.toString().trim();
                if(t.equals(text)){
                    return;
                }
                text = s.toString().trim().toLowerCase(Locale.getDefault());
                Log.d(TAG, "TEXT: " + text);
                if(text.length() > 1){
                    timer = new Timer();
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            UploadNewStyleBrandFragment.this.getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    //task = new SearchTask();
                                    //task.execute(text);
                                    useLoader(text);
                                }
                            });
                        }
                    }, DELAY);
                }else if(text.length() == 0){
                    lv.setVisibility(View.GONE);
                    progressBar.setVisibility(View.GONE);
                }
            }
        });

        okTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(brandEdit.getText().length() > 1){
                    //terminateTask();
                    //resetLoader(brandEdit.getText().toString());
                    currentGlam.setText(brandEdit.getText().toString()+"  ");
                    glamReady(brandEdit.getText().toString());
                }
            }
        });

        postPhoto = (ImageView) rootView.findViewById(R.id.postImage);
        postPhoto.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(!newGlamReadyToAdd){
                    return false;
                }
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        posX = (int) event.getX();
                        posY = (int) event.getY();
                        topBar.setVisibility(View.VISIBLE);
                        imageWidth = postPhoto.getWidth();
                        imageHeight = postPhoto.getHeight();
                        placeGlam(posX, posY, (int)postPhoto.getX(),
                                (int)postPhoto.getY(), imageWidth, imageHeight);
                        toggleTopBar();
                        //showKeyboard(brandEdit);
                }
                return false;
            }
        });

        imagePath = getArguments().getString("IMAGE_PATH");
        postTitle = getArguments().getString("POST_TITLE");
        mImageUri = Uri.parse(imagePath);
        Bitmap bitmap = BitmapFactory.decodeFile(mImageUri.getPath());
        postPhoto.setImageBitmap(bitmap);
        initPostForUpload();
       // grabImage(postPhoto);
        return rootView;
    }

    private void initPostForUpload(){
        if(mImageUri == null){
            return;
        }
        Bitmap bm = BitmapFactory.decodeFile(mImageUri.getPath());
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object
        byte[] byteArrayImage = baos.toByteArray();
        encodedImage = Base64.encodeToString(byteArrayImage, Base64.DEFAULT);
    }

    private void createPostJson() throws JSONException {
        JSONObject object = new JSONObject();
        object.put("image", encodedImage);
        object.put("title", postTitle);
        JSONArray array = new JSONArray();
        for(int i = 0; i < glamList.size(); i ++){
            JSONObject glamObject = new JSONObject();
            glamObject.put("tag", glamList.get(i).getTag());
            glamObject.put("x", glamList.get(i).getX());
            glamObject.put("y", glamList.get(i).getY());
            array.put(i, glamObject);
        }
        object.put("tags", array);
        Log.d(TAG, "JSON: " + object.toString(1));
        //Log.d(TAG, "JSON title: " + object.getString("title"));
        SendPhotoTask task = new SendPhotoTask();
        task.execute(object.toString());

    }

    private void glamReady(String tagName){
        lv.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
        toggleTopBar();
        newGlamReadyToAdd = true;
        hideKeyboard();
        Log.d(TAG, "GLAM TEXT: " + tagName);
        //String glamText = glam.getText().trim();
        int x = (int)currentGlam.getX();
        if(!currentGlam.isLhsAnimation()){
            x += currentGlam.getWidth();
        }
        Glam glamItem = new Glam(getVirtualX(x), getVirtualY((int)currentGlam.getY()), tagName);
        if(glamList == null){
            glamList = new ArrayList<>();
        }
        glamList.add(glamItem);
        brandEdit.setText("");
    }

    public void filter() {
        listData.clear();
        lv.setVisibility(View.GONE);
        if(tags.size() == 0){
            return;
        }
        for(Tag tag : tags){
            listData.add(tag.getTag());
        }

        if(adapter == null){
            adapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_list_item_1, listData);
            lv.setAdapter(adapter);
        }else{
            adapter.notifyDataSetChanged();
        }
        FTUtils.setListViewHeightBasedOnChildren(lv);
        lv.bringToFront();
        lv.setVisibility(View.VISIBLE);
    }

    private void hideKeyboard() {
        // Check if no view has focus:
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            keyboardOpen = false;
        }
    }

    private void showKeyboard(EditText editText) {
        editText.requestFocus();
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
        keyboardOpen = true;
    }

    private void placeGlam(int x, int y, final int leftBorder, final int topBorder, final int imageWidth, final int imageHeight) {
        final ExpandablePanel glam = new ExpandablePanel(getActivity(), null, x, y, x > UploadNewStyleActivity.width / 2 ? true : false, true);
        final int rightBorder = leftBorder + imageWidth - glam.getWidth();
        final int bottomBorder = topBorder + imageHeight - (int)getResources().getDimension(R.dimen.glam_width);
        currentGlam = glam;
        //panel.setTagText(new StringBuilder("").append(pivot.getGlamCount()).append(" | ").append(tag.getTag()).append(" ").toString());
        glam.setTypeface(FTUtils.loadFont(getActivity().getAssets(), getActivity().getString(R.string.font_helvatica_lt)));
        glam.setTag("ExpandablePanel");
        layout.addView(glam);
        newGlamReadyToAdd = false;
        glam.setOnTouchListener(new View.OnTouchListener() {
            int status = -1;
            float dx;
            float dy;
            boolean moveEnabled = true;
            RelativeLayout.LayoutParams lp;
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        dx = event.getX();
                        dy = event.getY();
                        status = 0;
                        break;
                    case MotionEvent.ACTION_MOVE:
                        lp = (RelativeLayout.LayoutParams) v.getLayoutParams();
                        int left = lp.leftMargin + ((int) (event.getX() - dx));
                        int top = lp.topMargin + ((int) (event.getY() - dy));
                        if (left > leftBorder && left < rightBorder && top > topBorder && top < bottomBorder) {
                            lp.leftMargin = left;
                            lp.topMargin = top ;
                            v.setLayoutParams(lp);
                        }
                        if(lp.leftMargin > UploadNewStyleActivity.width/2){
                            glam.setLhsAnimation(false);
                        }else{
                            glam.setLhsAnimation(true);
                        }
                        status = 1;
                        break;
                    case MotionEvent.ACTION_UP:
                        if(glam.isReadyToDelete()){
                            layout.removeView(glam);
                            if(glamList != null){
                                glamList.remove(glam);
                            }
                            break;
                        }
                        if(status != 1 ){
                            if(newGlamReadyToAdd)
                                glam.setReadyToDelete(true);
                            //glam.setReadyToDelete(glam.isReadyToDelete());
                        }
                        //Log.d(TAG, "DROP ID: " + v.getgetId());
                        status = 2;
                        break;
                    case MotionEvent.ACTION_OUTSIDE:
                        moveEnabled = false;
                        break;
                }
                return true;
            }
        });
    }

    public void toggleTopBar(){
        Animation slide;
        Animation.AnimationListener animationListener = new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                if(!editTextVisible){
                    brandEdit.setVisibility(View.VISIBLE);
                    showKeyboard(brandEdit);
                }
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if(editTextVisible){
                    brandEdit.setVisibility(View.INVISIBLE);
                }
                editTextVisible = !editTextVisible;
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        };
        if(editTextVisible){
            slide = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_up_top_menu);
            //Log.d(TAG,"ON CLICK - sldie up");
        }else{
            slide = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_down_top_menu);
            //Log.d(TAG,"ON CLICK - slide up");
        }

        if(slide != null){
            slide.reset();
            if(topBar != null){
                Log.d(TAG,"ON CLICK - start animation");
                topBar.clearAnimation();
                slide.setAnimationListener(animationListener);
                topBar.startAnimation(slide);
            }
        }
    }

    @Override
    public void setTag() {
        TAG = "Upload_New_Style_Brand_Fragment";
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //super.onCreateOptionsMenu(menu,inflater);
        inflater.inflate(R.menu.menu_send, menu);
        menu.removeItem(R.id.action_continue);
        menu.removeItem(R.id.action_null);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //super.onOptionsItemSelected(item);
        if (item.getItemId() == R.id.action_send) {
            Log.d(TAG, "SEND");
            if (sendActive) {
                try {
                    createPostJson();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                mListener.onFragmentInteraction("SEND", "");
            }

        } else if (item.getItemId() == android.R.id.home) {
            hideKeyboard();
            //super.onOptionsItemSelected(item);
            //mListener.onFragmentInteraction("SEND_BACK");
        }
        return false;
    }

    public void resetLoader(String text){
        Bundle bundle = new Bundle();
        Log.d(TAG, "RESET LOADER");
        bundle.putString("SEARCH_KEY", text);
        loader = (SearchBrandLoader) getActivity().getLoaderManager()
                .restartLoader(Constants.SEARCH_TAG_LOADER, bundle, this);
    }

    public void useLoader(String text){
        resetLoader(text);
        loader.forceLoad();
    }

    @Override
    public Loader<ArrayList<Tag>> onCreateLoader(int id, Bundle args) {
        String text = args.getString("SEARCH_KEY");
        Log.d(TAG, "ON CREATE LOADER: with text " + text );
        loader = new SearchBrandLoader(getActivity(), Constants.SEARCH_TAG_LOADER, text);
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(View.VISIBLE);

                if(noBrand.getVisibility() == View.VISIBLE){
                    noBrand.setVisibility(View.GONE);
                }
            }
        });
        return loader;
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<Tag>> loader, ArrayList<Tag> data) {
        Log.d(TAG, "ON LOAD FINISHED: " + data.size());
        if(data.size() == 0){
            //no_brand text
            lv.setVisibility(View.GONE);
            String noBrandTxt = noBrand.getText().toString();
            noBrandTxt = noBrandTxt.replace("|" , brandEdit.getText().toString());
            noBrand.setText(noBrandTxt);
            noBrand.setVisibility(View.VISIBLE);
        }else{
            lv.setVisibility(View.VISIBLE);
            noBrand.setVisibility(View.GONE);
        }
        tags = data;

        progressBar.setVisibility(View.GONE);
        filter();
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<Tag>> loader) {
        listData.clear();
        lv.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
    }

    private int getVirtualX(int x){
        Log.d(TAG, "REAL X: " + x  + " - Image Width: " + imageWidth + " - postPhotoX: " + postPhoto.getX());
        return Constants.VIRTUAL_WIDTH*x/imageWidth;

    }

    private int getVirtualY(int y){
        Log.d(TAG, "REAL Y: " + y  + " - Image Height: " + imageHeight + "- postPhotoY: " + postPhoto.getY());
        return (int)(Constants.VIRTUAL_HEIGHT*(y - postPhoto.getY()))/imageHeight;
    }


    public class SendPhotoTask extends AsyncTask<String, Void, String> {
        private final String TAG = "SendPhotoTask";
        int status = -1;
        public SendPhotoTask(){
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBarMain.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... params) {
            String response = "";
            RestClient restClient = new RestClient();
            try {
                String url = new StringBuilder(Constants.POST_NEW).toString();
                response = restClient.doPostRequestWithJSON(url, null,params[0]);
                Log.d(TAG, "User REQUEST RESPONSE: " + response);
                JSONObject object = new JSONObject(response);
                status = object.getInt("status");
            } catch (Exception e) {
                response = "NO_CONNECTION";
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(String text) {
            super.onPostExecute(text);
            progressBarMain.setVisibility(View.GONE);
            if(status == 0){
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
                getActivity().finish();
                BaseActivity.setTranslateAnimation(getActivity());
            }else{
                Toast.makeText(UploadNewStyleBrandFragment.this.getActivity(), "Bir sorun oluştu! Tekrar dene!", Toast.LENGTH_SHORT).show();
            }


        }
    }
}
