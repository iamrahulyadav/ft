package com.mallardduckapps.fashiontalks.components;

/**
 * Created by oguzemreozcan on 25/03/15.
 */

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.widget.ListView;

public class BounceListView extends ListView {

    private static final int MAX_Y_OVERSCROLL_DISTANCE = 70;

    private Context mContext;
    private int mMaxYOverscrollDistance;
    private int refreshBoundary;
    private RefreshListener callback;
    private boolean refresh = false;

    public BounceListView(Context context) {
        super(context);
        mContext = context;
        initBounceListView();
    }

    public BounceListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initBounceListView();
    }

    public BounceListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        initBounceListView();
    }

    private void initBounceListView() {
        final DisplayMetrics metrics = mContext.getResources()
                .getDisplayMetrics();
        final float density = metrics.density;

        mMaxYOverscrollDistance = (int) (density * MAX_Y_OVERSCROLL_DISTANCE);
        refreshBoundary = ((mMaxYOverscrollDistance + 15) / 2) * -1;
    }

    public void setRefreshListener(RefreshListener callback) {
        this.callback = callback;
    }

    @SuppressLint("NewApi")
    @Override
    protected boolean overScrollBy(int deltaX, int deltaY, int scrollX,
                                   int scrollY, int scrollRangeX, int scrollRangeY,
                                   int maxOverScrollX, int maxOverScrollY, boolean isTouchEvent) {
        // This is where the magic happens, we have replaced the incoming
        // maxOverScrollY with our own custom variable mMaxYOverscrollDistance;
        boolean overScroll = super.overScrollBy(deltaX, deltaY, scrollX, scrollY,
                scrollRangeX, scrollRangeY, maxOverScrollX,
                mMaxYOverscrollDistance, isTouchEvent);

        if (refreshBoundary > scrollY) {
            if (callback != null && !refresh) {
                refresh = true;
                callback.onRefreshList();
                //Log.d("BOUNCELISTVIEW", "OVER SCROLL: " + mMaxYOverscrollDistance + " - refreshBoundary: " + refreshBoundary + " - scrollY: " + scrollY );
            }
        } else if (scrollY == 0) {
            refresh = false;
        }

        return overScroll;
    }

    public interface RefreshListener {
        public void onRefreshList();
    }

}

