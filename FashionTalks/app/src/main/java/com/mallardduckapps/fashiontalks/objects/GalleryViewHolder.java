package com.mallardduckapps.fashiontalks.objects;

import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * Created by oguzemreozcan on 18/01/15.
 */
// Card View Holder class, these should hold all child-view references of a
// given card so that this references can be Re-used to update views (Without
// having to call findViewById each time on list scroll.)

public class GalleryViewHolder {
    public TextView textView;
    public ImageView imageView;
    public ProgressBar progressBar;
}
