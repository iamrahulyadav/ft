package com.mallardduckapps.fashiontalks.components;

/**
 * Created by oguzemreozcan on 29/01/15.
 */
import android.animation.Animator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.view.animation.Transformation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mallardduckapps.fashiontalks.R;

public class ExpandablePanel extends RelativeLayout {

    private int mHandleId;
    private int mContentId;
    // Contains references to the handle and content views
   // private ImageButton mHandle;
    private TextView mContent;
    private ImageView mHandle;
    private boolean mExpanded = false;
    private int expendedWidth = 0;
    private int mContentWidth = 0;
    private int mAnimationDuration = 0;
    private OnExpandListener mListener;
    private String text;

    public ExpandablePanel(final Context context, int x, int y) {
        super(context);
        //this.setWillNotDraw(false);
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (inflater != null) {
            View view = inflater.inflate(R.layout.expandable_panel, this);
            mHandle = (ImageButton) view.findViewById(R.id.expand);
            mContent = (TextView) view.findViewById(R.id.value);
            expendedWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, getResources()
                    .getDisplayMetrics());
            mContentWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 30, getResources()
                    .getDisplayMetrics());
            mAnimationDuration = 200;
            LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            //params.addRule(RelativeLayout.LEFT_OF, mHandle.getId());
            Log.d("EXPANDABLE_PANEL", "WIDTH: " + getWidth() + " - Height: " + getHeight());
            params.leftMargin = x; //- getWidth()/2;
            params.topMargin = y; //- getHeight()/2;
            setLayoutParams(params);

            mContent.getViewTreeObserver()
                    .addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                        @Override
                        public void onGlobalLayout() {
                                //Drawable img = context.getResources().getDrawable(
                                //        R.drawable.glam_icon);
                                //img.setBounds(0, 0, mContentWidth, mContent.getMeasuredHeight());
                               // mContent.setCompoundDrawables(img, null, null, null);
                                //resize = true;
                                if (Build.VERSION.SDK_INT < 16) {
                                    mContent.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                                } else {
                                    mContent.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                                }
                                        //removeOnLayoutChangeListener(this);

                        }
                    });

            mHandle.setOnClickListener(new PanelToggler());
        }

    }

