package com.mallardduckapps.fashiontalks.fragments;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.makeramen.RoundedImageView;
import com.mallardduckapps.fashiontalks.MainActivity;
import com.mallardduckapps.fashiontalks.R;
import com.mallardduckapps.fashiontalks.UsersActivity;
import com.mallardduckapps.fashiontalks.adapters.NavDrawerListAdapter;
import com.mallardduckapps.fashiontalks.objects.User;
import com.mallardduckapps.fashiontalks.utils.Constants;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * Fragment used for managing interactions for and presentation of a navigation drawer.
 * See the <a href="https://developer.android.com/design/patterns/navigation-drawer.html#Interaction">
 * design guidelines</a> for a complete explanation of the behaviors implemented here.
 */
public class NavigationDrawerFragment extends BasicFragment {

    /**
     * Remember the position of the selected item.
     */
    private static final String STATE_SELECTED_POSITION = "selected_navigation_drawer_position";
    /**
     * A pointer to the current callbacks instance (the Activity).
     */
    private NavigationDrawerCallbacks mCallbacks;
    private ListView mDrawerListView;
    private View mFragmentContainerView;

    public static int mCurrentSelectedPosition = -1;
    private boolean mFromSavedInstanceState;
    private RoundedImageView thumbnailImage;
    private TextView userNameTv;
    private TextView styleTv;
    //private boolean mUserLearnedDrawer;
    String[] menuItems;

    Integer[] imageId = {
            R.drawable.discover_icon,
            R.drawable.find_users_icon,
            R.drawable.notifications_icon,
            R.drawable.settings_icon,
    };

    public NavigationDrawerFragment() {
    }

/*    public static NavigationDrawerFragment getInstance(){
        if(instance == null){
            instance = new NavigationDrawerFragment();
        }
        return instance;
    }*/

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        if (savedInstanceState != null) {
            mCurrentSelectedPosition = savedInstanceState.getInt(STATE_SELECTED_POSITION);
            mFromSavedInstanceState = true;
        }

        if(getActivity() instanceof MainActivity){
            mCurrentSelectedPosition = 0;
        }else if(getActivity() instanceof UsersActivity){
            mCurrentSelectedPosition = 1;
        }
        // Select either the default item (0) or the last selected item.
        selectItem(mCurrentSelectedPosition, "");
    }

    @Override
    public void setTag() {
        TAG = "NAVIGATION_DRAWER_FRAGMENT";
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // Indicate that this fragment would like to influence the set of actions in the action bar.
        setHasOptionsMenu(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(
                R.layout.fragment_navigation_drawer, container, false);
        mDrawerListView = (ListView)view.findViewById(R.id.drawerListView) ;
        thumbnailImage = (RoundedImageView) view.findViewById(R.id.thumbnailImage);
        userNameTv = (TextView) view.findViewById(R.id.userNameTv);
        styleTv = (TextView) view.findViewById(R.id.styleTv);
        LinearLayout profileClickLayout = (LinearLayout) view.findViewById(R.id.profileClickLayout);
        User me = app.getMe();
        menuItems = new String[]{
            getString(R.string.title_section1),
                    getString(R.string.title_section2),
                    getString(R.string.title_section3),
                    getString(R.string.title_section4)
        };

        String url = new StringBuilder(Constants.CLOUD_FRONT_URL).append("/40x40/").append(me.getPhotoPath()).toString();
        ImageLoader.getInstance().displayImage(url, thumbnailImage, app.options);
        userNameTv.setText(me.getFirstName() +" " + me.getLastName());


        mDrawerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectItem(position, menuItems[position]);
            }
        });
        profileClickLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCallbacks != null) {
                    mCallbacks.onNavigationDrawerItemSelected(-1, "PROFILE");
                }
            }
        });
        //getActionBar().getThemedContext()
        NavDrawerListAdapter adapter = new NavDrawerListAdapter(getActivity(),menuItems,imageId);
        mDrawerListView.setAdapter(adapter);
//        mDrawerListView.setAdapter(new ArrayAdapter<String>(
//                getActivity(),
//                android.R.layout.simple_list_item_activated_1,
//                android.R.id.text1,menuItems));
        mDrawerListView.setItemChecked(mCurrentSelectedPosition, true);
        return view;
    }

    private void selectItem(int position, String actionName) {
        if(position == mCurrentSelectedPosition && !actionName.equals("")){
            actionName = "NO_ACTION";
        }

        mCurrentSelectedPosition = position;
        if (mDrawerListView != null) {
            mDrawerListView.setItemChecked(position, true);
        }
/*        if (mDrawerLayout != null) {
            mDrawerLayout.closeDrawer(mFragmentContainerView);
        }*/
        if (mCallbacks != null) {
            mCallbacks.onNavigationDrawerItemSelected(position, actionName);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallbacks = (NavigationDrawerCallbacks) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement NavigationDrawerCallbacks.");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(STATE_SELECTED_POSITION, mCurrentSelectedPosition);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Forward the new configuration the drawer toggle component.
        //mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // If the drawer is open, show the global app actions in the action bar. See also
        // showGlobalContextActionBar, which controls the top-left area of the action bar.
        //if (mDrawerLayout != null && isDrawerOpen()) {
            inflater.inflate(R.menu.global, menu);
            //showGlobalContextActionBar();
        //}
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
       // if (mDrawerToggle.onOptionsItemSelected(item)) {
        //    return true;
       // }

        if (item.getItemId() == R.id.action_example) {
            Toast.makeText(getActivity(), "Example action.", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Callbacks interface that all activities using this fragment must implement.
     */
    public static interface NavigationDrawerCallbacks {
        /**
         * Called when an item in the navigation drawer is selected.
         */
        void onNavigationDrawerItemSelected(int position, String actionName);
    }
}
