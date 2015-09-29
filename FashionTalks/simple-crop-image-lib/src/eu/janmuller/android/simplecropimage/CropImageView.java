package eu.janmuller.android.simplecropimage;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.PointF;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.FloatMath;
import android.util.Log;
import android.view.MotionEvent;

import java.util.ArrayList;

class CropImageView extends ImageViewTouchBase {

    ArrayList<HighlightView> mHighlightViews      = new ArrayList<HighlightView>();
    HighlightView            mMotionHighlightView = null;
    float mLastX, mLastY;
    int mMotionEdge;

    private Context mContext;

    @Override
    protected void onLayout(boolean changed, int left, int top,
                            int right, int bottom) {

        super.onLayout(changed, left, top, right, bottom);
        if (mBitmapDisplayed.getBitmap() != null) {
            for (HighlightView hv : mHighlightViews) {
                hv.mMatrix.set(getImageMatrix());
                hv.invalidate();
                if (hv.mIsFocused) {
                    centerBasedOnHighlightView(hv);
                }
            }
        }
    }

    public CropImageView(Context context, AttributeSet attrs) {

        super(context, attrs);
        this.mContext = context;
    }

    @Override
    protected void zoomTo(float scale, float centerX, float centerY) {

        super.zoomTo(scale, centerX, centerY);
        for (HighlightView hv : mHighlightViews) {
            hv.mMatrix.set(getImageMatrix());
            hv.invalidate();
        }
    }

    @Override
    protected void zoomIn() {

        super.zoomIn();
        for (HighlightView hv : mHighlightViews) {
            hv.mMatrix.set(getImageMatrix());
            hv.invalidate();
        }
    }

    @Override
    protected void zoomOut() {

        super.zoomOut();
        for (HighlightView hv : mHighlightViews) {
            hv.mMatrix.set(getImageMatrix());
            hv.invalidate();
        }
    }

    @Override
    protected void postTranslate(float deltaX, float deltaY) {

        super.postTranslate(deltaX, deltaY);
        for (int i = 0; i < mHighlightViews.size(); i++) {
            HighlightView hv = mHighlightViews.get(i);
            hv.mMatrix.postTranslate(deltaX, deltaY);
            hv.invalidate();
        }
    }

    // According to the event's position, change the focus to the first
    // hitting cropping rectangle.
    private void recomputeFocus(MotionEvent event) {

        for (int i = 0; i < mHighlightViews.size(); i++) {
            HighlightView hv = mHighlightViews.get(i);
            hv.setFocus(false);
            hv.invalidate();
        }

        for (int i = 0; i < mHighlightViews.size(); i++) {
            HighlightView hv = mHighlightViews.get(i);
            int edge = hv.getHit(event.getX(), event.getY());
            if (edge != HighlightView.GROW_NONE) {
                if (!hv.hasFocus()) {
                    hv.setFocus(true);
                    hv.invalidate();
                }
                break;
            }
        }
        invalidate();
    }
    int mode = -1;
    double oldDist = 0;
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        CropImage cropImage = (CropImage) mContext;
        if (cropImage.mSaving) {
            return false;
        }
        //Log.d("ON TOUCH", "ON TOUCH EVENT TRIGGERED");
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            //case MotionEvent.Action_P
            case MotionEvent.ACTION_DOWN:
              //  if (cropImage.mWaitingToPick) {
              //      recomputeFocus(event);
             //   } else {
                mode = 2;//DRAG;
                Log.d("ZOOM", "mode=DRAG " + mHighlightViews.size());
                    for (int i = 0; i < mHighlightViews.size(); i++) {
                        HighlightView hv = mHighlightViews.get(i);
                        int edge = hv.getHit(event.getX(), event.getY());
                        //if (edge != HighlightView.GROW_NONE) {
                            mMotionEdge = edge;
                            mMotionHighlightView = hv;
                            mLastX = event.getX();
                            mLastY = event.getY();
                            mMotionHighlightView.setMode(HighlightView.ModifyMode.Move);
                                   // (edge == HighlightView.MOVE)
                                     //       ? HighlightView.ModifyMode.Move
                                      //      : HighlightView.ModifyMode.Grow);
                           // break;
                        //}

                    }
               // }
                break;

