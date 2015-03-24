package com.mallardduckapps.fashiontalks.components;

/**
 * Created by oguzemreozcan on 12/01/15.
 */

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mallardduckapps.fashiontalks.R;
import com.mallardduckapps.fashiontalks.utils.FTUtils;

import java.util.ArrayList;

/**
 * To be used with ViewPager to provide a tab indicator component which give constant feedback as to
 * the user's scroll progress.
 * <p/>
 * To use the component, simply add it to your view hierarchy. Then in your
 * {@link android.app.Activity} or {@link android.support.v4.app.Fragment} call
 * {@link #setViewPager(ViewPager)} providing it the ViewPager this layout is being used for.
 * <p/>
 * The colors can be customized in two ways. The first and simplest is to provide an array of colors
 * via {@link #setSelectedIndicatorColors(int...)} and {@link #setDividerColors(int...)}. The
 * alternative is via the {@link TabColorizer} interface which provides you complete control over
 * which color is used for any individual position.
 * <p/>
 * The views used as tabs can be customized by calling {@link #setCustomTabView(int, int, int)},
 * providing the layout ID of your custom layout.
 */
public class SlidingTabLayout extends HorizontalScrollView {

    /**
     * Allows complete control over the colors drawn in the tab layout. Set with
     * {@link #setCustomTabColorizer(TabColorizer)}.
     */
    public interface TabColorizer {

        /**
         * @return return the color of the indicator used when {@code position} is selected.
         */
        int getIndicatorColor(int position);

        /**
         * @return return the color of the divider drawn to the right of {@code position}.
         */
        int getDividerColor(int position);

    }

    private static final int TITLE_OFFSET_DIPS = 4;
    private static final int TAB_VIEW_PADDING_DIPS = 10;//7//16
    private static final int TAB_VIEW_PADDING_TOP_DIPS = 4;//4;
    private static final int TAB_VIEW_HORIZONTAL_PADDING_DIPS = 8;
    private static final int TAB_VIEW_TEXT_SIZE_SP = 11;
    private static final int TAB_VIEW_SELECTED_TEXT_SIZE_SP = 13;

    private TabType tabType = TabType.TEXT;

    private int mTitleOffset;

    private int mTabViewLayoutId;
    private int mTabViewTextViewId;
    private int mTabViewImageViewId;
    private Integer titleColor = Color.BLACK;//-1;
    private Integer unSelectedTitleColor = 0xFF9B9B9B;
    private ViewPager mViewPager;
    private ViewPager.OnPageChangeListener mViewPagerPageChangeListener;
    private boolean mDistributeEvenly;
    private final SlidingTabStrip mTabStrip;
    //private ArrayList<TextView> titles;

    public SlidingTabLayout(Context context) {
        this(context, null);
        setFillViewport(true);
    }

    public SlidingTabLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SlidingTabLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        // Disable the Scroll Bar
        setHorizontalScrollBarEnabled(false);
        // Make sure that the Tab Strips fills this View
        setFillViewport(true);
        mTitleOffset = (int) (TITLE_OFFSET_DIPS * getResources().getDisplayMetrics().density);
        mTabStrip = new SlidingTabStrip(context);
        addView(mTabStrip, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
    }

    /**
     * Sets the title color showed in selected tab.
     *
     * @param titleColor
     */
    public void setTitleColor(int titleColor) {
        this.titleColor = titleColor;
    }

    /**
     * Sets the title color showed in unselected tab.
     *
     * @param titleColor
     */
    public void setUnselectedTitleColor(int titleColor) {
        this.unSelectedTitleColor = titleColor;
    }

    /**
     * Sets isFittingChildren of the tab layout. If isFittingChildren is true, then the children will
     * share the total width of the tab layout. Otherwise the children are placed as default.
     *
     * @param isFittingChildren
     */
    public void setFittingChildren(boolean isFittingChildren) {
        mTabStrip.setFittingChildren(isFittingChildren);
    }

    public void setDistributeEvenly(boolean distributeEvenly) {
        mDistributeEvenly = distributeEvenly;
    }

    /**
     * Sets the tab type of the tab layout. Type TEXT shows title as text, ICON shows icon that
     * is found with the title name as suffix(ic_TITLE_THAT_IS_PROVIDED).
     *
     * @param tabType type as enum. TEXT or ICON
     */
    public void setTabType(TabType tabType) {
        this.tabType = tabType;
    }

    /**
     * Set the custom {@link TabColorizer} to be used.
     * <p/>
     * If you only require simple custmisation then you can use
     * {@link #setSelectedIndicatorColors(int...)} and {@link #setDividerColors(int...)} to achieve
     * similar effects.
     */
    public void setCustomTabColorizer(TabColorizer tabColorizer) {
        mTabStrip.setCustomTabColorizer(tabColorizer);
    }

    /**
     * Sets the colors to be used for indicating the selected tab. These colors are treated as a
     * circular array. Providing one color will mean that all tabs are indicated with the same color.
     */
    public void setSelectedIndicatorColors(int... colors) {
        mTabStrip.setSelectedIndicatorColors(colors);
    }

