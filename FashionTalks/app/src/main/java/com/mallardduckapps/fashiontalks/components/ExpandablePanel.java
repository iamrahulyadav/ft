package com.mallardduckapps.fashiontalks.components;

/**
 * Created by oguzemreozcan on 29/01/15.
 */

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mallardduckapps.fashiontalks.R;
import com.mallardduckapps.fashiontalks.objects.Pivot;
import com.mallardduckapps.fashiontalks.tasks.GlamTask;
import com.mallardduckapps.fashiontalks.utils.FTUtils;
import com.mallardduckapps.fashiontalks.utils.TimeUtil;

public class ExpandablePanel extends TextView implements GlamTask.AsyncResponse {

    public boolean mExpanded = false;
    private int expendedWidth = 0;
    private int mContentWidth = 0;
    private int mAnimationDuration = 0;
    private Pivot pivot;
    private OnExpandListener mListener;
    private String text;
    private final String TAG = "EXPANDABLE_PANEL";
    private boolean lhsAnimation = false;
    //Meaning no pivot, draggable, no tasks works after click, glamCount 0
    private boolean ownPost = false;
    private boolean createPost = false;
    boolean nameGiven = false;
    private boolean selected = false;
    private boolean readyToDelete = false;

    public ExpandablePanel(final Context context, Pivot pivot, int x, int y, boolean lhsAnimation, boolean ownPost, boolean createPost) {
        super(context);
        this.lhsAnimation = lhsAnimation;
        this.ownPost = ownPost;
        this.pivot = pivot;
        Resources res = getResources();
        expendedWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, res
                .getDisplayMetrics());
        mContentWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, res.getDimension(R.dimen.glam_width), res
                .getDisplayMetrics());
        setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, res.getDisplayMetrics()));
        setTypeface(FTUtils.loadFont(context.getAssets(), context.getString(R.string.font_helvatica_thin)));
        mAnimationDuration = 200;
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, mContentWidth);
        //Log.d("EXPANDABLE_PANEL", "x: " + x + " - y: " + y);
        params.leftMargin = x;
        params.topMargin = y;
        setLhsAnimation(lhsAnimation);
        setBackgroundResource(R.drawable.glam_shape);
        setSingleLine();
        setMaxLines(1);
        setGravity(Gravity.CENTER);
        setClickable(true);
        setLayoutParams(params);
        if (!createPost) {
            setOnClickListener(new PanelToggler());
        } else {
            //setOnClickListener(new PanelToggler());
            //Change to delete icon
        }
    }

    @Override
    public boolean isSelected() {
        return selected;
    }

    @Override
    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public boolean isNameGiven() {
        return nameGiven;
    }

    public void setNameGiven(boolean nameGiven) {
        this.nameGiven = nameGiven;
    }

    public boolean isLhsAnimation() {
        return lhsAnimation;
    }

    public void setLhsAnimation(boolean lhsAnimation) {
        //if(ownPost){
        this.lhsAnimation = lhsAnimation;
        Drawable img = getResources().getDrawable(
                R.drawable.glam_dot_unpressed);
        img.setBounds(0, 0, mContentWidth, mContentWidth);
        if (!lhsAnimation) {
            setCompoundDrawables(null, null, img, null);
            //setPadding(5,0,5,0);// 5,0,0,0
        } else {
            setCompoundDrawables(img, null, null, null);
            //setPadding(5,0,5,0);
        }
        setCompoundDrawablePadding(0);
        // }
    }

    public boolean isReadyToDelete() {
        return readyToDelete;
    }

    public void setReadyToDelete(boolean readyToDelete) {
        Drawable img = null;
        if (!readyToDelete) {
            img = getResources().getDrawable(
                    R.drawable.glam_dot_unpressed);
            setBackgroundResource(R.drawable.glam_shape);
            setTagText(text, true);
            //setText(text);
        } else {
            img = getResources().getDrawable(
                    R.drawable.delete_circle);
            setBackgroundResource(R.drawable.glam_shape_delete);
            setText(getContext().getString(R.string.erase));
        }
        try {
            img.setBounds(0, 0, mContentWidth, mContentWidth);
        } catch (Exception e) {
            Log.e(TAG, "EXCEPTION ON EX PANEL - setReadyToDelete: " + e.getMessage());
        }

        if (!lhsAnimation && !readyToDelete) {
            setCompoundDrawables(null, null, img, null);
        } else {
            setCompoundDrawables(img, null, null, null);
        }
        this.readyToDelete = !this.readyToDelete;
    }

    public void setOnExpandListener(OnExpandListener listener) {
        mListener = listener;
    }

    public void setTagText(String text, boolean refresh) {
        this.text = text;
        setText(text);
        if (!refresh) {
            measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
            expendedWidth = getMeasuredWidth() + 20;
            setText("");
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec,
                             int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    public void processFinish(int glamCount, int totalGlamCount) {
        //Log.d(TAG, "PROCESS FINISH: " + glamCount);

        if (text.startsWith(pivot.getGlamCountPattern() + " | ")) {
            text = text.replaceFirst(pivot.getGlamCountPattern(), TimeUtil.getPatternedInteger(glamCount));
            pivot.setGlamCount(glamCount);
            setText(text);
            mListener.onTagGlammed(glamCount, totalGlamCount);
        }
        //new StringBuilder("").append(pivot.getGlamCount()).append(" | ").append(pivot.getTag()).toString();
    }

    public void runShrinkAnimation() {
        if (!mExpanded) {
            return;
        }
        Animation a = new ExpandAnimation(expendedWidth, mContentWidth);
        a.initialize(expendedWidth, mContentWidth, expendedWidth, mContentWidth);
        mListener.onCollapse(ExpandablePanel.this);
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
          //  return;
                a = new ExpandAnimation(expendedWidth, mContentWidth);
                a.initialize(expendedWidth, mContentWidth, expendedWidth, mContentWidth);
                mListener.onCollapse(ExpandablePanel.this);
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
            if (delta != 0 && lhsAnimation) {
                delta = (int) (mStartWidth + mDeltaWidth *
                        interpolatedTime) - delta;
                param.leftMargin -= delta;
            }

            setLayoutParams(param);

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
                    task.execute();
                }
            } else {
                mListener.onCollapse(ExpandablePanel.this);
            }
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    }

    public interface OnExpandListener {
        public void onExpand(View view);
        public void onCollapse(View view);
        public void onTagGlammed(int glamCount, int totalGlamCount);
    }

/*    private class DefaultOnExpandListener implements OnExpandListener {
        public void onCollapse(View view) {}
        public void onExpand(View view) {}
    }*/
}