/*    @Override
    protected void dispatchDraw(Canvas canvas) {
        //canvas.scale(-1,1, getWidth()/2, getHeight()/2);
       // super.dispatchDraw(canvas);
       // canvas.restore();
    }*/

    public ExpandablePanel(Context context, AttributeSet attrs) {
        super(context, attrs);
        mListener = new DefaultOnExpandListener();
        TypedArray a = context.obtainStyledAttributes(
                attrs, R.styleable.ExpandablePanel, 0, 0);
        expendedWidth = (int) a.getDimension(
                R.styleable.ExpandablePanel_expendedWidth, 0.0f);
        mAnimationDuration = a.getInteger(
                R.styleable.ExpandablePanel_animationDuration, 250);
        int handleId = a.getResourceId(
                R.styleable.ExpandablePanel_handle, 0);

        if (handleId == 0) {
            throw new IllegalArgumentException(
                    "The handle attribute is required and must refer "
                            + "to a valid child.");
        }

        int contentId = a.getResourceId(
                R.styleable.ExpandablePanel_content, 0);
        if (contentId == 0) {
            throw new IllegalArgumentException(
                    "The content attribute is required and must " +
                            "refer to a valid child.");
        }
        mHandleId = handleId;
        mContentId = contentId;
        a.recycle();
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

    /**
     * This method gets called when the View is physically
     * visible to the user
     */
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
    }

    /**
     * This is where the magic happens for measuring the actual
     * (un-expanded) height of the content. If the actual height
     * is less than the collapsedHeight, the handle will be hidden.
     */
    @Override
    protected void onMeasure(int widthMeasureSpec,
                             int heightMeasureSpec) {
        // First, measure how high content wants to be
        //mContent.
            //    measure(MeasureSpec.UNSPECIFIED,heightMeasureSpec );
       // mContentWidth = //mContent.
             //   getMeasuredWidth();
        //TODO
        //if (mContentWidth < mCollapsedWidth) {
       //     mHandle.setVisibility(View.GONE);
       // } else {
       //     mHandle.setVisibility(View.VISIBLE);
       // }

        // Then let the usual thing happen
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);


    }

    /**
     * This is the on click listene
     *
     * r for the handle.
     * It basically just creates a new animation instance and fires
     * animation.
     */
    private class PanelToggler implements OnClickListener {
        public void onClick(View v) {
            Animation a;
            if (mExpanded) {
                a = new ExpandAnimation(expendedWidth,mContentWidth,mContent,true);//mContentWidth
                // a = new ScaleAnimation(30, 1, 1,1);
                a.initialize(expendedWidth,mContentWidth, expendedWidth, mContentWidth);

                RelativeLayout.LayoutParams param = (LayoutParams) mContent.getLayoutParams();
                param.addRule(RelativeLayout.LEFT_OF, mHandle.getId());
                mContent.setLayoutParams(param);
                //a.setDuration((int)(mContentWidth / getContext().getResources().getDisplayMetrics().density));
                mListener.onCollapse(mContent, mContent );//mContent

            } else {
                a = new ExpandAnimation(mContentWidth,expendedWidth,mContent, true); //mContentWidth
                // a = new ScaleAnimation(1, 30, 1, 1);
                RelativeLayout.LayoutParams param = (LayoutParams) mContent.getLayoutParams();
                param.addRule(RelativeLayout.RIGHT_OF, mHandle.getId());
                mContent.setLayoutParams(param);
                a.initialize(mContentWidth,mContentWidth, mContentWidth, mContentWidth);
                //a.setDuration((int)(expendedWidth / getContext().getResources().getDisplayMetrics().density));
                mListener.onExpand(mContent, mContent);//mContent

            }
            //a.setDuration(mAnimationDuration);
            //mContent.
            a.setDuration(mAnimationDuration);
            a.setFillAfter(true);
            mContent.invalidate();
            mExpanded = !mExpanded;
            //mContent.startAnimation(a);
        }
    }

    /**
     * This is a private animation class that handles the expand/collapse
     * animations. It uses the animationDuration attribute for the length
     * of time it takes.
     */
    private class ExpandAnimation extends Animation implements Animation.AnimationListener {
        private final int mStartWidth;
        private final int mDeltaWidth;
        private final int mEndWidth;
        private final View view;
        private final boolean rightAnimation;

        public ExpandAnimation( int startWidth, int endWidth, View view, boolean rightAnimation) {
            mStartWidth = startWidth;
            mDeltaWidth = endWidth - startWidth;
            mEndWidth = endWidth;
            this.view = view;
                //this.view.setPivotX(1f);
            this.rightAnimation = rightAnimation;

            setAnimationListener(this);
        }

        @Override
        protected void applyTransformation(float interpolatedTime,Transformation t) {
            //LayoutParams lp =
              //      (LayoutParams) view.getLayoutParams();
            //if(rightAnimation){
            RelativeLayout.LayoutParams param = (LayoutParams) view.getLayoutParams();
            if(!rightAnimation) {

                param.addRule(RelativeLayout.LEFT_OF, mHandle.getId());
                //view.setLayoutParams(param);
            }
            param.width = (int) (mStartWidth + mDeltaWidth *
                    interpolatedTime);
                view.setLayoutParams(param);
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
            mContent.setText(text);
            Log.d("ExpandablePanel", "TEXTVIEW SIZE: " + mContent.getWidth());
            mContent.requestLayout();
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
        public void onExpand(View handle, View content);
        public void onCollapse(View handle, View content);
    }

    private class DefaultOnExpandListener implements OnExpandListener {
        public void onCollapse(View handle, View content) {}
        public void onExpand(View handle, View content) {}
    }
}
