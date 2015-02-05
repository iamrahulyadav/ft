package com.mallardduckapps.fashiontalks.components;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mallardduckapps.fashiontalks.MainActivity;
import com.mallardduckapps.fashiontalks.R;

/**
 * Created by oguzemreozcan on 03/02/15.
 */
public class ExpandableTag extends TextView {

    private boolean mExpanded = false;
    private int expendedWidth = 0;
    private int mContentWidth = 0;
    private int mAnimationDuration = 0;
    private OnExpandListener mListener;
    private String text;
    Drawable icon;

    private boolean ltrExpand = true;


    public ExpandableTag(final Context context, int x, int y) {
        super(context);
        //this.setWillNotDraw(false);
        expendedWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, getResources()
                .getDisplayMetrics());
        mContentWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 30, getResources()
                .getDisplayMetrics());
        mAnimationDuration = 200;
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        //params.addRule(RelativeLayout.LEFT_OF, mHandle.getId());
        Log.d("EXPANDABLE_PANEL", "WIDTH: " + getWidth() + " - Height: " + getHeight());
        params.leftMargin = x; //- getWidth()/2;
        params.topMargin = y; //- getHeight()/2;
        setLayoutParams(params);
        icon = context.getResources().getDrawable(
                        R.drawable.glam_icon);

        getViewTreeObserver()
                .addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        icon.setBounds(0, 0, mContentWidth, getMeasuredHeight());
                        if(ltrExpand){
                            setCompoundDrawables(null, null, icon, null);
                        }else{
                            setCompoundDrawables(icon, null, null, null);
                        }
                        if (Build.VERSION.SDK_INT < 16) {
                            getViewTreeObserver().removeGlobalOnLayoutListener(this);
                        } else {
                            getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        }
                    }
                });

        setOnClickListener(new PanelToggler());

    }

    public void setOnExpandListener(OnExpandListener listener) {
        mListener = listener;
    }

    public void setExpendedWidth(int collapsedWidth) {
        expendedWidth = expendedWidth;
    }

    public void setAnimationDuration(int animationDuration) {
        mAnimationDuration = animationDuration;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    private class PanelToggler implements OnClickListener {
        public void onClick(View v) {
            Animation a;
            if (mExpanded) {
                a = new ExpandAnimation(expendedWidth, mContentWidth, ltrExpand);//mContentWidth
                // a = new ScaleAnimation(30, 1, 1,1);
                a.initialize(expendedWidth, mContentWidth, expendedWidth, mContentWidth);
                //a.setDuration((int)(mContentWidth / getContext().getResources().getDisplayMetrics().density));
                mListener.onCollapse(ExpandableTag.this);//mContent

            } else {
                a = new ExpandAnimation(mContentWidth, expendedWidth, ltrExpand); //mContentWidth
                // a = new ScaleAnimation(1, 30, 1, 1);
                a.initialize(mContentWidth, mContentWidth, mContentWidth, mContentWidth);
                //a.setDuration((int)(expendedWidth / getContext().getResources().getDisplayMetrics().density));
                mListener.onExpand(ExpandableTag.this);//mContent

            }
            a.setDuration(mAnimationDuration);
            a.setFillAfter(true);
            startAnimation(a);
        }
    }

    private class ExpandAnimation extends Animation implements Animation.AnimationListener {
        private final int mStartWidth;
        private final int mDeltaWidth;
        private final int mEndWidth;
        private final boolean rightAnimation;

        public ExpandAnimation(int startWidth, int endWidth, boolean rightAnimation) {
            mStartWidth = startWidth;
            mDeltaWidth = endWidth - startWidth;
            mEndWidth = endWidth;
            //this.view.setPivotX(1f);
            this.rightAnimation = rightAnimation;
            setAnimationListener(this);
        }

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            RelativeLayout.LayoutParams param = (RelativeLayout.LayoutParams) getLayoutParams();
            param.width = (int) (mStartWidth + mDeltaWidth *
                    interpolatedTime);
            setLayoutParams(param);
            Log.d("EXPANDABLE_PANEL", "Width: " + param.width);
/*           // }else{
                lp.width = (int) (mStartWidth + mDeltaWidth *
                        interpolatedTime);

                view.setX( (int)(lp.leftMargin - (mDeltaWidth *interpolatedTime)));
                view.setLayoutParams(lp);
                Log.d("EXPANDABLE_PANEL", "DELTA: " + mDeltaWidth *interpolatedTime);
                //lp.setLayoutDirection(LAYOUT_DIRECTION_RTL);
                //lp.leftMargin = (int)
            }*/
            invalidate();

        }

        @Override
        public boolean willChangeBounds() {
            return true;
        }

        @Override
        public void onAnimationStart(Animation animation) {

            //((TextView)mContent).setText("");
        }

        @Override
        public void onAnimationEnd(Animation animation) {
            // if(collapse){
            //}else{
            //if(mStartWidth == mContentWidth)
            setText(text);
            Log.d("ExpandablePanel", "TEXTVIEW SIZE: " + getWidth());
            requestLayout();
            mExpanded = !mExpanded;
            //else
            //   ((TextView)mContent).setText("");
            //}
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    }

    /**
     * Simple OnExpandListener interface
     */
    public interface OnExpandListener {
        public void onExpand(View handle);

        public void onCollapse(View handle);
    }

    private class DefaultOnExpandListener implements OnExpandListener {
        public void onCollapse(View handle) {
        }

        public void onExpand(View handle) {
        }
    }

}