    /**
     * Sets the colors to be used for tab dividers. These colors are treated as a circular array.
     * Providing one color will mean that all tabs are indicated with the same color.
     */
    public void setDividerColors(int... colors) {
        mTabStrip.setDividerColors(colors);
    }

    /**
     * Set the {@link ViewPager.OnPageChangeListener}. When using {@link SlidingTabLayout} you are
     * required to set any {@link ViewPager.OnPageChangeListener} through this method. This is so
     * that the layout can update it's scroll position correctly.
     *
     * @see ViewPager#setOnPageChangeListener(ViewPager.OnPageChangeListener)
     */
    public void setOnPageChangeListener(ViewPager.OnPageChangeListener listener) {
        mViewPagerPageChangeListener = listener;
    }

    /**
     * Set the custom layout to be inflated for the tab views.
     *
     * @param layoutResId Layout id to be inflated
     * @param textViewId  id of the {@link TextView} in the inflated view
     */
    public void setCustomTabView(int layoutResId, int textViewId, int imageViewId) {
        mTabViewLayoutId = layoutResId;
        mTabViewTextViewId = textViewId;
        mTabViewImageViewId = imageViewId;
    }

    /**
     * Sets the associated view pager. Note that the assumption here is that the pager content
     * (number of tabs and tab titles) does not change after this call has been made.
     */
    public void setViewPager(ViewPager viewPager) {
        mTabStrip.removeAllViews();

        mViewPager = viewPager;
        if (viewPager != null) {
            viewPager.setOnPageChangeListener(new InternalViewPagerListener());
            populateTabStrip();
        }
    }

    /**
     * Create a default view to be used for tabs. This is called if a custom tab view is not set via
     * {@link #setCustomTabView(int, int, int)}.
     */
    protected ImageView createDefaultIconTabView(Context context) {
        ImageView tabIconImageView = new ImageView(context);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            // If we're running on Honeycomb or newer, then we can use the Theme's
            // selectableItemBackground to ensure that the View has a pressed state
            TypedValue outValue = new TypedValue();
            getContext().getTheme().resolveAttribute(android.R.attr.selectableItemBackground,
                    outValue, true);
            tabIconImageView.setBackgroundResource(outValue.resourceId);
        }
        int padding = (int) (TAB_VIEW_PADDING_DIPS * getResources().getDisplayMetrics().density);
        int paddingTop = (int) (TAB_VIEW_PADDING_TOP_DIPS * getResources().getDisplayMetrics().density);
        int sidePadding = (int) (TAB_VIEW_HORIZONTAL_PADDING_DIPS * getResources().getDisplayMetrics().density);
        tabIconImageView.setPadding(sidePadding, paddingTop, sidePadding, padding);

        return tabIconImageView;
    }

    /**
     * Create a default view to be used for tabs. This is called if a custom tab view is not set via
     * {@link #setCustomTabView(int, int, int)}.
     */
    protected TextView createDefaultTextTabView(Context context) {
        TextView textView = new TextView(context);
        textView.setGravity(Gravity.CENTER);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, TAB_VIEW_TEXT_SIZE_SP);
        textView.setTypeface(FTUtils.loadFont(context.getAssets(),context.getString(R.string.font_helvatica_lt)));

        //TODO
