package com.mallardduckapps.fashiontalks;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.appevents.AppEventsLogger;
import com.mallardduckapps.fashiontalks.utils.FTUtils;

import java.util.Locale;

public class StyleUploadActivity extends ActionBarActivity {

    Toolbar mainToolbar;
    ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_style_upload);
        mainToolbar = (Toolbar)findViewById(R.id.mainToolbar);
        ImageView imageView = (ImageView) findViewById(R.id.postingRuleImage);

        setSupportActionBar(mainToolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        TextView tvName = (TextView) findViewById(R.id.toolbarName);
        tvName.setTypeface(FTUtils.loadFont(getAssets(), getString(R.string.font_avantgarde_bold)));
        if(Locale.getDefault().getLanguage().equalsIgnoreCase("en")){
            imageView.setImageResource(R.drawable.posting_rules_en);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_settings, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        finish();
        BaseActivity.setBackwardsTranslateAnimation(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home){
            finish();
            BaseActivity.setBackwardsTranslateAnimation(this);
        }
        return false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        AppEventsLogger.activateApp(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        AppEventsLogger.deactivateApp(this);
    }
}
