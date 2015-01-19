package com.mallardduckapps.fashiontalks.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.birin.gridlistviewadapters.Card;
import com.birin.gridlistviewadapters.ListGridAdapter;
import com.birin.gridlistviewadapters.dataholders.CardDataHolder;
import com.birin.gridlistviewadapters.utils.ChildViewsClickHandler;

import com.mallardduckapps.fashiontalks.FashionTalksApp;
import com.mallardduckapps.fashiontalks.R;
import com.mallardduckapps.fashiontalks.objects.GalleryItem;
import com.mallardduckapps.fashiontalks.objects.GalleryViewHolder;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

/**
 * Created by oguzemreozcan on 18/01/15.
 */
public class GalleryGridAdapter extends ListGridAdapter<GalleryItem, GalleryViewHolder> {

    DisplayImageOptions options;
    private final String TAG = "GalleryGripAdapter";

    public GalleryGridAdapter(Activity context, int totalCardsInRow) {
        super(context, totalCardsInRow);
        options = ((FashionTalksApp)context.getApplication()).options;
    }

    @Override
    protected Card<GalleryViewHolder> getNewCard(int cardWidth) {
        // Create card through XML (can be created programmatically as well.)
        View cardView = getLayoutInflater().inflate(
                R.layout.gallery_card_view, null);
        cardView.setMinimumHeight(cardWidth);

        // Now create card view holder.
        GalleryViewHolder viewHolder = new GalleryViewHolder();
        viewHolder.textView = (TextView) cardView.findViewById(R.id.titleText);
        viewHolder.imageView = (ImageView) cardView.findViewById(R.id.coverPhoto);
        viewHolder.progressBar = (ProgressBar) cardView.findViewById(R.id.progress);
        return new Card<GalleryViewHolder>(cardView, viewHolder);
    }

    @Override
    protected void setCardView(CardDataHolder<GalleryItem> cardDataHolder,
                               final GalleryViewHolder cardViewHolder) {
        GalleryItem item = cardDataHolder.getData();
        cardViewHolder.textView.setText(item.getTitle());
        Log.d(TAG, "ADAPTER: " + item.getCoverPath() + " - width: " + cardViewHolder.imageView.getWidth());
        ImageLoader.getInstance()
                .displayImage(item.getCoverPath(), cardViewHolder.imageView, options, new SimpleImageLoadingListener() {
                    @Override
                    public void onLoadingStarted(String imageUri, View view) {
                        cardViewHolder.progressBar.setProgress(0);
                        cardViewHolder.progressBar.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                        cardViewHolder.progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                        cardViewHolder.progressBar.setVisibility(View.GONE);
                    }
                }, new ImageLoadingProgressListener() {
                    @Override
                    public void onProgressUpdate(String imageUri, View view, int current, int total) {
                        cardViewHolder.progressBar.setProgress(Math.round(100.0f * current / total));
                    }
                });
        //cardViewHolder.Image.setText(item.getTitle());
    }

    @Override
    protected void onCardClicked(GalleryItem cardData) {
        Toast.makeText(getContext(),
                "Card click " + cardData.getPositionText(), Toast.LENGTH_LONG)
                .show();
    }

    private final int TEXT_VIEW_CLICK_ID = 0;

    @Override
    protected void registerChildrenViewClickEvents(GalleryViewHolder cardViewHolder,
                                                   ChildViewsClickHandler childViewsClickHandler) {
        childViewsClickHandler.registerChildViewForClickEvent(
                cardViewHolder.imageView, TEXT_VIEW_CLICK_ID);
    }

    @Override
    protected void onChildViewClicked(View clickedChildView, GalleryItem cardData,
                                      int eventId) {
        if (eventId == TEXT_VIEW_CLICK_ID) {
            Toast.makeText(getContext(),
                    "TextView click " + cardData.getPositionText(),
                    Toast.LENGTH_LONG).show();
        }
    }

    // OPTIONAL SETUP

    @Override
    public int getCardSpacing() {
        return (2 * super.getCardSpacing());
    }

    @Override
    protected void setRowView(View rowView, int arg1) {
  //      rowView.setBackgroundColor(getContext().getResources().getColor(
    //            R.color.simplest_list_background));
    }

}
