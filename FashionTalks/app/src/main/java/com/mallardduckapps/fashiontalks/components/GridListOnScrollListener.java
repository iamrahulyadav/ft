package com.mallardduckapps.fashiontalks.components;


import android.util.Log;
import android.widget.AbsListView;

/**
 * Created by oguzemreozcan on 21/01/15.
 */
public class GridListOnScrollListener implements AbsListView.OnScrollListener {

    //To avoid multiple calls
    private int preLast;
    OnScrolledToBottom callback;

    public GridListOnScrollListener(OnScrolledToBottom callback){
        this.callback = callback;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        //switch(view.getId()) {
         //   case android.R.id.list:

                // Make your calculation stuff here. You have all your
                // needed info from the parameters of this function.

                // Sample calculation to determine if the last
                // item is fully visible.
                final int lastItem = firstVisibleItem + visibleItemCount;
                if(lastItem == totalItemCount) {
                    if(preLast!=lastItem){ //to avoid multiple calls for last item
                        //Log.d("Last", "Last");
                        preLast = lastItem;
                        callback.reachedToEnd();
                    }
                }
        //}
    }

    public interface OnScrolledToBottom{
        public void reachedToEnd();
    }
}
