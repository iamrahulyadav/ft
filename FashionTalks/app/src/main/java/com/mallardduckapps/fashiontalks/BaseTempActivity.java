package com.mallardduckapps.fashiontalks;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.Menu;
import android.view.MenuItem;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

public class BaseTempActivity extends SlidingFragmentActivity {

    private int mTitleRes;
    protected ListFragment mFrag;

    public BaseTempActivity(int titleRes) {
        mTitleRes = titleRes;
    }

    public BaseTempActivity(){

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle(mTitleRes);

        // set the Behind View
        setBehindContentView(R.layout.fragment_navigation_drawer);
        if (savedInstanceState == null) {
            //FragmentTransaction t = this.getSupportFragmentManager().beginTransaction();
           // mFrag = new SampleListFragment();
           // t.replace(R.id.menu_frame, mFrag);
           // t.commit();
        } else {
           // mFrag = (ListFragment)this.getFragmentManager().findFragmentById(R.id.menu_frame);
        }

        // customize the SlidingMenu
        SlidingMenu sm = getSlidingMenu();
        sm.setShadowWidthRes(R.dimen.shadow_width);
        sm.setShadowDrawable(R.drawable.drawer_shadow);
        sm.setSelectorEnabled(true);
        sm.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        sm.setFadeDegree(0.35f);
        sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        setSlidingActionBarEnabled(true);

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        //actionBar.setHomeAsUpIndicator(R.drawable.ic_drawer);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                toggle();
                return true;
            //case R.id.github:
                //Util.goToGitHub(this);
               // return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getSupportMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
}
