package com.mallardduckapps.fashiontalks;

import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mallardduckapps.fashiontalks.R;
import com.mallardduckapps.fashiontalks.fragments.BasicFragment;
import com.mallardduckapps.fashiontalks.fragments.NotificationsFragment;
import com.mallardduckapps.fashiontalks.utils.FTUtils;

public class NotificationActivity extends BaseActivity implements BasicFragment.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);
        tabToolbar.setVisibility(View.GONE);
        mViewPager.setVisibility(View.GONE);
        TextView tvName = (TextView) findViewById(R.id.toolbarName);
        tvName.setTypeface(FTUtils.loadFont(getAssets(), getString(R.string.font_avantgarde_bold)));
        LinearLayout.LayoutParams param = (LinearLayout.LayoutParams)mainLayout.getLayoutParams();
        param.topMargin = 0;
        topDivider.setVisibility(View.VISIBLE);
        //actionBar.setHomeButtonEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.hamburger_menu);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, new NotificationsFragment())
                .commit();
    }


    @Override
    public void onNavigationDrawerItemSelected(int position, String actionName) {
        super.onNavigationDrawerItemSelected(position, actionName);

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
        if (id == R.id.home) {
            menu.toggle();
        }
        return false;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onToolbarThemeChange(int themeId) {

    }

}
