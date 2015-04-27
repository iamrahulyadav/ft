package com.mallardduckapps.fashiontalks.utils;


import com.mallardduckapps.fashiontalks.swipelistview.SwipeListView;

/**
 * Created by oguzemreozcan on 12/04/15.
 */
public class SwipeListViewSettingsManager {
    private int swipeMode = SwipeListView.SWIPE_MODE_BOTH;
    private boolean swipeOpenOnLongPress = true;
    private boolean swipeCloseAllItemsWhenMoveList = true;
    private long swipeAnimationTime = 0;
    private float swipeOffsetLeft = 0;
    private float swipeOffsetRight = 0;
    private int swipeActionLeft = SwipeListView.SWIPE_ACTION_REVEAL;
    private int swipeActionRight = SwipeListView.SWIPE_ACTION_REVEAL;

    private static SwipeListViewSettingsManager settingsManager = new SwipeListViewSettingsManager();

    public static SwipeListViewSettingsManager getInstance() {
        return settingsManager;
    }

    public static SwipeListViewSettingsManager getSettingsManager() {
        return settingsManager;
    }

    public static void setSettingsManager(SwipeListViewSettingsManager settingsManager) {
        SwipeListViewSettingsManager.settingsManager = settingsManager;
    }

    public long getSwipeAnimationTime() {
        return swipeAnimationTime;
    }

    public void setSwipeAnimationTime(long swipeAnimationTime) {
        this.swipeAnimationTime = swipeAnimationTime;
    }

    public boolean isSwipeCloseAllItemsWhenMoveList() {
        return swipeCloseAllItemsWhenMoveList;
    }

    public void setSwipeCloseAllItemsWhenMoveList(boolean swipeCloseAllItemsWhenMoveList) {
        this.swipeCloseAllItemsWhenMoveList = swipeCloseAllItemsWhenMoveList;
    }

    public int getSwipeMode() {
        return swipeMode;
    }

    public void setSwipeMode(int swipeMode) {
        this.swipeMode = swipeMode;
    }

    public float getSwipeOffsetLeft() {
        return swipeOffsetLeft;
    }

    public void setSwipeOffsetLeft(float swipeOffsetLeft) {
        this.swipeOffsetLeft = swipeOffsetLeft;
    }

    public float getSwipeOffsetRight() {
        return swipeOffsetRight;
    }

    public void setSwipeOffsetRight(float swipeOffsetRight) {
        this.swipeOffsetRight = swipeOffsetRight;
    }

    public boolean isSwipeOpenOnLongPress() {
        return swipeOpenOnLongPress;
    }

    public void setSwipeOpenOnLongPress(boolean swipeOpenOnLongPress) {
        this.swipeOpenOnLongPress = swipeOpenOnLongPress;
    }

    public int getSwipeActionLeft() {
        return swipeActionLeft;
    }

    public void setSwipeActionLeft(int swipeActionLeft) {
        this.swipeActionLeft = swipeActionLeft;
    }

    public int getSwipeActionRight() {
        return swipeActionRight;
    }

    public void setSwipeActionRight(int swipeActionRight) {
        this.swipeActionRight = swipeActionRight;
    }

}