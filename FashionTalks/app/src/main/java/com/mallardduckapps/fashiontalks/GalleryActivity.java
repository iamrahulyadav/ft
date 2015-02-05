package com.mallardduckapps.fashiontalks;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.mallardduckapps.fashiontalks.R;
import com.mallardduckapps.fashiontalks.fragments.PopularPostsFragment;
import com.mallardduckapps.fashiontalks.utils.Constants;

public class GalleryActivity extends ActionBarActivity {


    private final static String TAG = "GALLERY_ACTIVITY";
    Toolbar mainToolbar;
    ActionBar actionBar;
    int galleryId;
    FashionTalksApp app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posts);

        galleryId = getIntent().getIntExtra("GALLERY_ID", 0);
        //positionIndex = getIntent().getIntExtra("POST_INDEX", -1);
        Log.d(TAG, "GALLERY ID: " + galleryId);
        app = (FashionTalksApp) getApplication();
        mainToolbar = (Toolbar)findViewById(R.id.mainToolbar);
        setSupportActionBar(mainToolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        PopularPostsFragment galleryFragment = new PopularPostsFragment();
        //galleryFragment.setActivity(activity);
        Bundle bundle = new Bundle();
        bundle.putInt("LOADER_ID", Constants.GALLERY_POSTS_LOADER_ID);
        bundle.putInt("GALLERY_ID", galleryId);
        galleryFragment.setArguments(bundle);

        FragmentTransaction fragmentTx = getSupportFragmentManager().beginTransaction();
        fragmentTx.setCustomAnimations(android.R.anim.slide_in_left,android.R.anim.slide_out_right, android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        fragmentTx.replace(R.id.container, galleryFragment, galleryFragment.TAG);
        fragmentTx.commit();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_gallery, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }else if(id == android.R.id.home){
/*            if (getSupportFragmentManager().getBackStackEntryCount() > 0 ){
                getSupportFragmentManager().popBackStack();
            } else {*/
                finish();
         //   }
        }

        return super.onOptionsItemSelected(item);
    }
}