            case MotionEvent.ACTION_POINTER_DOWN:
                oldDist = spacing(event);
                Log.d("ZOOM", "oldDist=" + oldDist);
                if (oldDist > 10f) {
                   // savedMatrix.set(matrix);
                    //midPoint(mid, event);
                    mode = 1;// zoom
                    Log.d("ZOOM", "mode=ZOOM");
                }
                break;
            case MotionEvent.ACTION_POINTER_UP:
                if(mMotionHighlightView != null){
                    //centerBasedOnHighlightView(mMotionHighlightView);
                    mMotionHighlightView.setMode(
                            HighlightView.ModifyMode.None);
                    mode = 0; // none
                    Log.d("ZOOM", "mode=NONE");
                }
                break;
            case MotionEvent.ACTION_UP:
                Log.d("ZOOM", "mode=ACTION UP " +cropImage.mWaitingToPick);
                if (cropImage.mWaitingToPick) {
                    for (int i = 0; i < mHighlightViews.size(); i++) {
                        HighlightView hv = mHighlightViews.get(i);
                        if (hv.hasFocus()) {
                            cropImage.mCrop = hv;
                            for (int j = 0; j < mHighlightViews.size(); j++) {
                                if (j == i) {
                                    continue;
                                }
                                mHighlightViews.get(j).setHidden(true);
                            }
                            //centerBasedOnHighlightView(hv);
                            ((CropImage) mContext).mWaitingToPick = false;
                            return true;
                        }
                    }
                } else if (mMotionHighlightView != null) {
                    //centerBasedOnHighlightView(mMotionHighlightView);
                    mMotionHighlightView.setMode(
                            HighlightView.ModifyMode.None);
                    mode = 0;
                }
                mMotionHighlightView = null;
                break;
            case MotionEvent.ACTION_MOVE:
                Log.d("DRAG or ZOOM", "MOVE " + mode);
                if(mode == 2){
                    Log.d("DRAG or ZOOM", "DRAG");
                    //if (cropImage.mWaitingToPick) {
                    //    recomputeFocus(event);
                    //} else

                    if (mMotionHighlightView != null ) {
                        mMotionHighlightView.handleMotion(mMotionEdge,
                                event.getX() - mLastX,
                                event.getY() - mLastY);
                        mLastX = event.getX();
                        mLastY = event.getY();
                        Log.d("DRAG", "DRAG");
                        if (true) {
                            // This section of code is optional. It has some user
                            // benefit in that moving the crop rectangle against
                            // the edge of the screen causes scrolling but it means
                            // that the crop rectangle is no longer fixed under
                            // the user's finger.
                            ensureVisible(mMotionHighlightView);
                        }
                    }

                }else
                if(mode == 1){
                    Log.d("DRAG or ZOOM", "ZOOM");
                    double newDist = spacing(event);
                    //Log.d("ZOOM", "newDist=" + newDist);
                    if (newDist > 10f) {
                        //matrix.set(savedMatrix);
                        double scale = newDist / oldDist;
                        Log.d("ZOOM", "scale=" + scale);
                        //ImageViewTouchBase.SCALE_RATE = scale;
                        if(scale >= 1){
                            //zoomTo(scale);
                            //mMotionHighlightView.setMode(HighlightView.ModifyMode.Grow);
                            //for (int i = 0; i < mHighlightViews.size(); i++) {
                            //    HighlightView hv = mHighlightViews.get(i);
                            //    hv.handleMotion(hv.G, 0 , 0);
                            //}
                        }

                        //matrix.postScale(scale, scale, mid.x, mid.y);
                    }
                }
                break;
        }

        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
                center(true, true);
                break;
            case MotionEvent.ACTION_MOVE:
                // if we're not zoomed then there's no point in even allowing
                // the user to move the image around.  This call to center puts
                // it back to the normalized location (with false meaning don't
                // animate).
                if (getScale() == 1F) {
                    //center(true, true);
                }
                break;
        }

        return true;
    }

    /** Determine the space between the first two fingers */
    private double spacing(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return Math.sqrt(x * x + y * y);
    }

    /** Calculate the mid point of the first two fingers */
    private void midPoint(PointF point, MotionEvent event) {
        float x = event.getX(0) + event.getX(1);
        float y = event.getY(0) + event.getY(1);
        point.set(x / 2, y / 2);
    }

    // Pan the displayed image to make sure the cropping rectangle is visible.
    private void ensureVisible(HighlightView hv) {

        Rect r = hv.mDrawRect;

        int panDeltaX1 = Math.max(0, mLeft - r.left);
        int panDeltaX2 = Math.min(0, mRight - r.right);

        int panDeltaY1 = Math.max(0, mTop - r.top);
        int panDeltaY2 = Math.min(0, mBottom - r.bottom);

        int panDeltaX = panDeltaX1 != 0 ? panDeltaX1 : panDeltaX2;
        int panDeltaY = panDeltaY1 != 0 ? panDeltaY1 : panDeltaY2;

        if (panDeltaX != 0 || panDeltaY != 0) {
            panBy(panDeltaX, panDeltaY);
        }
    }

    // If the cropping rectangle's size changed significantly, change the
    // view's center and scale according to the cropping rectangle.
    private void centerBasedOnHighlightView(HighlightView hv) {

        Rect drawRect = hv.mDrawRect;

        float width = drawRect.width();
        float height = drawRect.height();

        float thisWidth = getWidth();
        float thisHeight = getHeight();

        float z1 = thisWidth / width * .6F;
        float z2 = thisHeight / height * .6F;

        float zoom = Math.min(z1, z2);
        zoom = zoom * this.getScale();
        zoom = Math.max(1F, zoom);
        if ((Math.abs(zoom - getScale()) / zoom) > .1) {
            float[] coordinates = new float[]{hv.mCropRect.centerX(),
                    hv.mCropRect.centerY()};
            getImageMatrix().mapPoints(coordinates);
            zoomTo(zoom, coordinates[0], coordinates[1], 300F);
        }

        ensureVisible(hv);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        super.onDraw(canvas);
        for (int i = 0; i < mHighlightViews.size(); i++) {
            mHighlightViews.get(i).draw(canvas);
        }
    }

    public void add(HighlightView hv) {

        mHighlightViews.add(hv);
        invalidate();
    }
}