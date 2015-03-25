package com.mallardduckapps.fashiontalks.adapters;

import android.app.Activity;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.birin.gridlistviewadapters.Card;
import com.birin.gridlistviewadapters.ListGridAdapter;
import com.birin.gridlistviewadapters.dataholders.CardDataHolder;
import com.birin.gridlistviewadapters.utils.ChildViewsClickHandler;
import com.mallardduckapps.fashiontalks.FashionTalksApp;
import com.mallardduckapps.fashiontalks.R;
import com.mallardduckapps.fashiontalks.objects.GalleryItem;
import com.mallardduckapps.fashiontalks.objects.GalleryViewHolder;
import com.mallardduckapps.fashiontalks.utils.Constants;
import com.mallardduckapps.fashiontalks.utils.FTUtils;
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
    Activity activity;
    //Defines if adapter used on gallery or a posts - galleries is the parent of posts, posts are the parent of post
    boolean isGallery;
    boolean opensGallery;
    GalleryItemClicked galleryCallback;
    PostItemClicked postCallback;
    int cardWidth;
    StringBuilder builder;
    private final int TEXT_VIEW_CLICK_ID = 0;
    private String mainImagePath;

    public GalleryGridAdapter(Activity context, GalleryItemClicked callback, int totalCardsInRow, boolean isGallery) {
        super(context, totalCardsInRow);
        this.galleryCallback = callback;
        //this.opensGallery = opensGallery;
        builder = new StringBuilder(Constants.CLOUD_FRONT_URL).append("/");
        activity = context;
        options = ((FashionTalksApp)context.getApplication()).options;
        this.isGallery = isGallery;
    }

    public GalleryGridAdapter(Activity context, PostItemClicked callback, int totalCardsInRow, boolean isGallery) {
        super(context, totalCardsInRow);
        this.postCallback = callback;
        builder = new StringBuilder(Constants.CLOUD_FRONT_URL).append("/");
        activity = context;
        options = ((FashionTalksApp)context.getApplication()).options;
        this.isGallery = isGallery;
    }

    private void init(){

    }

    @Override
    protected Card<GalleryViewHolder> getNewCard(int cardWidth) {
        // Create card through XML (can be created programmatically as well.)
        View cardView = getLayoutInflater().inflate(
                R.layout.gallery_card_view, null);
        this.cardWidth = cardWidth;
        //Log.d(TAG, "CARD WIDTH: " + cardWidth);
        cardView.setMinimumHeight(cardWidth);
        mainImagePath = new StringBuilder(Constants.CLOUD_FRONT_URL).append("/").append(cardWidth).append("x").append(cardWidth).append("/").toString();

        // Now create card view holder.
        GalleryViewHolder viewHolder = new GalleryViewHolder();
        viewHolder.textView = (TextView) cardView.findViewById(R.id.titleText);
        viewHolder.textView.setTypeface(FTUtils.loadFont(activity.getAssets(), activity.getString(R.string.font_helvatica_md)));
        viewHolder.imageView = (ImageView) cardView.findViewById(R.id.coverPhoto);
        viewHolder.progressBar = (ProgressBar) cardView.findViewById(R.id.progress);
        return new Card<GalleryViewHolder>(cardView, viewHolder);
    }

    @Override
    protected void setCardView(CardDataHolder<GalleryItem> cardDataHolder,
                               final GalleryViewHolder cardViewHolder) {
        final GalleryItem item = cardDataHolder.getData();
        cardViewHolder.textView.setText(item.getTitle());
        String path = mainImagePath.concat(item.getCoverPath());
        //Log.d(TAG, "ADAPTER URL PATH: " + path + " - width: " + cardViewHolder.imageView.getWidth());
        displayImage(path, cardViewHolder, item);
        //cardViewHolder.Image.setText(item.getTag());
    }

    @Override
    protected void onCardClicked(GalleryItem cardData) {
/*        Toast.makeText(getContext(),
                "Card click " + cardData.getPositionText(), Toast.LENGTH_LONG)
                .show()*/;
    }

    @Override
    protected void registerChildrenViewClickEvents(GalleryViewHolder cardViewHolder,
                                                   ChildViewsClickHandler childViewsClickHandler) {
        childViewsClickHandler.registerChildViewForClickEvent(
                cardViewHolder.imageView, TEXT_VIEW_CLICK_ID);
    }

    private void displayImage(String path, final GalleryViewHolder cardViewHolder, final GalleryItem item){
        ImageLoader.getInstance()
                .displayImage(path, cardViewHolder.imageView, options, new SimpleImageLoadingListener() {
                    @Override
                    public void onLoadingStarted(String imageUri, View view) {
                        //cardViewHolder.progressBar.setProgress(0);
                        cardViewHolder.progressBar.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                        cardViewHolder.progressBar.setVisibility(View.GONE);
                        Log.d(TAG, "LOAD IMAGE FAILED");
                        item.setLoadingFailed(true);
                    }

                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                        cardViewHolder.progressBar.setVisibility(View.GONE);
                        item.setLoadingFailed(false);
                    }
                }, new ImageLoadingProgressListener() {
                    @Override
                    public void onProgressUpdate(String imageUri, View view, int current, int total) {
                        //cardViewHolder.progressBar.setProgress(Math.round(100.0f * current / total));
                    }
                });
    }

    @Override
    protected void onChildViewClicked(View clickedChildView, GalleryItem cardData,
                                      int eventId) {
        if (eventId == TEXT_VIEW_CLICK_ID) {
/*            Toast.makeText(getContext(),
                    "TextView click " + cardData.getPositionText() + " - GET ITEM ID: " + cardData.getId() + "-isGallery: " +isGallery,
                    Toast.LENGTH_LONG).show();*/

            if(cardData.isLoadingFailed()){
                String path = mainImagePath.concat(cardData.getCoverPath());
                ImageLoader.getInstance()
                        .displayImage(path, (ImageView)clickedChildView, options);
                cardData.setLoadingFailed(false);
                return;
            }

            if(galleryCallback != null){
                //if(galleryCallback != null){
                    galleryCallback.galleryOnItemClicked(cardData.getId(), cardData.getTitle(), cardData.getPosition());
               // }
            }else{
               // if(postCallback != null){
                Log.d(TAG, "ON POST CLICKED : " + cardData.getPosition());
                    postCallback.postOnItemClicked(cardData.getId(), cardData.getPosition());
               // }
            }
        }
    }

    // OPTIONAL SETUP
    @Override
    public int getCardSpacing() {
        return ( super.getCardSpacing()/2);
    } // 2*

    @Override
    protected void setRowView(View rowView, int arg1) {
        rowView.setBackgroundColor(getContext().getResources().getColor(
                R.color.white));
    }

    public interface GalleryItemClicked{
        public void galleryOnItemClicked(int galleryId,String galleryName, int galleryItemPosition);
    }


    public interface PostItemClicked{
        public void postOnItemClicked(int postId, int postItemPosition);
    }

}