//        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
//        Display display = wm.getDefaultDisplay();
//        Point size = new Point();
//        display.getSize(size);
//        textView.setWidth(size.x / 3);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            // If we're running on Honeycomb or newer, then we can use the Theme's
            // selectableItemBackground to ensure that the View has a pressed state
            TypedValue outValue = new TypedValue();
            getContext().getTheme().resolveAttribute(android.R.attr.selectableItemBackground,
                    outValue, true);
            textView.setBackgroundResource(outValue.resourceId);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            // If we're running on ICS or newer, enable all-caps to match the Action Bar tab style
            //textView.setAllCaps(true);
        }

        int padding = (int) (TAB_VIEW_PADDING_DIPS * getResources().getDisplayMetrics().density);
        int sidePadding = (int) (TAB_VIEW_HORIZONTAL_PADDING_DIPS * getResources().getDisplayMetrics().density);
        int paddingTop = (int) (TAB_VIEW_PADDING_TOP_DIPS * getResources().getDisplayMetrics().density);
        textView.setPadding(sidePadding, paddingTop, sidePadding, padding);

        System.out.println("c " + titleColor);
        if (titleColor != null) {
            textView.setTextColor(titleColor);
        }
        textView.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f));

        return textView;
    }

    private void populateTabStrip() {
        final PagerAdapter adapter = mViewPager.getAdapter();
        final View.OnClickListener tabClickListener = new TabClickListener();
        //titles = new ArrayList<>(adapter.getCount());
        for (int i = 0; i < adapter.getCount(); i++) {
            View tabView = null;
            TextView tabTitleView = null;
            ImageView tabIconView = null;

            if (mTabViewLayoutId != 0) {
                // If there is a custom tab view layout id set, try and inflate it
                tabView = LayoutInflater.from(getContext()).inflate(mTabViewLayoutId, mTabStrip,
                        false);
                tabTitleView = (TextView) tabView.findViewById(mTabViewTextViewId);
                tabIconView = (ImageView) tabView.findViewById(mTabViewImageViewId);
            }

            if (tabView == null) {
                if (tabType.equals(TabType.TEXT)){
                    tabView = createDefaultTextTabView(getContext());
                    //titles.add((TextView)tabView);
                }
                else if (tabType.equals(TabType.ICON)) {
                    tabView = createDefaultIconTabView(getContext());
                }
            }

            if (tabTitleView == null && TextView.class.isInstance(tabView)) {
                tabTitleView = (TextView) tabView;
            }
            if (mDistributeEvenly) {
                LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) tabView.getLayoutParams();
                lp.width = 0;
                lp.weight = 1;
            }
            // setting title to tabView or its' child
            if (tabTitleView != null) {
                // if tabTitleView could be found in tabView, then set title to it
                tabTitleView.setText(adapter.getPageTitle(i));

            } else if (TextView.class.isInstance(tabView)) {
                // if generated tabView is a text view, get page title and set it to the text view
                ((TextView) tabView).setText(adapter.getPageTitle(i));

            }

            // setting icon to tabView or its' child
            if (tabIconView != null) {
                // if tabIconView could be found in tabView, then set icon to it
                tabIconView.setImageResource(
                        getResourceIdByName("ic_" + adapter.getPageTitle(i).toString()));

            } else if (ImageView.class.isInstance(tabView)) {
                // if generated tabView is an image view, find the resource with the page
                // title as resource name suffix. ex: ic_PAGE_TITLE
                ((ImageView) tabView).setImageResource(
                        getResourceIdByName("ic_" + adapter.getPageTitle(i).toString()));
            }

            tabView.setOnClickListener(tabClickListener);
            mTabStrip.addView(tabView);
        }

        setTitleTextStyle(0);
    }

    protected void setTitleTextStyle(int selectedViewOrder){
        int childCount  = mTabStrip.getChildCount();
        for(int i = 0; i < childCount; i ++){
            View view = mTabStrip.getChildAt(i);
            if(view instanceof TextView){
                if(i == selectedViewOrder){
                    ((TextView) view).setTextColor(titleColor);
                    ((TextView) view).setTextSize(TAB_VIEW_SELECTED_TEXT_SIZE_SP);
                    ((TextView) view).setTypeface(null, Typeface.BOLD);
                }else{
                    ((TextView) view).setTextColor(unSelectedTitleColor);
                    ((TextView) view).setTextSize(TAB_VIEW_TEXT_SIZE_SP);
                    ((TextView) view).setTypeface(null, Typeface.NORMAL);
                }
            }
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        if (mViewPager != null) {
            scrollToTab(mViewPager.getCurrentItem(), 0);
        }
    }

    private int getResourceIdByName(String resourceName) {
        return getResources().getIdentifier(resourceName, "drawable", getContext().getPackageName());
    }

    private void scrollToTab(int tabIndex, int positionOffset) {
        final int tabStripChildCount = mTabStrip.getChildCount();
        if (tabStripChildCount == 0 || tabIndex < 0 || tabIndex >= tabStripChildCount) {
            return;
        }

        View selectedChild = mTabStrip.getChildAt(tabIndex);
        if (selectedChild != null) {
            int targetScrollX = selectedChild.getLeft() + positionOffset;

            if (tabIndex > 0 || positionOffset > 0) {
                // If we're not at the first child and are mid-scroll, make sure we obey the offset
                targetScrollX -= mTitleOffset;
            }

            scrollTo(targetScrollX, 0);
        }
    }

    private class InternalViewPagerListener implements ViewPager.OnPageChangeListener {
        private int mScrollState;

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            int tabStripChildCount = mTabStrip.getChildCount();
            if ((tabStripChildCount == 0) || (position < 0) || (position >= tabStripChildCount)) {
                return;
            }

            mTabStrip.onViewPagerPageChanged(position, positionOffset);

            View selectedTitle = mTabStrip.getChildAt(position);
            int extraOffset = (selectedTitle != null)
                    ? (int) (positionOffset * selectedTitle.getWidth())
                    : 0;
            scrollToTab(position, extraOffset);

            if (mViewPagerPageChangeListener != null) {
                mViewPagerPageChangeListener.onPageScrolled(position, positionOffset,
                        positionOffsetPixels);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            mScrollState = state;

            if (mViewPagerPageChangeListener != null) {
                mViewPagerPageChangeListener.onPageScrollStateChanged(state);
            }
        }

        @Override
        public void onPageSelected(int position) {
            if (mScrollState == ViewPager.SCROLL_STATE_IDLE) {
                mTabStrip.onViewPagerPageChanged(position, 0f);
                scrollToTab(position, 0);
            }
            setTitleTextStyle(position);
            if (mViewPagerPageChangeListener != null) {

                mViewPagerPageChangeListener.onPageSelected(position);
            }
        }

    }

    private class TabClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            for (int i = 0; i < mTabStrip.getChildCount(); i++) {
                if (v == mTabStrip.getChildAt(i)) {
                    mViewPager.setCurrentItem(i);
                    return;
                }
            }
        }
    }

    public enum TabType {
        TEXT, ICON;
    }

}