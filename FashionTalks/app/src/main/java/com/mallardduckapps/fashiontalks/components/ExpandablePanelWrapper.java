package com.mallardduckapps.fashiontalks.components;

import android.content.Context;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.mallardduckapps.fashiontalks.FashionTalksApp;
import com.mallardduckapps.fashiontalks.R;
import com.mallardduckapps.fashiontalks.objects.Pivot;
import com.mallardduckapps.fashiontalks.tasks.GlamTask;

/**
 * Created by oguzemreozcan on 02/08/15.
 */
public class ExpandablePanelWrapper extends RelativeLayout {

    private ExpandablePanel glam;

    public ExpandablePanelWrapper(final FashionTalksApp app,final Context context, Pivot pivot, int x, int y, boolean lhsAnimation, boolean ownPost, boolean createPost, boolean adPost) {
        super(context);
        glam = new ExpandablePanel(app, context, pivot, this,x, y, lhsAnimation, ownPost, createPost, adPost);
        int padding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 6, getResources().getDisplayMetrics());
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.leftMargin = x - padding;
        params.topMargin = y - padding;
        addView(glam);
        setPadding(padding,padding,padding,padding);
        setBackgroundColor(getResources().getColor(R.color.transparent));
        setClickable(true);
        setFocusable(true);
        setFocusableInTouchMode(true);

        setLayoutParams(params);
    }

    public ExpandablePanel getGlam(){
        return glam;
    }

 /*   public void runShrinkAnimation(boolean autoClose) {
        if (!mExpanded) {
            return;
        }
        this.autoClose = autoClose;
        Animation a = new ExpandAnimation(expendedWidth, mContentWidth);
        a.initialize(expendedWidth, mContentWidth, expendedWidth, mContentWidth);
        //mListener.onCollapse(ExpandablePanel.this, tagId, brandName);
        a.setDuration(mAnimationDuration);
        a.setFillAfter(true);
        startAnimation(a);
        invalidate();
        //mExpanded = false;
    }

    public void runExpandAnimation(){
        if(mExpanded){
            return;
        }

        Animation a = new ExpandAnimation(mContentWidth, expendedWidth);
        a.initialize(mContentWidth, mContentWidth, mContentWidth, mContentWidth);
        mListener.onExpand(ExpandablePanel.this);
        a.setDuration(mAnimationDuration);
        a.setFillAfter(true);
        startAnimation(a);
        invalidate();
        //mExpanded = true;
    }

    public void animateExpand() {
        Animation a;
        if (mExpanded) {
            mListener.onCollapse(ExpandablePanel.this, tagId, brandName);
            return;
            //      a = new ExpandAnimation(expendedWidth, mContentWidth);
            //      a.initialize(expendedWidth, mContentWidth, expendedWidth, mContentWidth);
            //      mListener.onCollapse(ExpandablePanel.this);
        } else {
            a = new ExpandAnimation(mContentWidth, expendedWidth);
            a.initialize(mContentWidth, mContentWidth, mContentWidth, mContentWidth);

        }
        a.setDuration(mAnimationDuration);
        a.setFillAfter(true);
        startAnimation(a);
        invalidate();
    }

    private class PanelToggler implements OnClickListener {
        public void onClick(View v) {
            //Log.d(TAG, "ANIMATE EXPAND");
            animateExpand();
        }
    }

    private class ExpandAnimation extends Animation implements Animation.AnimationListener {
        private final int mStartWidth;
        private final int mDeltaWidth;
        int delta;

        public ExpandAnimation(int startWidth, int endWidth) {
            mStartWidth = startWidth;
            mDeltaWidth = endWidth - startWidth;
            setAnimationListener(this);
        }

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            RelativeLayout.LayoutParams param = (RelativeLayout.LayoutParams) getLayoutParams();
            delta = param.width;
            if (param.width <= 0) {
                delta = 0;
            }
            param.width = (int) (mStartWidth + mDeltaWidth *
                    interpolatedTime);
            if (delta != 0 && !lhsAnimation) {
                delta = (int) (mStartWidth + mDeltaWidth *
                        interpolatedTime) - delta;
                param.leftMargin -= delta;
            }
            setLayoutParams(param);
            getParent().getParent().requestLayout();



//            Log.d("EXPANDABLE_PANEL", "Width: " + param.width + "leftmargin: "+ param.leftMargin +
//                    " - time: " +  interpolatedTime + " - delta: " + delta);
        }

        @Override
        public boolean willChangeBounds() {
            return true;
        }

        @Override
        public void onAnimationStart(Animation animation) {
        }

        @Override
        public void onAnimationEnd(Animation animation) {
            setText(text);
            requestLayout();
            mExpanded = !mExpanded;
            if (mExpanded) {
                mListener.onExpand(ExpandablePanel.this);
                if (!pivot.isGlammed() && !ownPost) {
                    GlamTask task = new GlamTask(ExpandablePanel.this, pivot.getId(), pivot.getGlamCount());
                    if(app != null){
                        app.executeAsyncTask(task, null);
                    }else{
                        task.execute();
                    }
                }
            } else {
                if(!autoClose){
                    mListener.onCollapse(ExpandablePanel.this, tagId, brandName);
                    autoClose = false;
                }

            }
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    }*/

}
