package com.mallardduckapps.fashiontalks;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mallardduckapps.fashiontalks.fragments.SettingsFragment;
import com.mallardduckapps.fashiontalks.utils.FTUtils;

public class SettingsActivity extends BaseActivity {

    //Toolbar mainToolbar;
    //ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_settings);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);// false
        tabToolbar.setVisibility(View.GONE);
        mViewPager.setVisibility(View.GONE);
        topDivider.setVisibility(View.VISIBLE);
        LinearLayout.LayoutParams param = (LinearLayout.LayoutParams)mainLayout.getLayoutParams();
        param.topMargin = 0;
        actionBar.setHomeAsUpIndicator(R.drawable.hamburger_menu);
        TextView tvName = (TextView) findViewById(R.id.toolbarName);
        tvName.setTypeface(FTUtils.loadFont(getAssets(), getString(R.string.font_avantgarde_bold)));
/*        mainToolbar = (Toolbar)findViewById(R.id.mainToolbar);
        setSupportActionBar(mainToolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);*/
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new SettingsFragment())
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            menu.toggle();
        }

        return true;
    }
}
